package backend;

import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;

/**
* @brief Acts out operations upon accounts table based on transactions.
**/
class TransactionCalculator {
  /**
  * @brief Gives known accounts to the calculator for reference.
  * @param account_table Customer names keyed to bank accounts.
  **/
  public void setAccountTable(Map<String, ArrayList<Account>> account_table) {
    account_table_ = account_table;
  }
  
  /**
  * @brief Retrieves account table.
  * @return Database with all previous operations applied to it.
  **/
  public Map<String, ArrayList<Account>> getAccountTable() {
    return account_table_;
  }
  
  /**
  * @brief Applies transactions to the bank system.
  **/
  public void applyTransactions(Vector<Transaction> transactions) {
    assert account_table_ != null;
    while(!transactions.isEmpty()) {
      Transaction current_transaction = transactions.firstElement();
      switch(current_transaction.code) {
        case TransactionType.login:
        case TransactionType.logout:
          // nothing to do
          transactions.remove(0);
          break;
          
        case TransactionType.withdrawal:
          handleWithdrawal(transactions);
          break;
          
        case TransactionType.transfer:
          handleTransfer(transactions);
          break;
          
        case TransactionType.paybill:
          handlePaybill(transactions);
          break;
          
        case TransactionType.deposit:
          handleDeposit(transactions);
          break;
          
        case TransactionType.changeplan:
          handleChangePlan(transactions);
          break;
          
        case TransactionType.delete:
          handleDelete(transactions);
          break;
        
        case TransactionType.create:
          handleCreate(transactions);
          break;
          
        case TransactionType.disable:
          handleDisable(transactions);
          break;
          
        case TransactionType.enable:
          handleEnable(transactions);
          break;
          
        default:
          assert false;
        
      }
      
    }
  }
  
  private Account getAccount(Transaction transaction) {
    ArrayList<Account> accounts = account_table_.get(transaction.account_name);
    for(Account account : accounts) {
      if(account.number == transaction.account_number) {
        return account;
      }
    }
    return null;
  }
  
  private void handleWithdrawal(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    Account account = getAccount(top);
    double new_balance = account.balance - top.amount;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }

  private void handleTransfer(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handlePaybill(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleDeposit(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleChangePlan(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleDelete(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleCreate(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleDisable(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }

  private void handleEnable(Vector<Transaction> transactions) {
    //todo
    Transaction top = transactions.firstElement();
    transactions.remove(0);
  }
  
  /**
  * @brief Index of bank accounts.
  **/
  public Map<String, ArrayList<Account>> account_table_;
}