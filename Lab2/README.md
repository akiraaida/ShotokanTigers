Test Plan
============

- We will be using a shell script since the operating system of choice is Linux.
- We will be containing our shell script, input files, expected output files, and pass/fail files in a directory beside the project directory.
- Input files will be containing the inputs that each test case needs to run. Each input will be on a newline representing a command being entered.
- Two output files will have contain the outputs that each test case is expecting. Each output will be on a newline and each output will be a console statement/prompt or a file modification.
	- One console terminal output file
	- One transactions output file
- Two temporary files will be created to capture the console outputs and file modifications to be "diff"'d to the corresponding output files to create the pass/fail file.
	- One temp console terminal output file
	- One temp transactions output file
- A Pass/Fail file will be created with the date/time when the entire test completes which states which test cases passed and failed for that build.
	- eg. 10Feb237.pass
	- Inside it will have the title have the title of each test.in file and if it passed/failed.
	- The date/time will allow to cross reference in the future.
- Shell script
	- The script will execute each test case using their .in for their input and reference their .out and .trans against the two temporary files after completion/error. If the .out and the .trans are "diff"'d and return a difference the the test fails, otherwise the test passes. The test pass/fail file will have the .in file as a string and if it passed or failed.
		- eg. chng000.in - Pass
			  chng001.in - Fail
			  ...

Directory Structure

- wrapper/
	- project/
		- .exe file
		- src/
			- source files
			- resources/
				- transactions.txt
				- accounts.txt
	- tests/
		- script file
		- two temporary file during a test case that captures outputs
		- inputs/
			- subdirectories for functionality points/
				- input files
		- outputs/
			- subdirectories for functionality points/
				- expected output files
				- expected transaction files
		- passes_fails/
			- pass/fail files

A tree for the directories...
.
├── project/
│   └── src/
│       └── resources/
└── tests/
    ├── inputs/
    │   ├── chng/
    │   ├── crte/
    │   ├── delt/
    │   ├── depo/
    │   ├── dsbl/
    │   ├── enab/
    │   ├── logn/
    │   ├── logt/
    │   ├── payb/
    │   ├── tran/
    │   └── with/
    ├── outputs/
    │   ├── chng/
    │   ├── crte/
    │   ├── delt/
    │   ├── depo/
    │   ├── dsbl/
    │   ├── enab/
    │   ├── logn/
    │   ├── logt/
    │   ├── payb/
    │   ├── tran/
    │   └── with/
    └── passes_fails/