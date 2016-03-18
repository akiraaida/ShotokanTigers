package banksys;

import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;

/**
* Acts out operations upon accounts table based on transactions.
*
* <h1>Usage Example:<h1>
* <p>
*   transactionCalculator.setAccountTable(account_table);<br />
*   transactionCalculator.applyTransactions(transactions_list);<br />
* </p>
*
*/
class TransactionCalculator {
  /**
  * Gives known accounts to the calculator for reference.
  * @param account_table Customer names keyed to bank accounts.
  */
  public void setAccountTable(Map<String, ArrayList<Account>> account_table) {
    account_table_ = account_table;
  }

  /**
  * Retrieves account table.
  * @return Database with all previous operations applied to it.
  */
  public Map<String, ArrayList<Account>> getAccountTable() {
    return account_table_;
  }

  /**
  * Applies transaction operations to the bank system.
  * @param transactions Sequence of changes to be made to the bank system.
  *
  * <p>
  *   An error occurs if a transaction involves a nonexistent user, or if a
  *   user's transaction count is negative.
  * <p>
  */
  public void applyTransactions(Vector<Transaction> transactions) {
    // reset values
    is_logged_in_ = false;
    is_admin_ = false;

    // start scanning
    assert account_table_ != null;
    while(!transactions.isEmpty()) {
      Transaction current_transaction = transactions.firstElement();
      switch(current_transaction.code) {
        case TransactionType.login:
          handleLogin(transactions);
          break;

        case TransactionType.logout:
          handleLogout(transactions);
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

        assert account != null;                // TODO when phase 5: report
                                               // error when transaction uses
                                               // invalid user
        assert account.transaction_count >= 0; // TODO when phase 5: report
                                               // error when transaction count
                                               // is negative
        account.transaction_count += 1;
      }

    }
  }

  /**
  * Searches for account in the bank system.
  * @param transaction Code containing account number & owner name
  * @return null, if the account was not found.
  */
  private Account getAccount(Transaction transaction) {
    ArrayList<Account> accounts = account_table_.get(transaction.account_name);
    for(Account account : accounts) {
      if(account.number == transaction.account_number) {
        return account;
      }
    }
    return null;
  }

  /**
  * Looks up the appropriate transaction fee.
  * @param account Account with fees being applied to it
  * @return A fee of zero, if an admin is known to be logged in.
  */
  private double getTransactionFee(Account account) {
    assert is_logged_in_ == true; // TODO when phase 5 is done: print error when
                                  // user is not logged in.
    if(is_admin_) {
      return 0.0;
    } else if (account.is_student_plan) {
      return 0.05;
    } else {
      return 0.1;
    }

  }

  /**
  * Searches for an account with the specified number in the system.
  * @param number 5-digit (i.e. <=99999) account number.
  * @return False, if no accounts with that number are found.
  */
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

  /**
  * Processes the 'login' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the login is consumed from the given stack.
  * </p>
  *
  * <p>
  * An error occurs when the misc does not include 'A ' or 'S '.
  * </p>
  */
  private void handleLogin(Vector<Transaction> transactions) {
    // Pop off stuff
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // do login
    // TODO when phase 5: Print error when misc has bad value
    is_logged_in_ = true;
    assert top.misc.compareTo("A ") == 0 || top.misc.compareTo("S ") == 0;
    is_admin_ = top.misc.compareTo("A ") == 0 ? true : false;
  }
  /**
  * Processes the 'logout' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the logout is consumed from the given stack.
  * </p>
  *
  */
  private void handleLogout(Vector<Transaction> transactions) {
    // Pop off stuff
    Transaction top = transactions.firstElement();
    transactions.remove(0);


    // do login
    is_logged_in_ = false;
    is_admin_ = false;
  }

  /**
  * Processes the 'withdrawal' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the withdrawal is consumed from the given stack.
  * </p>
  *
  * <p>
  * An error occurs if the transaction would cause the balance to go below
  * zero.
  * </p>
  */
  private void handleWithdrawal(Vector<Transaction> transactions) {
    // Pop off stuff
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Perform withdrawal
    // TODO when phase 5: Print error if balance becomes negative
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double debit = top.amount + account_fee;
    double new_balance = account.balance - debit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }
  /**
  * Processes the 'transfer' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the transfer is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the transaction would cause the sender's balance to go
  *   below zero, or if the transfer amounts do not match.
  * </p>
  */
  private void handleTransfer(Vector<Transaction> transactions) {
    // Pop off first two
    Transaction sender_transaction = transactions.firstElement();
    transactions.remove(0);
    Transaction recipient_transaction = transactions.firstElement();
    transactions.remove(0);

    // Check transaction
    // TODO when phase 5: Print error if transfer amount does not match
    assert sender_transaction.amount == recipient_transaction.amount;
    double amount = sender_transaction.amount;

    // Perform transfer
    // TODO when phase 5: Print error if sender balance drops below 0.0
    Account sender = getAccount(sender_transaction);
    Account recipient = getAccount(recipient_transaction);
    double sender_fee = getTransactionFee(sender);
    double debit = sender_fee + amount;
    double sender_balance = sender.balance - debit;
    assert sender_balance >= 0.0;
    sender.balance = sender_balance;
    recipient.balance = recipient.balance + amount;
  }

  /**
  * Processes the 'paybill' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the paybill is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the transaction would cause the balance to go below
  *   zero.
  * </p>
  */
  private void handlePaybill(Vector<Transaction> transactions) {
    // Pop off first one
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // pay that bill
    // TODO when phase 5: Print error if balance drops below 0.0
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double debit = top.amount + account_fee;
    double new_balance = account.balance - debit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }

  /**
  * Processes the 'deposit' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the deposit is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the transaction would cause the balance to go below
  *   zero (e.g. because of transaction fees).
  * </p>
  */
  private void handleDeposit(Vector<Transaction> transactions) {
    // Pop off the first one
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Do a deposit
    // TODO when phase 5: print error if balance drops below 0.0
    Account account = getAccount(top);
    double account_fee = getTransactionFee(account);
    double credit = top.amount - account_fee;
    double new_balance = account.balance + credit;
    assert new_balance >= 0.0;
    account.balance = new_balance;
  }
  /**
  * Processes the 'changeplan' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the changeplan is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the misc column in the transaction does not contain
  *   the flags 'S ' and 'N '
  * </p>
  */
  private void handleChangePlan(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Change the plan
    // TODO when phase 5: print error if misc value is invalid
    assert top.misc.compareTo("S ") == 0 || top.misc.compareTo("N ") == 0;
    Account account = getAccount(top);
    account.is_student_plan = top.misc.compareTo("S ") == 0 ? true : false;
  }

  /**
  * Processes the 'delete' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the delete is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the account is not found.
  * </p>
  */
  private void handleDelete(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Delete the dude
    // TODO when phase 5: print error if account does not exist
    Account account = getAccount(top);
    assert account != null;
    account_table_.get(top.account_name).remove(account);
  }

  /**
  * Processes the 'create' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the create is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the account number already exists.
  * </p>
  */
  private void handleCreate(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // ensure account number does not already exist
    // TODO when phase 5: print error if account number exists already
    assert !accountNumberExists(top.account_number);

    // TODO when phase 5: implement creation
  }

  /**
  * Processes the 'disable' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the disable is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the account is not found, or if it is already disabled.
  * </p>
  */
  private void handleDisable(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Disable the dude
    // TODO when phase 5: print error if account cannot be disabled
    Account account = getAccount(top);
    assert account != null;
    assert account.is_active == true;
    account.is_active = false;
  }

  /**
  * Processes the 'enable' transaction.
  * @param transactions Stack of operations.
  *
  * <p>
  * The entirety of the enable is consumed from the given stack.
  * </p>
  *
  * <p>
  *   An error occurs if the account is not found, or if the account is already
  *   enabled.
  * </p>
  */
  private void handleEnable(Vector<Transaction> transactions) {
    // Pop off the transaction
    Transaction top = transactions.firstElement();
    transactions.remove(0);

    // Enable the dude
    // TODO when phase 5: print error if account cannot be enabled
    Account account = getAccount(top);
    assert account != null;
    assert account.is_active != true;
    account.is_active = true;
  }

  /**
  * Index of bank accounts.
  */
  public Map<String, ArrayList<Account>> account_table_;

  /**
  * When scanning, whether a login or logout occured.
  */
  boolean is_logged_in_;

  /**
  * When scanning, whether logged in user is admin.
  */
  boolean is_admin_;
}
