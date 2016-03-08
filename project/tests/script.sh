#!/bin/bash
exe="../../src/./frontend.exe"
accounts="../../src/accounts.txt"
# Switch to the inputs directory
cd inputs
# For loop for all of the chng files
for f in *.in;
do
    # Create actual files for each test case (actual transfer and actual output)
    actOut=../${f%%.*}.aout
    actTrans=../${f%%.*}.atf
    touch $actTrans
    # Pipe the input file into the program and then output the console output to the actOut file
    $exe $accounts $actTrans < $f > $actOut
    # Check if there is a difference between the correct output and the output of the test case
    checkCons=$(diff $actOut ../outputs/${f%%.*}.out)
    checkTrans=$(diff $actTrans ../outputs/${f%%.*}.trans)
    if [ "$checkCons" == "" ] && [ "$checkTrans" == "" ]
    then
        # Output the test has passed in green
        echo -e "\e[1;30;42m[ '$f' Test Case Has Passed ]\e[0m"
    else
        # Output the test has failed in red
        echo -e "\e[1;30;41m[ '$f' Test Case Has Failed ]\e[0m"
        # Outputs the differences between the actual file and the output that should be there 
        echo -e "[ output ]"
        diff $actOut ../outputs/${f%%.*}.out -y
        echo -e "[ transactions ]"
        diff $actTrans ../outputs/${f%%.*}.trans -y
    fi
    # Move the temporary files to the actual files directory to compare actual/expected later
    mv $actOut ../actual
    mv $actTrans ../actual
done
