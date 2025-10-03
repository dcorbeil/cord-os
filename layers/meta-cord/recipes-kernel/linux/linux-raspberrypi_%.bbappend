FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI:append:raspberrypi2 = " file://led-ctrl-overlay.dts;subdir=git/arch/${ARCH}/boot/dts/overlays"

