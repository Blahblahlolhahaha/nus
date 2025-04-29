#!/bin/bash

d0="$(dirname "$(readlink -f -- "$0")")"

py38="python3"

"$py38" -c "import cquery" "$1"

