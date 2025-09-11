EXTRA_IMAGE_FEATURES ?= "allow-empty-password empty-root-password allow-root-login"
# EXTRA_IMAGE_FEATURES ?= "allow-root-login"
IMAGE_INSTALL:append = " nano dropbear"

INHERIT += "extrausers"
# Password is 'password'
# It was generated with: openssl passwd -1 password
# The $ were escaped with \$
EXTRA_USERS_PARAMS = "usermod -p '\$1\$GYn4f9V5\$IotMhfo57nY73zcA6SJih0' root;"

