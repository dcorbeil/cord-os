
# cord-os

## Getting Started

Setup `/opt` directory that'll be used for caching downloads and persistent data between sandboxes

```shell
mkdir -p /opt/cord/yocto/
sudo chmod 740 /opt/cord/
```

Checkout local dependencies

```shell
kas checkout
source layers/third-party/poky/oe-init-build-env
```

## Working with yocto

### Building

Building the image for default platform as specified in `.config.yaml`

```shell
bitbake cord-image
```

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

To clean the build directory

```shell
bitbake -c cleanall
```

Building only one package

```shell
bitbake <package name>
```

### Working with sub make module

FIXME: Refine this section

```bash
bitbake -c menuconfig virtual/bootloader
bitbake -c menuconfig virtual/kernel
bitbake busybox -c menuconfig # ??
```

Configs are written to .config in the build directory. So far I'm copying them manually back to
the git tree, It works but not ideal. For example:

```bash
cp build/tmp-glibc/work/dogbonedark-oe-linux-gnueabi/linux-kiss/6.16/build/defconfig <defconfig_location>
```

[This](https://stackoverflow.com/questions/61220838/change-kernel-config-but-defconfig-already-there) could be relevant also

### Cross-compiling kernel module

Paths might change as I'm writing these steps after my first try. There might be some non-ideal
things happening in here but it works. For now, it'll stay like that because it _works_

1. Add kernel dev package for the image in `cord-image.bb`

    ```shell
    TOOLCHAIN_TARGET_TASK += "kernel-devsrc"
    ```

2. Build the cross compilation SDK

    ```shell
    bitbake cord-image -c populate_sdk
    ```

3. Extract the SDK. This will prompt for an install location

    ```shell
    ./tmp-glibc/deploy/sdk/oecore-cord-image-x86_64-cortexa7t2hf-neon-vfpv4-raspberrypi2-toolchain-nodistro.0.sh
    ```

4. `cd` to the location where the toolchain was extracted, inside the `usr/src/kernel` directory. In
   my case it was `/opt/cord/test_yocto_sdk/sysroots/cortexa7t2hf-neon-vfpv4-oe-linux-gnueabi/usr/src/kernel`

    ```shell
    cd /opt/cord/test_yocto_sdk/sysroots/cortexa7t2hf-neon-vfpv4-oe-linux-gnueabi/usr/src/kernel
    ```

5. Prepare the kernel source for building

    ```shell
    make scripts
    make prepare
    ```

6. In the directory containing the kernel module to be built source the cross compiler environment

    ```shell
    source /opt/cord/test_yocto_sdk/environment-setup-cortexa7t2hf-neon-vfpv4-oe-linux-gnueabi
    ```

7. Build the kernel :)

    ```shell
    # Needed to tell the makefile where the kernel sources are
    export KERNEL_SRC=/opt/cord/test_yocto_sdk/sysroots/cortexa7t2hf-neon-vfpv4-oe-linux-gnueabi/usr/src/kernel
    # Build
    make all
    # If the kernel expect the kernel objects to be compressed. This can be done by simply doing
    xz -fv <kernel object file>.ko
    # or by doing the following if available
    make compress
    ```

### Flashing

```shell
sudo umount /dev/sdX*
sudo bmaptool copy build/tmp-glibc/deploy/images/raspberrypi2/cord-image-raspberrypi2.rootfs.wic.bz2 /dev/sdX
```

```shell
MACHINE=qemuarm bitbake cord-image
runqemu qemuarm nographic
```

I struggled so much adding a DTS overlay. [This](https://stackoverflow.com/questions/75684094/yocto-create-a-meta-layer-with-a-bbappend-file-adding-dts-flle-to-raspberry-pi/75734668#75734668) post helped me get through that hurdle.

The command that got me out of my misery was:

```shell
recipetool appendsrcfile -wm raspberrypi2 layers/meta-cord virtual/kernel layers/meta-cord/recipes-kernel/linux/files/<some overlay>.dts 'arch/${ARCH}/boot/dts/overlays/mydts-overlay.dts'
```

In this state it's just for raspberrypi2 but removing it makes it more generic.
