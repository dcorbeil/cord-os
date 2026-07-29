# qemu - initramfs notes

1. Build the `cord-image` image for `qemuarm`

    ```shell
    MACHINE=qemuarm bitbake cord-image
    ```

2. Run the qemu image

    ```shell
    runqemu tmp-glibc/deploy/images/qemuarm nographic
    ```

**Note**: It's important to point to `tmp-glibc/deploy/images/qemuarm` because `runqemu` expects
to find the `.qemuboot.conf` file in `build/tmp` and we use `build/tmp-glibc`

Press `Ctrl+A` then `X` to exit (this is the QEMU serial console escape sequence when running with `nographic`)
