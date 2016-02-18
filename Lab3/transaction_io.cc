#include "transaction_io.h"

#include <iostream>

namespace BankFrontEnd {
namespace TransactionIO {
// Print To Transaction File
void PrintToTransactionFile(std::deque<std::string>* transactions,
                            std::string fpath) {
  // unwind queue
  std::cout << "Printing to file: " << std::endl; //TODO: remove later
  while (!transactions->empty()) {
    std::string current_transaction = transactions->front();
    std::cout << "\""<< current_transaction << "\" size: "
              << current_transaction.size() << std::endl; // TODO: remove later
    transactions->pop_front();
  }
}
}
}
