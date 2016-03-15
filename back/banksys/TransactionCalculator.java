package banksys;

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
      
      // add to transaction count 
      if(current_transaction.account_number != 0) {
        Account account = getAccount(current_transaction);
        assert account != null;
        account.transaction_count += 1;
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
  
  private double getTransactionFee(Account account) {
    //todo
    return 0.0;
  }
  
  private boolean accountNumberExists(int number) {
    for(Map.Entry<String, ArrayList<Account>> entry 
        : account_table_.entrySet()) {
      for(Account account : entry.getValue()) {
        if(account.number == number) {
          return true;
        }
      }
    }
    return false;
  }
  
  private void handleWithdrawal(Vector<Transaction> transactions) {
    // Pop off stuff
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Perform withdrawal
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double debit = top.amount + account_fee;
    double new_balance = account.balance - debit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }

  private void handleTransfer(Vector<Transaction> transactions) {
    // Pop off first two
    Transaction sender_transaction = transactions.firstElement();
    transactions.remove(0);
    Transaction recipient_transaction = transactions.firstElement();
    transactions.remove(0);
    
    // Check transaction
    assert sender_transaction.amount == recipient_transaction.amount;
    double amount = sender_transaction.amount;
    
    // Perform transfer
    Account sender = getAccount(sender_transaction);
    Account recipient = getAccount(recipient_transaction);
    double sender_fee = getTransactionFee(sender);
    double debit = sender_fee + amount;
    double sender_balance = sender.balance - debit;
    assert sender_balance >= 0.0;
    sender.balance = sender_balance;
    recipient.balance = recipient.balance + amount;
  }

  private void handlePaybill(Vector<Transaction> transactions) {
    // Pop off first one
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // pay that bill
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double debit = top.amount + account_fee;
    double new_balance = account.balance - debit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }

  private void handleDeposit(Vector<Transaction> transactions) {
    // Pop off the first one
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Do a deposit
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double credit = top.amount - account_fee;
    double new_balance = account.balance + credit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }

  private void handleChangePlan(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Change the plan
    assert top.misc.compareTo(" S") == 0 || top.misc.compareTo(" N") == 0;
    Account account = getAccount(top);
    account.is_student_plan = top.misc.compareTo(" S") == 0 ? true : false;
  }

  private void handleDelete(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Delete the dude
    Account account = getAccount(top);
    assert account != null;
    account_table_.get(top.account_name).remove(account);
  }

  private void handleCreate(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // ensure account number does not already exist
    assert !accountNumberExists(top.account_number);
    
    // todo
  }

  private void handleDisable(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Disable the dude
    Account account = getAccount(top);
    assert account != null;
    assert account.is_active == true;
    account.is_active = false;
  }

  private void handleEnable(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);
    
    // Enable the dude
    Account account = getAccount(top);
    assert account != null;
    assert account.is_active != true;
    account.is_active = true;
  }
  
  /**
  * @brief Index of bank accounts.
  **/
  public Map<String, ArrayList<Account>> account_table_;
}