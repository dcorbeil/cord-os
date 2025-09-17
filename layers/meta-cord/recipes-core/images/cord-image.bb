SUMMARY = "A console-only image that fully supports the target device \
hardware."

IMAGE_FEATURES += "splash"

LICENSE = "MIT"

inherit core-image

EXTRA_IMAGE_FEATURES ?= "allow-empty-password empty-root-password allow-root-login"
# EXTRA_IMAGE_FEATURES ?= "allow-root-login"

# nano for text editing
# dropbear for ssh access. Its a light-weight ssh server and DOESN'T support sftp
# udev-extraconf for dynamic device handling
IMAGE_INSTALL:append = " nano dropbear udev-extraconf"

INHERIT += "extrausers"
# Password is 'password'
# It was generated with: openssl passwd -1 password
# The $ were escaped with \$
EXTRA_USERS_PARAMS = "usermod -p '\$1\$GYn4f9V5\$IotMhfo57nY73zcA6SJih0' root;"

