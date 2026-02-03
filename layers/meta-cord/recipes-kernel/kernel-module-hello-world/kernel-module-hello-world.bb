SUMMARY = "Hello world kernel module"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

# This file's implementation was inspired from:
# layers/third-party/openembedded-core/meta-skeleton/recipes-kernel/hello-mod/hello-mod_0.1.bb

inherit module

SRC_URI = "file://helloworld.c \
           file://Makefile \
        "

S = "${WORKDIR}"

RPROVIDES_${PN} += "kernel-module-hello"

KERNEL_MODULE_AUTOLOAD += "helloworld"

# Somehow this works for building and installing the module. It seems like there is another more
# manual and granular way of doing it with do_compile and do_install functions.
# See: layers/third-party/meta-openembedded/meta-oe/recipes-support/vboxguestdrivers/vboxguestdrivers_7.0.14.bb

# do_compile() {
#         oe_runmake
# }

# do_install() {
#         install -d ${D}${base-libdir}/modules/${KERNEL_VERSION}/extra
#         install -m 0644 helloworld.ko ${D}${base-libdir}/modules/${KERNEL_VERSION}/extra
# }
