# Simple recipe for using a mainline Linux kernel

LICENSE = "GPL-2.0-only"

inherit kernel
inherit kernel-devicetree

# Without -@ on the base DTB, there's no symbol table to resolve against, overlay application fails.
# Without it on the overlay, the compiler doesn't emit the fixup info, the loader doesn't know what
# to patch. It ends up generating the following error:
#   ...
#   Retrieving file: /extlinux/../ledoverlay.dtbo
#   failed on fdt_overlay_apply(): FDT_ERR_NOTFOUND
#   base fdt does not have a /__symbols__ node
#   make sure you've compiled with -@
#   Failed to apply overlay ../ledoverlay.dtbo, skipping
# For example, &am33xx_pinmux becomes an entry like /ocp/l4_wkup@44c00000/...
# This flag is applied globally to all dtc invocations in the kernel build so all base DTBs and
# our overlay are compiled with the necessary symbols/fixups for the base device tree and overlays
# to work together.
KERNEL_DTC_FLAGS += "-@"

# As we are using a fully mainline kernel there is no need to clone an
# entire git repository, which would take time and disk space. Downloading
# a tarball is enough. In real cases you might have an internal kernel
# repository with your branches, so it can make sense to fetch from
# there. Or you could be using a mainline kernel with only a few patches on
# top, and in that case downloading a tarball and applying your patches can
# be a good solution.
SRC_URI = "${KERNELORG_MIRROR}/linux/kernel/v6.x/linux-${PV}.tar.xz"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
S = "${WORKDIR}/linux-${PV}"

# For most realistic setups, a customized defconfig is preferred to the default
# architecture defconfig, to enable all the needed features and drivers while
# trimming the kernel and modules size disabling unneeded ones.
SRC_URI += "file://defconfig"
# Add out ledoverlay.dtso file to the kernel's build directory at the specified path so
SRC_URI += "file://ledoverlay.dtso;subdir=linux-${PV}/arch/arm/boot/dts/ti/omap"
