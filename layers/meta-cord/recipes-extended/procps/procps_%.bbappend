

# Tell bitbake to first search in files/ when trying to resolve files as defined in
# the upstream repice.
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"