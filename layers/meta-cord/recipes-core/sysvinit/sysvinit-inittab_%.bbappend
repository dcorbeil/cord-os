# The local directory needs to be added in the FILESEXTRAPATHS
# so that BitBake can find the inittab file.
FILESEXTRAPATHS:prepend := "${THISDIR}/sysvinit-inittab:"
