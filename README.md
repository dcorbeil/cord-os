




```shell
mkdir -p /opt/cord/yocto/
sudo chmod 777 /opt/cord/
```


```shell
kas checkout
source layers/third-party/poky/oe-init-build-env
```

```shell
bitbake cord-image
```

```shell
sudo umount /dev/sdX*
sudo bmaptool copy build/tmp/deploy/images/raspberrypi2/cord-image-raspberrypi2.rootfs.wic.bz2 /dev/sdX
```

```shell
MACHINE=qemuarm bitbake cord-image
runqemu qemuarm nographic
```

I struggled so much adding a DTS overlay. [This](https://stackoverflow.com/questions/75684094/yocto-create-a-meta-layer-with-a-bbappend-file-adding-dts-flle-to-raspberry-pi/75734668#75734668) post helped me get through that hurdle.

The command that got me out of my misery was:

```shell
recipetool appendsrcfile -wm raspberrypi2 layers/meta-cord virtual/kernel layers/meta-cord/recipes-kernel/linux/files/led-ctrl-overlay.dts 'arch/${ARCH}/boot/dts/overlays/mydts-overlay.dts'
```

In this state it's just for raspberrypi2 but removing it makes it more generic.
