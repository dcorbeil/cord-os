SUMMARY = "dtled kernel module"
LICENSE = "MIT"

SUMMARY = "Custom simple LED driver for controlling an LED through a GPIO"
HOMEPAGE = "https://github.com/dcorbeil/cord-km"
SRC_URI = "git://github.com/dcorbeil/cord-km.git;protocol=https;branch=main"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRCREV = "14645e2362405f567364dbe9080e6220d63a899e"

inherit module

# The git fetcher puts the extracted code in ${WORKDIR}/git whereas the tarball fetcher puts the source
# code in ${WORKDIR}/${BPN}-${PV} and by default S is set to the tarball fetcher's output directory
# See layers/third-party/bitbake/doc/bitbake-user-manual/bitbake-user-manual-fetching.rst for more details
S = "${WORKDIR}/git"
# The code we're building and installing is in dtled/ so we change the build directory accordingly.
# That way, no need to override do_build and do_install
B = "${S}/dtled"

# The inherit of module.bbclass will automatically name module packages with
# "kernel-module-" prefix as required by the oe-core build environment.
RPROVIDES_${PN} += "kernel-module-dtled"

# Prevent the versioned split package (kernel-module-dtled-6.16.0) from also providing
# "kernel-module-dtled" via RPROVIDES. Because the recipe PN already equals the module name,
# the virtual provision would overwrite the SPDX providers map and break do_rootfs SPDX generation.
# Another solution would be to rename the package or the .ko file so that they are different
KERNEL_MODULE_PROVIDE_VIRTUAL = "0"

KERNEL_MODULE_AUTOLOAD += "dtled"

