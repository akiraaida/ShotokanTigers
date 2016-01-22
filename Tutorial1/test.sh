#!/bin/bash

cd $2
for f in *.txt
do
	diff ../$1 $f
done

cd ..