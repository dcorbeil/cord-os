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

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
RPROVIDES_${PN} += "kernel-module-hello"

KERNEL_MODULE_AUTOLOAD += "helloworld"
