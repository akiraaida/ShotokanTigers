#!/bin/bash

exe="../../../project/src/./frontend.exe"
accounts="../../../project/src/accounts.txt"
tempCons="../../tempCons.txt"

cd inputs
cd chng

# Change text colour to default
echo -e "\033[0m"
# For loop for all of the chng files
for f in *.in;
do
    # Output the test being run in white
    echo -e "\033[37m [ Running Test: $f ]"
    # Pipe the input file into the program and then output the
    # console output to the tempCons file
    cat $f | $exe $accounts > $tempCons 
    # Check if there is a difference between the correct output
    # and the output of the test case
    checkCons=$(diff $tempCons ../../outputs/chng/${f%%.*}.out)
    if [ "$checkCons" == "" ]
    then
        # Output the test has passed in green
        echo -e "\033[32m Test Case Has Passed"
    else
        # Output the test has failed in red
        echo -e "\033[31m Test Case Has Failed"
    fi
    # Output the test ended in white 
    echo -e "\033[37m [ Finished Test: $f ]"
    echo -e "\033[34m ------------------------------"
done
# Change text colour back to default
echo -e "\033[0m"
