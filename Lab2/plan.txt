Test Plan
============

A test plan document, outlining how your tests are organized (in directories or whatever),
how they will be run (as shell scripts or DOS batch files), and how the output
will be stored and organized for reporting and comparison with later runs (make text file
printouts of any directory structures and script files created)

- We will be using a shell script since the operating system of choice is Linux.
- We will be containing our shell script, input files, expected output files, and pass/fail files in a directory beside the project directory.
- Input files will be containing the inputs that each test case needs to run. Each input will be on a newline representing a command being entered.
- A temporary file will be created to capture the console outputs and file modifications to be "diff"'d to the corresponding output file to create the pass/fail file.
- Output files will be containing the outputs that each test case is expecting. Each output will be on a newline and each output will be a console statement/prompt or a file modification.
- Pass/Fail files will be created with a date/time when the entire test completes which states which test cases passed and failed for that build.
- Shell script
	- Script will have a title for the test case and will consist of test steps and expected outputs. It will then run the test case and determine pass/fail based on the results (in the temporary file).

- wrapper/
	- project/
		- .exe file
		- src/
			- source files
			- resouces/
				- transactions.txt
				- accounts.txt
	- tests/
		- script file
		- temporary file during test case that captures outputs
		- inputs/
			- input files
		- outputs/
			- expected output files
		- passes_fails/
			- pass/fail files
