# qemu - initramfs notes

1. Build the initramfs image for `qemuarm`

    ```shell
    MACHINE=qemuarm bitbake cord-image-initramfs
    ```

2. Create the disk image and format it

    ```shell
    qemu-img create -f raw extra-data.img 512M
    mkfs.ext4 extra-data.img
    ```

3. Run `qemu` and map the created disk

    ```shell
    qemu-system-arm \
        -M virt \                                                                # Machine type: ARM 'virt' platform (generic, suitable for modern ARM kernels)
        -cpu cortex-a15 \                                                        # CPU model to emulate
        -m 512 \                                                                 # RAM size in MiB
        -kernel tmp-glibc/deploy/images/qemuarm/zImage \                        # Compressed Linux kernel image to boot
        -initrd tmp-glibc/deploy/images/qemuarm/cord-image-initramfs-qemuarm.cpio.gz \ # Initial RAM disk (initramfs) loaded into memory at boot
        -drive if=none,id=disk0,format=raw,file=extra-data.img \                # Declare a raw disk image, not attached to any bus yet (referenced by id=disk0)
        -device virtio-blk-device,drive=disk0                                   # Attach disk0 to the guest as a VirtIO block device (appears as /dev/vda)
    ```

    The disk will be automatically mounted to `/media/vda`. Changes will persist through reboots
    in the `.img` file
