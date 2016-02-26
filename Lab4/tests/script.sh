#!/bin/bash
cd inputs
cd chng

for f in *.in;
do
    echo "[ Running Test: $f ]"
    cat $f |../../../project/src/./frontend.exe accounts.txt
    echo "[ END ]"
done
