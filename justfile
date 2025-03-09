### Environment ###
# - just >= 1.39.0
# - gradle >= 8.13
# - grc >= 1.13
# - trick >= 1.0.6

run:
    @gradle bootRun --quiet --console=plain

dev:
    @grc -c config.grc gradle bootRun --quiet --console=plain

clear:
    rm -rf build

trick-encrypt:
    source trick.profile.sh && trick encrypt SUPERVISOR_SERVER_TRICK_SECRET

trick-decrypt:
    source trick.profile.sh && trick decrypt SUPERVISOR_SERVER_TRICK_SECRET