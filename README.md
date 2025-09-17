




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
