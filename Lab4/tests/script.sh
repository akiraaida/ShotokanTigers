#!/bin/bash 
cd inputs
cd chng

for f in *.in;
do
    f=chng000.in 
    echo "Running Test: $f"
    cat $f #|../../../project/src/./frontend.exe accounts.txt    
    printf "\n"
done

echo login | ../../../project/src/./frontend.exe accounts.txt 
