# Simple recipe for using mainline U-Boot

require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

# Rewrite SRC_URI so we don't download the CVE patches: we fetch a more recent
# version were they have already been applied.
SRC_URI = "git://source.denx.de/u-boot/u-boot.git;protocol=https;branch=master"

# v2025.07
SRCREV = "e37de002fac3895e8d0b60ae2015e17bb33e2b5b"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

# Recent versions of U-Boot need gnutls headers on host machine.
DEPENDS += "gnutls-native"

# Give the variable a weak default value
UBOOT_EXTLINUX_FDT_OVERLAYS ??= ""

# Run code after the original function in uboot-extlinux-config.bbclass runs
python do_create_extlinux_config:append() {

    if d.getVar("UBOOT_EXTLINUX") != "1":
      return

    cfile = d.getVar('UBOOT_EXTLINUX_CONFIG')
    if not cfile:
        bb.fatal('Unable to read UBOOT_EXTLINUX_CONFIG')

    localdata = bb.data.createCopy(d)

    try:
        with open(cfile, 'a') as cfgfile:

            fdt_overlays = localdata.getVar('UBOOT_EXTLINUX_FDT_OVERLAYS')
            if fdt_overlays:
                cfgfile.write('\tFDTOVERLAYS %s\n' % fdt_overlays)

    except OSError:
        bb.fatal('Unable to open %s' % (cfile))
}

