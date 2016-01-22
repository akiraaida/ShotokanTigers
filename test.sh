#!/bin/bash

printf "\nPreparing to identify the differencecs between files.\n"
printf "1. $1\n"
printf "2. $2\n"
printf "The differences between the two files are...\n"
diff "$1" "$2"