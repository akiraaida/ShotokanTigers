/******************************************************************************
* transaction_io.cc
* CSCI 3060u/SOFE 3980u: Course Project Front End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*******************************************************************************/
#include "transaction_io.h"

#include <fstream>
#include <iostream>

namespace BankFrontEnd {
namespace TransactionIO {
// Print To Transaction File
void PrintToTransactionFile(std::deque<std::string>* transactions,
                            std::string fpath) {
  // get ofstream
  std::ofstream file;
  file.open(fpath, std::ios_base::app);

  // unwind queue
  std::cout << "Printing to file: " << std::endl; //TODO: remove later
  while (!transactions->empty()) {
    std::string current_transaction = transactions->front();
    file << current_transaction << std::endl;
    std::cout << "\""<< current_transaction << "\" size: "
              << current_transaction.size() << std::endl; // TODO: remove later
    transactions->pop_front();
  }
}
} //namespace TransactionIO
} //namespace BankFrontEnd
