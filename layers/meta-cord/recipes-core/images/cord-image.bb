SUMMARY = "A console-only image that fully supports the target device \
hardware."

IMAGE_FEATURES += "splash"

LICENSE = "MIT"

inherit core-image

# FIXME: Figure out and document what is the perfect place for these settings to live.
# I can probably figure out that the settings for allowing root login and empty password belongs
# in a potential new debug image recipe.
# Although, for the packages to install logically they belong in the the distro so I need to find the
# right way to do that.

EXTRA_IMAGE_FEATURES ?= "allow-empty-password empty-root-password allow-root-login"
# EXTRA_IMAGE_FEATURES ?= "allow-root-login"

# nano for text editing
# dropbear for ssh access. Its a light-weight ssh server and DOESN'T support sftp
# udev-extraconf for dynamic device handling
IMAGE_INSTALL:append = " nano dropbear udev-extraconf sl"

INHERIT += "extrausers"
# Password is 'password'
# It was generated with: openssl passwd -1 password
# The $ were escaped with \$
EXTRA_USERS_PARAMS = "usermod -p '\$1\$GYn4f9V5\$IotMhfo57nY73zcA6SJih0' root;"

