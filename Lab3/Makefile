CXX = g++
CXXFLAGS = -Wall -Wextra -std=c++11
SOURCES = main.cc account.cc commands.cc account_parser.cc account_status.cc transaction_io.cc
INCLUDES = account.h commands.h account_parser.h
OBJECTS = $(subst .cc,.o,$(SOURCES))
EXE = frontend.exe
.PHONY: clean help

frontend.exe : $(OBJECTS)
	$(CXX) $(CXXFLAGS) $^ $(LIBS) -o $@

%.o : %.cc $(INCLUDES)
	$(CXX) $(CXXFLAGS) -c $<

all : $(EXE)

clean:
	rm -f $(OBJECTS) $(EXE) transactions.txt *~

help:
	@echo "Valid targets:"
	@echo "  all:    generates all binary files"
	@echo "  clean:  removes .o and .exe files"
