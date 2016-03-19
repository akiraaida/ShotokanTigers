package banksys;

import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;

/**
* Acts out operations upon accounts table based on transactions.
*
* <h1>Usage Example:<h1>
* <p>
*   transactionCalculator.setAccountTable(accountTable);<br />
*   transactionCalculator.applyTransactions(transactionsList);<br />
* </p>
*
*/
class TransactionCalculator {
  /**
  * Gives known accounts to the calculator for reference.
  * @param accountTable Customer names keyed to bank accounts.
  */
  public void setAccountTable(Map<String, ArrayList<Account>> pAccountTable) {
    accountTable = pAccountTable;
  }

  /**
  * Retrieves account table.
  * @return Database with all previous operations applied to it.
  */
  public Map<String, ArrayList<Account>> getAccountTable() {
    return accountTable;
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
    isLoggedIn = false;
    isAdmin = false;

    // start scanning
    assert accountTable != null;
    while(!transactions.isEmpty()) {
      Transaction currentTransaction = transactions.firstElement();
      switch(currentTransaction.code) {
        case TransactionType.LOGIN:
          handleLogin(transactions);
          break;

        case TransactionType.LOGOUT:
          handleLogout(transactions);
          break;

        case TransactionType.WITHDRAWAL:
          handleWithdrawal(transactions);
          break;

        case TransactionType.TRANSFER:
          handleTransfer(transactions);
          break;

        case TransactionType.PAYBILL:
          handlePaybill(transactions);
          break;

        case TransactionType.DEPOSIT:
          handleDeposit(transactions);
          break;

        case TransactionType.CHANGEPLAN:
          handleChangePlan(transactions);
          break;

        case TransactionType.DELETE:
          handleDelete(transactions);
          break;

        case TransactionType.CREATE:
          handleCreate(transactions);
          break;

        case TransactionType.DISABLE:
          handleDisable(transactions);
          break;

        case TransactionType.ENABLE:
          handleEnable(transactions);
          break;

        default:
          assert false;

      }

      // add to transaction count
      if(currentTransaction.accountNumber != 0) {
        Account account = getAccount(currentTransaction);

        assert account != null;                // TODO when phase 5: report
                                               // error when transaction uses
                                               // invalid user
        assert account.transactionCount >= 0; // TODO when phase 5: report
                                               // error when transaction count
                                               // is negative
        account.transactionCount += 1;
      }

    }
  }

  /**
  * Searches for account in the bank system.
  * @param transaction Code containing account number & owner name
  * @return null, if the account was not found.
  */
  private Account getAccount(Transaction transaction) {
    ArrayList<Account> accounts = accountTable.get(transaction.accountName);
    for(Account account : accounts) {
      if(account.number == transaction.accountNumber) {
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
    assert isLoggedIn == true; // TODO when phase 5 is done: print error when
                                  // user is not logged in.
    if(isAdmin) {
      return 0.0;
    } else if (account.isStudentPlan) {
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
        : accountTable.entrySet()) {
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
    isLoggedIn = true;
    assert top.misc.compareTo("A ") == 0 || top.misc.compareTo("S ") == 0;
    isAdmin = top.misc.compareTo("A ") == 0 ? true : false;
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
    isLoggedIn = false;
    isAdmin = false;
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
    double accountFee = getTransactionFee(account);
    double debit = top.amount + accountFee;
    double newBalance = account.balance - debit;
    assert newBalance >= 0.0;
    account.balance = newBalance;
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
    Transaction senderTransaction = transactions.firstElement();
    transactions.remove(0);
    Transaction recipientTransaction = transactions.firstElement();
    transactions.remove(0);

    // Check transaction
    // TODO when phase 5: Print error if transfer amount does not match
    assert senderTransaction.amount == recipientTransaction.amount;
    double amount = senderTransaction.amount;

    // Perform transfer
    // TODO when phase 5: Print error if sender balance drops below 0.0
    Account sender = getAccount(senderTransaction);
    Account recipient = getAccount(recipientTransaction);
    double senderFee = getTransactionFee(sender);
    double debit = senderFee + amount;
    double senderBalance = sender.balance - debit;
    assert senderBalance >= 0.0;
    sender.balance = senderBalance;
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
    double accountFee = getTransactionFee(account);
    double debit = top.amount + accountFee;
    double newBalance = account.balance - debit;
    assert newBalance >= 0.0;
    account.balance = newBalance;
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
    double accountFee = getTransactionFee(account);
    double credit = top.amount - accountFee;
    double newBalance = account.balance + credit;
    assert newBalance >= 0.0;
    account.balance = newBalance;
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
    account.isStudentPlan = top.misc.compareTo("S ") == 0 ? true : false;
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
    accountTable.get(top.accountName).remove(account);
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
    assert !accountNumberExists(top.accountNumber);

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
    assert account.isActive == true;
    account.isActive = false;
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
    assert account.isActive != true;
    account.isActive = true;
  }

  /**
  * Index of bank accounts.
  */
  public Map<String, ArrayList<Account>> accountTable;

  /**
  * When scanning, whether a login or logout occured.
  */
  boolean isLoggedIn;

  /**
  * When scanning, whether logged in user is admin.
  */
  boolean isAdmin;
}
