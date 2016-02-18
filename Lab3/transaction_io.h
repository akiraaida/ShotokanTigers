#ifndef BANK_FRONTEND_TRANSACTION_IO_H_
#define BANK_FRONTEND_TRANSACTION_IO_H_
/******************************************************************************
* transaction_io.h
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Functions enabling the use of the transactions file.
*
*******************************************************************************/

#include <deque>
#include <string>

#define FPATH_TRANSACTIONS_FILE "transactions.txt"


namespace BankFrontEnd {
namespace TransactionIO {
/**
* Consumes deque; appends to transactions file.
**/
void PrintToTransactionFile(std::deque<std::string>* transactions,
                            std::string fpath = FPATH_TRANSACTIONS_FILE);


}
}

#endif //BANK_FRONTEND_TRANSACTION_IO_H_
