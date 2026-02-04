SUMMARY = "A console-only image that fully supports the target device \
hardware."

LICENSE = "MIT"

IMAGE_FEATURES += "splash"


# FIXME: Figure out and document what is the perfect place for these settings to live.
# I can probably figure out that the settings for allowing root login and empty password belongs
# in a potential new debug image recipe.
# Although, for the packages to install logically they belong in the the distro so I need to find the
# right way to do that.

IMAGE_FEATURES += "allow-empty-password empty-root-password allow-root-login"

# nano for text editing
# dropbear for ssh access. Its a light-weight ssh server and DOESN'T support sftp
# udev-extraconf for dynamic device handling
# sl for fun
# IMAGE_INSTALL = "packagegroup-core-boot nano dropbear udev-extraconf sl libgpiod libgpiod-tools libgpiod-dev"
# IMAGE_INSTALL = "packagegroup-core-boot nano dropbear udev-extraconf sl kernel-module-hello-world"
IMAGE_INSTALL:append = " packagegroup-core-boot nano openssh udev-extraconf sl kernel-module-hello-world"

# Install kernel devsrc for building out-of-tree modules on target.
# TODO: This should be a development-only feature and live in a development image.
# Look at the following for an example:
#   layers/third-party/openembedded-core/meta/recipes-extended/images/core-image-kernel-dev.bb
TOOLCHAIN_TARGET_TASK += "kernel-devsrc"

INHERIT += "extrausers"
# Password is 'password'
# It was generated with: openssl passwd -1 password
# The $ were escaped with \$
EXTRA_USERS_PARAMS = "usermod -p '\$1\$GYn4f9V5\$IotMhfo57nY73zcA6SJih0' root;"



inherit core-image