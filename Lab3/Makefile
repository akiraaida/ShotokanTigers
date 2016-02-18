CC = clang++
CFLAGS = -std=c++11
SOURCES = main.cc account.cc commands.cc account_parser.cc account_status.cc transaction_io.cc
INCLUDES = account.h commands.h
OBJECTS = $(subst .cc,.o,$(SOURCES))
EXE = frontend.exe
.PHONY: clean help

frontend.exe : $(OBJECTS)
	$(CC) $(CFLAGS) $^ $(LIBS) -o $@

%.o : %.cc $(INCLUDES)
	$(CC) $(CFLAGS) -c $<

all : $(EXE)

clean:
	rm -f $(OBJECTS) $(EXE) transactions.txt *~

help:
	@echo "Valid targets:"
	@echo "  all:    generates all binary files"
	@echo "  clean:  removes .o and .exe files"
