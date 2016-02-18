#ifndef BANK_FRONTEND_ACCOUNT_PARSER_H_
#define BANK_FRONTEND_ACCOUNT_PARSER_H_
/******************************************************************************
* account_parser.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Contains utility to parse in bank customer information from file.
*
*******************************************************************************/
#include "account.h"

namespace BankFrontEnd {
namespace AccountParser {
/**
* Convert from file to accounts map.
**/
std::map<std::string, std::vector<Account*> > Parse(const char* fpath);
}
}


#endif //BANK_FRONTEND_ACCOUNT_PARSER_H_
