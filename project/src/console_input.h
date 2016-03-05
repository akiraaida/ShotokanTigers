/******************************************************************************
* console_input.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* The procedures contained within facilitate extraction of different values from
* std::cin in a standardized fashion. Each function extracts the entire line
* i.e. up to the newline.
*
* Example Usage:
*   std::cout << "Please enter amount to withdraw: ";
*   double amount = ConsoleInput::GetDouble();
*
*******************************************************************************/
#ifndef FRONTEND_CONSOLE_INPUT_H_
#define FRONTEND_CONSOLE_INPUT_H_

#include <string>

namespace BankFrontEnd {
namespace ConsoleInput {
/**
* Extracts a line of input from std::cin.
**/
std::string GetString();

/**
* Extracts a number from std::cin.
* Consumes a newline.
* Returns INT_MIN on failure.
**/
int GetInteger();

/**
* Extracts a floating-point value from std::cin.
* Consumes a newline.
* Returns FLT_MIN on failure.
**/
double GetDouble();

} //namespace ConsoleInput
} //namespace BankFrontEnd


#endif //FRONTEND_CONSOLE_INPUT_H_