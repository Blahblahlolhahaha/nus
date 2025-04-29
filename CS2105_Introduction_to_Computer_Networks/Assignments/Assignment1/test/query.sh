#!/bin/bash

d0="$(dirname "$(readlink -f -- "$0")")"

py38="python"

"$py38" -c "import cquery" "$1"

