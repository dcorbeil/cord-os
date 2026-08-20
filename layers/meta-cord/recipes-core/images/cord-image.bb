SUMMARY = "A console-only image that fully supports the target device \
hardware."

LICENSE = "MIT"

# FIXME: Figure out and document what is the perfect place for these settings to live.
# I can probably figure out that the settings for allowing root login and empty password belongs
# in a potential new debug image recipe.
# Although, for the packages to install logically they belong in the the distro so I need to find the
# right way to do that.

# IMAGE_FEATURES += "allow-empty-password empty-root-password allow-root-login"
IMAGE_FEATURES += "tools-sdk"
# Relevant info on sources.list:
# https://community.toradex.com/t/how-to-add-apt-package-manager-in-yocto-project/18849/2
# TODO: Validate if PACKAGE_CLASSES = "package_deb" is needed. Currently placed in .config.yml
IMAGE_FEATURES += "package-management"

# nano for text editing
# openssh for ssh access. Unlike (default) dropbear it provides an ftp server
# sl for fun
CORE_IMAGE_EXTRA_INSTALL += "nano openssh sl"

# My own kernel modules
CORE_IMAGE_EXTRA_INSTALL += "kernel-module-hello-world kernel-module-dtled"

# Bunch of fun useful commands
CORE_IMAGE_EXTRA_INSTALL += "packagegroup-core-full-cmdline-utils"

# TODO: This belongs in a dev image
CORE_IMAGE_EXTRA_INSTALL += "i2c-tools"

# Install kernel devsrc for building out-of-tree modules on target.
# TODO: This should be a development-only feature and live in a development image.
# Look at the following for an example:
#   layers/third-party/openembedded-core/meta/recipes-extended/images/core-image-kernel-dev.bb

# Variable info: https://stackoverflow.com/questions/60113300/toolchain-host-task-vs-toolchain-target-task
# TOOLCHAIN_TARGET_TASK += "kernel-devsrc"

# packagegroup-core-buildessential also exists and is a smaller version of *-buildessential
KERNEL_DEV_TOOLS ?= "packagegroup-core-sdk kernel-devsrc"
KERNEL_DEV_MODULE ?= "kernel-modules"

CORE_IMAGE_EXTRA_INSTALL += "${KERNEL_DEV_TOOLS} \
                             ${KERNEL_DEV_MODULE} \
                            "

INHERIT += "extrausers"
# Password is 'password'
# It was generated with: openssl passwd -1 password
# The $ were escaped with \$
EXTRA_USERS_PARAMS = "usermod -p '\$1\$GYn4f9V5\$IotMhfo57nY73zcA6SJih0' root;"

inherit core-image