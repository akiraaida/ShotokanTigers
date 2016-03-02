#!/bin/bash
exe="../../src/./frontend.exe"
accounts="../../src/accounts.txt"
trans="../../src/transactions.txt"
tempCons="../tempCons.txt"
# Switch to the inputs directory
cd inputs
# For loop for all of the chng files
for f in *.in;
do
    # Output the test being run in white
    echo -e "\t\e[1;30;47m[ Running Test: $f ]\e[0m"
    # If the transactions file exists, delete it since it will have output for other test cases
    if [ -f $trans ]
    then
        rm $trans       
    fi
    # Create an empty transaction file
    touch $trans
    # Pipe the input file into the program and then output the console output to the tempCons file
    cat $f | $exe $accounts $trans > $tempCons
    # Check if there is a difference between the correct output and the output of the test case
    checkCons=$(diff $tempCons ../outputs/${f%%.*}.out)
    checkTrans=$(diff $trans ../outputs/${f%%.*}.trans)
    if [ "$checkCons" == "" ] && [ "$checkTrans" == "" ]
    then
        # Output the test has passed in green
        echo -e "\t\e[1;30;42m[ Test Case Has Passed ]\e[0m"
    else
        # Output the test has failed in red
        echo -e "\t\e[1;30;41m[ Test Case Has Failed ]\e[0m"
        #echo $checkCons
        #echo $checkTrans
    fi
    # Output the test ended in white 
    echo -e "\t\e[1;30;47m[ Finished Test: $f ]\e[0m\n"
done
# Delete files made during tests
if [ -f $tempCons ]
then
    rm $tempCons
fi
if [ -f $trans ]
then
    rm $trans       
fi
