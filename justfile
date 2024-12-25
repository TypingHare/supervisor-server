### Dependencies ###
# - gradle > 8.12
# - grc > 1.13
# - trick > 1.0.6

dev:
    @grc -c grc.config.ini gradle bootRun --quiet

clear:
    rm -rf build

trick-encrypt:
    source trick.profile.sh && trick encrypt

trick-decrypt:
    source trick.profile.sh && trick decrypt
