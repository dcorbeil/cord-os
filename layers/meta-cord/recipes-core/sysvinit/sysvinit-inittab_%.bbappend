# The local directory needs to be added in the FILESEXTRAPATHS
# so that BitBake can find the inittab file.
FILESEXTRAPATHS:prepend := "${THISDIR}/sysvinit-inittab:"

# The :append in SRC_URI:append doesn't seen to be needed. Probably because we want to force using
# the inittab file from this layer.
# It looks like we are overriding the input file from the sysvinit-inittab recipe. This means
# we don't need to update the do_install() function.
SRC_URI = "\
                file://inittab \
                file://start_getty \
                  "

