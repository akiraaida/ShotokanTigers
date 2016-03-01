#!/bin/bash

exe="../../../project/src/frontend.exe"
transactions="../../../project/src/transactions.txt"
accounts="../../../project/src/accounts.txt"
tempCons="../../tempCons.txt"

cd inputs
cd chng

# For loop for all of the chng files
for f in *.in;
do
    # Output the test being run in white
    echo -e "\t\e[1;30;47m[ Running Test: $f ]\e[0m"
    # Pipe the input file into the program and then output the
    # console output to the tempCons file
    cat $f | $exe $accounts > $tempCons

    # Check if there is a difference between the correct output
    # and the output of the test case
    checkCons=$(diff $tempCons ../../outputs/chng/${f%%.*}.out -w)
    if [ "$checkCons" == "" ]
    then
        # Output the test has passed in green
        echo -e "\t\e[1;32;42m[ Output File Match ]\e[0m"
    else
        # Output the test has failed in red
        echo -e "\t\e[1;31;41m[ Output File Mismatch ]\e[0m"
        #diff $tempCons ../../outputs/chng/${f%%.*}.out -w
    fi

    # Check if there is a difference between the transaction file
    # and the output of the test case
    checkTrans=$(diff $transactions ../../outputs/chng/${f%%.*}.trans -w)
    if [ "$checkTrans" == "" ]
    then
        # Output the test has passed in green
        echo -e "\t\e[1;32;42m[ Transaction File Match ]\e[0m"
    else
        # Output the test has failed in red
        echo -e "\t\e[1;31;41m[ Transaction File Mismatch ]\e[0m"
        #diff $transactions ../../outputs/chng/${f%%.*}.trans -w
    fi

    # output if test passed
    #checkBoth= ["$checkCons" == "" && "$checkTrans" == ""]
    if [ "$checkCons" == ""  -a  "$checkTrans" == "" ]
    then
        # Output the test has passed in green
        echo -e "\t\e[1;32;42m[ Test Case Has Passed ]\e[0m"
    else
      # Output the test has failed in red
      echo -e "\t\e[1;31;41m[ Test Case Has Failed ]\e[0m"
    fi


    # Output the test ended in white
    echo -e "\t\e[1;30;47m[ Finished Test: $f ]\e[0m\n"
done
