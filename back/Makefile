ROOT_PACKAGE = banksys
SOURCES = $(ROOT_PACKAGE)/Account.java $(ROOT_PACKAGE)/Transaction.java $(ROOT_PACKAGE)/TransactionType.java $(ROOT_PACKAGE)/FileParser.java $(ROOT_PACKAGE)/FileUpdater.java $(ROOT_PACKAGE)/TransactionCalculator.java $(ROOT_PACKAGE)/Backend.java
OBJECTS = $(subst .java,.class,$(SOURCES))
JFLAGS = -Xlint:unchecked

#prepare:
#	javac backend/Account.java
#	javac backend/Transaction.java
#	javac backend/TransactionType.java
#	javac backend/FileParser.java
#	javac backend/FileUpdater.java
#	javac backend/TransactionCalculator.java
#	javac backend/Backend.java

all: $(OBJECTS)

%.class : %.java
	javac $(JFLAGS) $(SOURCES)

run:
	java -ea:$(ROOT_PACKAGE).TransactionCalculator $(ROOT_PACKAGE).Backend master.txt trans1.txt trans2.txt trans3.txt

clean:
	rm $(ROOT_PACKAGE)/*.class
	rm concat.txt
	rm newMaster.txt
	rm newCurr.txt