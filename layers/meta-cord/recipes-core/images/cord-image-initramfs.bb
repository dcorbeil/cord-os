require cord-image.bb

SUMMARY = "Initramfs variant of cord-image"

# Emit initramfs artifacts instead of normal rootfs image types.
IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
# Avoid circular dependencies when INITRAMFS_IMAGE points at this image.
IMAGE_FSTYPES:remove = " wic wic.bmap"
IMAGE_NAME_SUFFIX ?= ""

# Freeze package list at parse-time for image construction.
PACKAGE_INSTALL = "${IMAGE_INSTALL}"

# Adding kernel stuff to the initramfs would make it too big and not fit in ram.
# TODO: Put the kernel dev stuff in a new dev image so we don't have that problem in this "prod"
# version
KERNEL_DEV_TOOLS = ""
KERNEL_DEV_MODULE = ""
CORE_IMAGE_EXTRA_INSTALL = ""
# IMAGE_INSTALL:remove = " kernel-module-hello-world"

# Keep kernel image packages out of the initramfs payload.
PACKAGE_EXCLUDE += "kernel-image-*"
