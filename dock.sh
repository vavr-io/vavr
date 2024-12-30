#!/bin/zsh

docker run --rm -v .:/mnt alpine sh -c \
    "sh /mnt/nested.sh"