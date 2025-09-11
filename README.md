




```shell
mkdir -p /opt/cord/yocto/
sudo chmod 777 /opt/cord/
```


```shell
kas checkout
source layers/third-party/poky/oe-init-build-env
```

```shell
bitbake core-image-base
```

```shell
sudo umount /dev/sdX*
sudo bmaptool copy /opt/cord/yocto/tmp/deploy/images/raspberrypi2/core-image-base-raspberrypi2.rootfs.wic.bz2 /dev/sdX
```

```shell
MACHINE=qemuarm bitbake core-image-base
runqemu qemuarm nographic
```
