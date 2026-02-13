
# cord-os

`cord-os` is a small Yocto playground for the [BeagleBone Black](https://www.beagleboard.org/boards/beaglebone-black). It includes a minimal BSP, examples
showing how to configure and customize U-Boot, how to add and build a basic package, and how to
build the Linux kernel. The repo can generate an SDK for cross-development and provides example
recipes and configs to get you started quickly.

This project doesn't use `poky`. As much as `poky` is awesome, it does a lot of heavy lifting. This
can be great when wanting to have something up and running quickly. It is not so great when trying
to learn Yocto as it does so many things for the user which makes it a bit hard to understand what
is actually happening behind the scenes.

Maybe surprisingly, this project doesn't support the Raspberry Pi. This is because so many Raspberry Pi
Yocto tutorials use [meta-raspberrypi](https://github.com/agherzan/meta-raspberrypi) and like `poky`,
this layer does so many things. Too many things. I elected to use the BBB instead and refer to the
more easily digestible [meta-ti](https://github.com/TexasInstruments/meta-ti) if needed.

The very good [simplest-yocto-setup](https://github.com/bootlin/simplest-yocto-setup) was used to help
kickstart this project.

## Prerequisites

The basic Yocto dependencies are needed and instructions can be found [here](https://docs.yoctoproject.org/brief-yoctoprojectqs/index.html)

```bash
sudo apt install build-essential chrpath cpio debianutils diffstat file gawk gcc git iputils-ping
                libacl1 locales python3 python3-git python3-jinja2 python3-pexpect python3-pip
                python3-subunit socat texinfo unzip wget xz-utils zstd
```

This project uses `kas` to manage the Yocto build. Kas as proven to be fairly useful for quickly and
effectively setup the project even if it's one more project dependency. It allows to setup external
layers at the correct place as well as managing their version. Git submodules are an alternative to
accomplish the same thing but I found that `kas` does a better and simpler job.

You can install it using [pipx](https://github.com/pypa/pipx):

```shell
pipx install kas
```

## Getting Started

Setup `/opt` directory that'll be used for caching downloads and Yocto's shared state cache. This
will allow other `cord-os` sandboxes to reuse the same package downloads and build cache which will
increase build speed across sandboxes.

```shell
# Setup cord-os common directory
mkdir -p /opt/cord/yocto/
sudo chmod 740 /opt/cord/

# Sets up external layers as well as configuring various download and build directories used by yocto
kas checkout

# Initialize build environment. Needs to be done on each new terminal
source layers/third-party/openembedded-core/oe-init-build-env
```

Building and flashing SD card

```shell
# By default, this will build for the image specified in .config.yaml
bitbake cord-image

# Once SD card is plugged in, unmount it to allow bmaptool to write to it. Replace X with the device
# path given by your OS. Tip: use dmesg after plugging the SD card to find out where the device is
umount /dev/sdX*
# Zoom zoom much faster than dd
sudo bmaptool copy build/tmp-glibc/deploy/images/beaglebone-black/cord-image-beaglebone-black.rootfs.wic /dev/sdX
```

Thanks to `openembedded-core`, `quemu` support comes for free. It can be pretty handy sometimes

```bash
# Building for a non-default image can be accomplished by setting the MACHINE environment variable
# For example building and running the image for qemuarm machine
MACHINE=qemuarm bitbake cord-image
runqemu qemuarm nographic
```

Various helpful commands

```shell
# Build one single package
bitbake <recipe name>

# Show recipe versions for all recipes
bitbake -s

# Show variables and where variables are set
bitbake -e cord-image
```

```

### Cross-compiling kernel module

Paths might change as I'm writing these steps after my first try. There might be some non-ideal
things happening in here but it works. For now, it'll stay like that because it _works_. Yocto calls
this SDK the [Standard SDK](https://docs.yoctoproject.org/5.0.15/sdk-manual/using.html#using-the-standard-sdk)

Eventually a proper `cord-image-dev` will be created as this is where all of the dev related stuff
should live.

1. Build the cross compilation SDK

    ```shell
    bitbake cord-image -c populate_sdk
    ```

2. Extract the SDK. This will install it at `/opt/cord/cord_sdk-x86_64`

    ```shell
    ./tmp-glibc/deploy/sdk/oecore-cord-image-x86_64-armv7at2hf-neon-beaglebone-black-toolchain-nodistro.0.sh -d /opt/cord/cord_sdk-x86_64
    ```

3. `cd` to the location where the toolchain was extracted, inside the `usr/src/kernel` directory.

    ```shell
    cd /opt/cord/cord_sdk-x86_64/sysroots/armv7at2hf-neon-oe-linux-gnueabi/usr/src/kernel
    ```

4. In the directory containing the kernel module to be built, `source` the cross compiler environment.
   This needs to be done every time a new shell is created. I suppose it doesn't absolutely needs to
   be done if the same variables are set manually elsewhere.

    ```shell
    source /opt/cord/cord_sdk-x86_64/environment-setup-armv7at2hf-neon-oe-linux-gnueabi
    ```

5. Prepare the kernel source for building. [This](https://stackoverflow.com/a/67335209) SO post helped me with that step

    ```shell
    make scripts
    make prepare
    ```

6. Build :)

    ```shell
    # Needed to tell the makefile where the kernel sources are and what compile options to use
    export KERNEL_SRC=/opt/cord/cord_sdk-x86_64/sysroots/armv7at2hf-neon-oe-linux-gnueabi/usr/src/kernel
    # Build kernel module. For example: layers/meta-cord/recipes-kernel/kernel-module-hello-world/files
    make all
    ```

## Troubleshooting

If you run on Ubuntu 24.04, you might encounter the following error when running `bitbake`:

```shell
ERROR: PermissionError: [Errno 1] Operation not permitted

During handling of the above exception, another exception occurred:

Traceback (most recent call last):
  File "/home/dcorbeil/git/cord-os/layers/third-party/poky/bitbake/bin/bitbake-worker", line 278, in child
    bb.utils.disable_network(uid, gid)
  File "/home/dcorbeil/git/cord-os/layers/third-party/poky/bitbake/lib/bb/utils.py", line 1696, in disable_network
    with open("/proc/self/uid_map", "w") as f:
PermissionError: [Errno 1] Operation not permitted
```

To fix the issue, run the following command ([source](https://lists.yoctoproject.org/g/yocto/topic/106192359)):

```shell
sudo apparmor_parser -R /etc/apparmor.d/unprivileged_userns
```
