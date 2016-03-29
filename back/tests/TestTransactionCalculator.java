/*
* TestTransactionCalculator.java
* CSCI 3060U/SOFE 3980U: Course Project Back End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*
* Adapted console output test code from:
*  http://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
*/

package tests;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Vector;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.After;
import org.junit.Before;
import banksys.TransactionCalculator;
import banksys.TransactionType;
import banksys.Account;
import banksys.Transaction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class TestTransactionCalculator {
  private static final String USERNAME_JOHN_DOE = "John Doe";
  private static final String USERNAME_MATT_COW = "Matt Cow";
  private static final String USERNAME_NEW_GUY = "New Guy";
  private static final String USERNAME_MOTH_WALLET = "Moth Wallet";
  private static final String[] USERNAME_OTHER_GUYS = {"Guy 1", "Guy 2", "Guy 3"};
  private static final Integer[] ACCOUNTS_JOHN_DOE = {12345, 12346, 12347, 12360, 12361};
  private static final Integer[] ACCOUNTS_MATT_COW = {12348, 12349};
  private static final Integer[] ACCOUNTS_NEW_GUY = {12350, 12351};
  private static final Integer[] ACCOUNTS_OTHER_GUYS = {12370, 12371, 12372};
  private static final Integer MOTH_WALLET_ACCOUNT = 12380;
  private static double DEFAULT_ACCOUNT_BALANCE = 100.0;
  private static double DEFAULT_DEBIT_AMOUNT = 5.00;
  private static double WEIRD_WITHDRAWAL_AMOUNT = 1.00;
  private static Integer DEFAULT_TRANSACTION_COUNT = 0;
  private static Integer DISABLED_ACCOUNT_NUMBER = 12346;
  private static Integer STUDENT_PLAN_ACCOUNT_NUMBER = 12347;
  private static Integer BAD_TRANSACTION_ACCOUNT_NUMBER = 12360;
  private static Integer HUGE_TRANSACTION_ACCOUNT_NUMBER = 12361;
  private static int NEGATIVE_TRANSACTION_VALUE = -1;
  private static int MAX_TRANSACTION_VALUE = 9999;
  private static double MOTH_WALLET_BALANCE = 0.04;

  /**
   * Holds on to the referance to stdout printstream
   */
  private PrintStream oldConsoleOut;
  
  /**
   * Storage of stdout data
   */
  private final ByteArrayOutputStream consoleOutContent = new ByteArrayOutputStream();
  
  /**
   * Number to test console out scheme
   */
  private final Integer numberForConsoleOutTest = 0;
  
  /**
   * Test objects
   */
  private final TransactionCalculator transactionCalculator = new TransactionCalculator();
  
  /**
   * Stack of commands to test
   */
  private final Vector<Transaction> transactionList = new Vector<Transaction>();
  
  /**
   * Initializes database.
   */
  @Before
  public void setupCalculator() {
    // Initialize customer data 1
    ArrayList<Account> john_does_stuff = new ArrayList<Account>();
    for(int accountNumber : ACCOUNTS_JOHN_DOE) {
      john_does_stuff.add(genAccount(accountNumber));
    }
    
    // Initialize customer data 2 
    ArrayList<Account> matt_cows_stuff = new ArrayList<Account>();
    for(int accountNumber : ACCOUNTS_MATT_COW) {
      matt_cows_stuff.add(genAccount(accountNumber));
    }
    
    // Initialize moth wallet
    ArrayList<Account> moth_wallet_stuff = new ArrayList<Account>();
    moth_wallet_stuff.add(genAccount(MOTH_WALLET_ACCOUNT));
    
    // Assemble account table 
    Map<String, ArrayList<Account>> accountTable = new HashMap<String, ArrayList<Account>>();
    accountTable.put(USERNAME_JOHN_DOE, john_does_stuff);
    accountTable.put(USERNAME_MATT_COW, matt_cows_stuff);
    accountTable.put(USERNAME_MOTH_WALLET, moth_wallet_stuff);
    
    // Add in extra guys
    for(int i = 0; i < USERNAME_OTHER_GUYS.length; i++) {
      ArrayList<Account> this_guys_stuff = new ArrayList<Account>();
      this_guys_stuff.add(genAccount(ACCOUNTS_OTHER_GUYS[i]));
      accountTable.put(USERNAME_OTHER_GUYS[i], this_guys_stuff);
    }
    
    // Finarry
    transactionCalculator.setAccountTable(accountTable);
  }
  
  /**
   * Swaps out old stdout destination to storage
   */
  @Before
  public void setupStreams() {
    oldConsoleOut = System.out;
    System.setOut(new PrintStream(consoleOutContent));
  }
  
  /**
   * Restores previous state of stdout
   */
  @After
  public void cleanupStreams() {
    System.setOut(oldConsoleOut);
  }
  
  /**
   * Routine to report unexpected exceptions e.g. null pointer, etc.
   */
  public void failUnexpectedException(Exception e) {
    e.printStackTrace(System.err);
    fail(String.format("Exception was thrown:%n%s%ncause: %s", e.toString(), e.getCause()));
  }
  
  /**
   * Based on the number, setup account parameters.
   */
  private Account genAccount(int accountNumber) {
    Account account = new Account();
    account.number = accountNumber;
    account.isActive = accountNumber != DISABLED_ACCOUNT_NUMBER;
    account.balance = accountNumber == MOTH_WALLET_ACCOUNT ? MOTH_WALLET_BALANCE
                                                      : DEFAULT_ACCOUNT_BALANCE;
    account.transactionCount = accountNumber == BAD_TRANSACTION_ACCOUNT_NUMBER ?
                                                NEGATIVE_TRANSACTION_VALUE
                                                : DEFAULT_TRANSACTION_COUNT;
    account.transactionCount = accountNumber == HUGE_TRANSACTION_ACCOUNT_NUMBER ?
                                                MAX_TRANSACTION_VALUE
                                                : DEFAULT_TRANSACTION_COUNT;
    account.isStudentPlan = accountNumber == STUDENT_PLAN_ACCOUNT_NUMBER;
    return account;
  }
  
  /**
   * Transaction constructor stub(?)
   */
  private Transaction makeTransaction(int code, String accountName,
                                      int accountNumber, double amount,
                                      String misc) {
    Transaction transaction = new Transaction();
    transaction.code = code;
    transaction.accountName = accountName;
    transaction.accountNumber = accountNumber;
    transaction.amount = amount;
    transaction.misc = misc;
    return transaction;
  }
  
  /**
   * Initialize transaction with nothing but the code.
   */
  private Transaction makeTransaction(int code) {
    return makeTransaction(code, "", 0, 0.0, "");
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'applyTransactions'
   *
   * <h1>Coverage:</h1><ul><li>
   *   Covers statements:<ul><li>
   *     (Everything up to "start scanning" will always execute)</li><li>
   *     (Statements in switch cases are equivalent to decision coverage points,
   *      are listed there)</li><li>
   *     (In the transaction count block, the statement checking for null
   *      accounts should ALWAYS pass, it is only there for debugging purposes.
   *      Therefore, it will not be included in statement coverage.)</li><li>
   *     S1 Begin iterating through transactions</li><li>
   *     S2 Get current transaction </li><li>
   *     S3 Add to transaction count</li><li>
   *     S4 Transaction count check</li><li>
   *     S5 Growth of transaction count</li></ul></li><li>
   *   Covers decisions:<ul><li>
   *     D1           Response to null accountTable</li><li>
   *     DLogin       Handling of 'login' transaction</li><li>
   *     DLogout      Handling of 'logout' transaction</li><li>
   *     DWithdrawal  Handling of 'withdrawal' transaction</li><li>
   *     DTransfer    Handling of 'transfer' transaction</li><li>
   *     DPaybill     Handling of 'paybill' transaction</li><li>
   *     DDeposit     Handling of 'deposit' transaction</li><li>
   *     DChangeplan  Handling of 'changeplan' transaction</li><li>
   *     DDelete      Handling of 'delete' transaction</li><li>
   *     DCreate      Handling of 'create' transaction</li><li>
   *     DDisable     Handling of 'disable' transaction</li><li>
   *     DEnable      Handling of 'enable' transaction</li><li>
   *     DDefault     Response to unknown transaction code</li><li>
   *     D2           Incrementation of the transaction count</li><li>
   *     D3           Response to invalid existing transaction count.</li></ul></li><li>
   *   Covers Loops:<ul><li>
   *     L1-0 No transactions are processed.</li><li>
   *     L1-1 One transaction is processed.</li><li>
   *     L1-2 Two transactions are processed.</li><li>
   *     L1-3 Login, something, Logout with 3 transactions.</li><li>
   *     L1-4 Login, something x2, Logout with 4 transactions</li><li>
   *     L1-* Many transactions are processed in between login/logout.</li></ul></li></ul>
   *
   * <p> The procedure for this test is to feed applyTransactions certain
   *  sequences of transactions, so that we can be certain that each of the
   *  above statements, decision blocks, and loop cases are covered. To
   *  determine with confidence whether the expected lines were covered, it is
   *  nessecary to define expectations for the presence and content of error
   *  messages, whether successful transactions consumed the transactions stack
   *  or not, and finally whether the state of the account table has changed. 
   *  Each test uses some combination of these to narrow down the possible
   *  statements that were hit.</p>
   * 
   * <h1>Tests</h1><ul><li>
   *  <b>Test name, transaction sequence, error message, sequence afterwards,
   *       cases covered, other input</b></li><li>
   *     T1, [Empty], no error, L1-0 & S1</li><li>
   *     T2, [Login as admin], invalid transaction, L1-1 & S2</li><li>
   *     T3, [Login as admin, Logout], no error, L1-2</li><li>
   *     T4, [Login as admin, withdraw from 12345, Logout], no error,
   *          L1-3 & S3 & S5 & D2
   *          transaction count would
   *          become 1</li><li>
   *     T5, [Login as admin, withdraw from 12345, withdraw again, Logout],
   *          no error, L1-4, transaction count would become 2</li><li>
   *     T6, [Login as admin, withdraw from 12345, withdraw again, withdraw
   *          again, Logout], no error, L1-5, transaction count would become 3
   *          </li><li>
   *     T7, [Login as admin, withdraw from the invalid transaction count
   *          account], Invalid transaction count error, S4</li><li>
   *     T8, [Empty], Null account table error, D1</li><li>
   *     T9, [Login as admin, Login as admin], Can't login twice error,
   *          DLogin</li><li>
   *     T10, [Logout], Can't logout twice error, DLogout</li><li>
   *     T11, [Login as admin, Withdraw non-canadian amount from 12345],
   *          Invalid withdrawal amount error, DWithdrawal</li><li>
   *     T12, [Login as admin, Transfer from John Doe to Matt Cow with different
               amounts],
   *          Mismatched transfer amounts error, DTransfer</li><li>
   *     T13, [Login as admin, Pay bill on 12345 to TV, Logout], no error, 
   *          DPaybill, 12345 has reduced balance</li><li>
   *     T14, [Login as admin, Deposit to 12345, Logout], no error,
   *          DDeposit, amount of money is raised in 12345
   *          </li><li>
   *     T15, [Login as admin, Change plan of 12345, Logout], no error,
   *          DChangeplan, plan flag is changed</li><li>
   *     T16, [Login as admin, Delete 12345, Logout], no error, DChangeplan,
   *          12346 becomes first account of John Doe</li><li>
   *     T17, [Login as admin, Create New Guy, Logout, Login as New Guy, Logout]
   *          no error, DCreate</li><li>
   *     T18, [Login as admin, Disable 12345, Logout], no error,
   *          DDisable, 12345 now has isActive == false</li><li>
   *     T19, [Login as admin, Enable Disabled Account, Logout], no error,
   *          DEnable, Disabled account now has isActive == true</li><li>
   *     T20, [Login as admin, use nonexistent code (-1)], Invalid code error,
   *          DDefault</li><li>
   *     T21, [Login as admin, withdraw from big transaction code guy], overflow
   *          transaction code error, N/A</li></ul>
   */
   /*
   * TODO: 
   * Make skeleton of function so this one will be as neat as the getAccount one
   */
  @Test
  public void checkApplyTransactions() {
    try { 
      // T1 (no loop)
      doApplyTransactionsTest(transactionList, "");
      
      // T2 (loop once)
      transactionList.clear();
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(new Vector<Transaction>(transactionList.subList(1,
                                                  transactionList.size() - 1)),
                      TransactionCalculator.ERROR_INVALID_TRANSACTION_SEQUENCE);
      
      // T3 (in & out)
      transactionList.clear();
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      
      // T4 (1 withdrawal)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 1, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
      
      // T5 (2 withdraw)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 2, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
                          
      
      // T6 (3 withdraw)
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
                          DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected transaction count to change", 3, 
        getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).transactionCount);
        
      // T7 test for invalid (negative) transaction count
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
            USERNAME_JOHN_DOE, BAD_TRANSACTION_ACCOUNT_NUMBER,
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_NEGATIVE_TRANSACTION_COUNT);
      
      // Skip to T9 for now (instead of T8, save that one for last)
      // Confirm login reached
      transactionList.clear();
      logSandwich("admin", transactionList);
      transactionList.add(0, transactionList.firstElement());
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_LOGIN_TWICE);
            
      // T10 confirm logout reached
      transactionList.clear();
      logSandwich("admin", transactionList);
      transactionList.add(0, transactionList.lastElement());
      doApplyTransactionsTest(transactionList,
            TransactionCalculator.ERROR_LOGOUT_TWICE);
            
      // T11 confirm withdrawal reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            WEIRD_WITHDRAWAL_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_NONCANADIAN_WITHDRAWAL_AMOUNT);
      
      // T12 confirm transfer reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.TRANSFER, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            WEIRD_WITHDRAWAL_AMOUNT, ""));
      transactionList.add(makeTransaction(TransactionType.TRANSFER, 
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0],
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_MISMATCHED_TRANSFER_AMOUNT);
        
      // T13 confirm paybill
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.PAYBILL,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            DEFAULT_DEBIT_AMOUNT, "TV"));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected balance to change", DEFAULT_DEBIT_AMOUNT, 
        DEFAULT_ACCOUNT_BALANCE
        - getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).balance, 0.01);
      
      // T14 confirm deposit
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.DEPOSIT,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected balance to change", -DEFAULT_DEBIT_AMOUNT, 
        DEFAULT_ACCOUNT_BALANCE
        - getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).balance, 0.01);
      
      // T15  check that change plan is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.CHANGEPLAN,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            0.0, "S "));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected plan to change", true, 
            getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).isStudentPlan);
            
      // T17 (skip T16, do second last before T8)
      // ensure creation is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.CREATE,
            USERNAME_NEW_GUY, ACCOUNTS_NEW_GUY[0], 
            DEFAULT_ACCOUNT_BALANCE, "N "));
      logSandwich("admin", transactionList);
      transactionList.add(makeTransaction(TransactionType.LOGIN,
            USERNAME_NEW_GUY, 0, 0.0, "S "));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected creation stack to complete in full", true, 
                    transactionList.isEmpty());
      
      // T18 ensure coverage of disable
      transactionList.add(makeTransaction(TransactionType.DISABLE,
                          USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected account to get disabled", false, 
                  getAccount(USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0]).isActive);
                  
      // T19 Ensure coverage of enable
      transactionList.add(makeTransaction(TransactionType.ENABLE,
                          USERNAME_JOHN_DOE, DISABLED_ACCOUNT_NUMBER, 0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected account to get enabled", true, 
                  getAccount(USERNAME_JOHN_DOE,
                              DISABLED_ACCOUNT_NUMBER).isActive);
      
      // T20 Ensure coverage of invalid transaction code errors
      transactionList.clear();
      transactionList.add(makeTransaction(-1));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
          TransactionCalculator.ERROR_INVALID_TRANSACTION_CODE);
      
      // T21 Ensure coverage of transaction count overflow
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, 
        USERNAME_JOHN_DOE, HUGE_TRANSACTION_ACCOUNT_NUMBER,
        DEFAULT_DEBIT_AMOUNT, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList,
        TransactionCalculator.ERROR_OVERFLOW_TRANSACTION_COUNT);
      
      // Do T16: ensure deletion is reached
      transactionList.clear();
      transactionList.add(makeTransaction(TransactionType.DELETE,
            USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 
            0.0, ""));
      logSandwich("admin", transactionList);
      doApplyTransactionsTest(transactionList, "");
      assertEquals("Expected removal of first account", ACCOUNTS_JOHN_DOE[1], 
            transactionCalculator.getAccountTable().get(USERNAME_JOHN_DOE).get(0));
        
      // Do T8: test with null account table
      transactionCalculator.setAccountTable(null);
      doApplyTransactionsTest(new Vector<Transaction>(), 
                            TransactionCalculator.ERROR_UNSET_ACCOUNT_TABLE);
      
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'getAccount'
   *
   * <h1>Coverage:</h1><ul><li>
   *   Covers statements:<ul><li>
   *     (Assignment and for check always occurs for any input)</li><li>
   *     S1 Account number check (if statement)</li><li>
   *     S2 Successful return of account</li><li>
   *     S3 Unsuccessful return of null</li></ul></li><li>
   *   Covers decisions:<ul><li>
   *     D1 Block executed when account number checked DOES match the input
   *          transaction's account number</li></ul></li><li>
   *   Covers Loops:<ul><li>
   *     L1-1 Examines one account</li><li>
   *     L1-2 Examines two accounts</li><li>
   *     L1-* Examines many accounts</li></ul></li></ul><p>
   *
   * As the bank system is not expected to have customer names with zero 
   * accounts, there is no coverage for the L1 loop to execute zero times.
   * </p><p>
   *
   * The experiment will be to apply a typical withdrawal transaction on one of
   * John Doe's accounts. The independent variable is whether to use the first,
   * second, third, or some nonexistent account. </p><p>
   *
   * Whether the account CAN be found will influence whether D1 is executed, and
   * consequentally whether the S3 return statement can be reached.
   * Additionally, since the L1 for loop will scan the customer's accounts in
   * order, the account number will determine how many times the loop will 
   * execute. </p></p>
   *
   * The outputs tested are the absence or presence of error messages, as well
   * as whether the account was modified (determined by checking whether the
   * transaction count of the current account is 0 or 1).</p><ul><li>
   *  The D1 block is known to be executed IFF no error messages have occurred.
   *    </li><li>
   *  The loop is only known to have checked all accounts up to the input one
   *   IFF it is shown that a transaction has been applied to it.</li></ul><p>
   *
   * The tests are as follows:</p><table><tr><th>
   *    Name</th><th> Account</th><th>        Error?</th><th> Coverage</th></tr><td>
   *    T1</td><td>   first</td><td>          no</td><td>     S1 S2 D1 L1-1</td></tr><td>
   *    T2</td><td>   invalid(00000)</td><td> yes</td><td>    S3 L1-*</td></tr><td>
   *    T3</td><td>   second</td><td>         no</td><td>     L1-2</td></tr></table>
   */
  @Test
  public void checkGetAccount() {
    try {
      doCheckGetAccountTest(0, false); //T1
      doCheckGetAccountTest(null, true); //T2
      doCheckGetAccountTest(1, false); //T3
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'getTransactionFee'
   * 
   * <ul><li>
   * S1 Evaluate login status</li><li>
   * S2 Check if it is student plan</li><li>
   * S3 Otherwise</li><li>
   * D1 No charge for admin</li><li>
   * D2 Student plan charge</li><li>
   * D3 Nonstudent charge</li></ul>
   *
   * <p>Experiment is to log in with different privleges and to withdraw from
   *    different accounts. Depending on the login mode and account plan, the
   *    amount the balance drops will be different because of the return value
   *    of this function.</p>
   * 
   * <ul><li>
   * T1 admin login, account 12345, expect no charge (coverage of S1, D1)
   * T2 John Doe login, account 12345, expect 10 cent charge (coverage of S2, D2)
   * T3 John Doe login, Student plan account, expect 5 cent charge (coverage of
   *    S3, D3)</li></ul>
   */
  @Test
  public void checkGetTransactionFee() {
    try { //TODO impelment tests, create function
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'accountNumberExists'
   *
   * <ul><li>S1 iterate over user's accounts</li><li>
   *         S2 check if current matches target</li><li>
   *         S3 return true</li><li>
   *         S4 return false</li><li>
   *         (Decision block D1 would be equal to S3)</li><li>
   *         L1-0 iterate over no K,V</li><li>
   *         L1-1 iterate K,V run once</li><li>
   *         L1-2 iterate K,V run twice</li><li>
   *         L1-* iterate K,V run many times</li><li>
   *         L2-1 first hit is account</li><li>
   *         L2-2 find account on second hit</li><li>
   *         L2-* find account after many hits</li></ul>
   *
   * <p>Since it shouldn't be valid for a name to exist in the system with no
   * accounts, a case where the L2 loop runs once is not considered.</p>
   *
   * <p>To gain coverage of these nodes, the create transaction should be tried
   * with the account number as a parameter. As in checkGetAccount, the order
   * that the accounts will be checked is known, which will give us knowledge of
   * the number of time the loop runs. The other parameter is the account map,
   * which will be empty for some tests and filled as usual in others.</p>
   * 
   * <p>To assess whether or not the function returned true, the presence of an
   * error output to console will be checked.</p>
   * 
   * <ul><li>T1, empty accountTable, target of 12345, expect no error.
   *          (cover L1-0, S4)</li><li>
   *         T2, filled accountTable, target of 12345, expect error.
   *          (cover L1-1, L2-1, S1, S2, S3)</li><li>
   *         T3, filled accountTable, John Doe's second account, expect error.
   *          (cover L2-2)</li><li>
   *         T4, filled accountTable, John Doe's third account, expect error.
   *          (cover L2-3)</li><li>
   *         T5, filled accountTable, Matt Cow's first account, expect error.
   *          (cover L1-2)</li><li>
   *         T6, filled accountTable, nonexistent account(0), expect no error,
   *          (cover L1-*)</li></ul>
   */
  @Test
  public void checkAccountNumberExists() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleLogin'
   *
   * <ul><li>S1 pop off top</li><li>
   *         S2 make the thing logged in</li><li>
   *         S3 assert that the flag is real</li><li>
   *         S4 determine if the privlege is admin</li><li>
   *         D1 React to bad flag</li><li>
   *         D2 set admin to true</li><li>
   *         D3 set admin to false</li></ul>
   *
   * <p>Our levers this time are the login name and flag used.</p>
   *
   * <ul><li>T1 use "admin" name, "A " flag, expect no error.
   *          (Covers S1, S2, S3, S4, D2)</li><li>
   *         T2 use "John Doe" name, "S " flag, expect no error.
   *          (Covers D3)</li><li>
   *         T3 use "John Doe" name, "" flag, expect error.
   *          (Covers D1)</li></ul>
   */
  @Test
  public void checkHandleLogin() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleLogout'
   * 
   * <ul><li>S1 pop off top</li><li>
   *         S2 set isLoggedIn</li><li>
   *         S3 set isAdmin</li></ul>
   *
   * <p>Only test needed to cover these statements is to push the logout code
   *    transaction. Isn't that Neat?</p>
   */
  @Test
  public void checkHandleLogout() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleWithdrawal'
   *
   * <ul><li>S1 pop off top</li><li>
   *         S2 retrieve account.</li><li>
   *         S3 retrieve account fee.</li><li>
   *         S4 calculate charge.</li><li>
   *         S5 calculate new balance.</li><li>
   *         S6 check if balance is negative.</li><li>
   *         S7 set new account balance.</li><li>
   *         D1 respond to negative balance.</li></ul>
   *
   * <p>Main lever here is the debited amount.</p>
   *
   * <ul><li>T1 withdraw 5 dollars, expect success (covers S1-S7)</li><li>
   *         T2 withdraw 105 dollars, expect message about negative balance</li></ul>
   *
   */
  @Test
  public void checkHandleWithdrawal() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleTransfer'
   * 
   * <ul><li>S1 Remove sender transaction</li><li>
   *         S2 Get recipient transaction</li><li>
   *         S3 Remove recipient transaction</li><li>
   *         S4 Check if amounts match</li><li>
   *         S5 Set amount</li><li>
   *         S6 Get sender account</li><li>
   *         S7 Get recipient account</li><li>
   *         S8 Get sender fee</li><li>
   *         S9-S11 Check sender balance</li><li>
   *         S12-S13 Update balances for sender and reciever</li><li>
   *         D1 Respond to mismatched amounts</li><li>
   *         D2 Respond to negative balacne</li></ul>
   *
   * <p>The parameters for this test are the amounts sent and recievied. The
   * output is the error message, or lackthereof.</p>
   *
   * <ul><li>T1 send & recieve 5.00, expect success
   *          (covers S1-S13)</li><li>
   *         T2 send 5.00, receive 105.00. Expect mismatched amounts error.
   *          (covers D1)</li><li>
   *         T3 send & recieve 105.00, expect negative balance error
   *          (covers D2)</li></ul>
   */
  @Test
  public void checkHandleTransfer() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handlePaybill'
   * 
   * <ul><li>S1 pop off top transaction</li><li>
   *         S2 retrieve account.</li><li>
   *         S3-S6 check new balance.</li><li>
   *         S7 update account balance.</li>
   *         D1 Respond to negative balance.</ul>
   *
   * <p>The parameter here is amount, as usual.</p>
   * 
   * <ul><li>T1 pay 5.00, expect success</li><li>
   *         T2 pay 105.00, expect negative balance error.</li></ul>
   */
  @Test
  public void checkHandlePaybill() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDeposit'
   * 
   * <ul><li>S1 pop off top.</li><li>
   *         S2 retrieve account.</li><li>
   *         S3-S6 check resulting balance.</li><li>
   *         S7 update balance.</li></ul>
   *
   * <p>Deposit 5 cents in the moth wallet account as an admin or standard.</p>
   *
   * <ul><li>T1 deposit as admin, expect success.</li><li>
   *         T2 desposit as standard, expect failure.</li></ul>
   * 
   */
  @Test
  public void checkHandleDeposit() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleChangePlan'
   *
   * <ul><li>S1 pop off top</li><li>
   *         S2 check if flag is valid.</li><li>
   *         S3 retrieve account.</li><li>
   *         S4 determine plan.</li><li>
   *         D1 respond to bad flag.</li><li>
   *         D2 set student plan to true.</li><li>
   *         D3 set student plan to false.</li></ul>
   * 
   * <p>The account and flag are the parameters here. Outputs are error message
   *    and resulting account flag.</p>
   *
   * <ul><li>T1 Nonstudent account, Student flag, expect success & student plan.
   *          (covers S1, S2, S3, S4, D3)</li><li>
   *         T2 Nonstudent account, flag of "", expect failure.
   *          (covers D1)</li><li>
   *         T3 Student account, Nonstudent flag, expect success & Nonstudent
   *            plan. (covers D3)</li></ul>
   */
  @Test
  public void checkHandleChangePlan() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDelete'
   *
   * <ul><li>S1 Pop off top</li><li>
   *         S2 Retrieve account.</li><li>
   *         S3 Check if account is already gone.</li><li>
   *         S4 Remove account from table</li><li>
   *         D1 Respond to broken account</li></ul>
   *
   * <p>The test parameter is the account number.</p>
   *
   * <ul><li>T1 delete 12345, expect success (covers S1-S4)</li><li>
   *         T2 delete new guy, expect failure. (covers D1)</li></ul>
   * 
   */
  @Test
  public void checkHandleDelete() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleCreate'
   *
   * <ul><li>S1 pop off transaction</li><li>
   *         S2 check if account number exists</li><li>
   *         D1 respond to already existing account</li></ul>
   *
   * <p>Test parameter is the account number.</p>
   *
   * <ul><li>T1 create 12345, expect failure</li><li>
   *         T2 create new guy, expect success</li></ul>
   */
  @Test
  public void checkHandleCreate() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleDisable'
   *
   * <ul><li>S1 pop off top</li><li>
   *         S2 retrieve account</li><li>
   *         S3-S4 check if account is valid</li><li>
   *         S5 set account as disabled</li><li>
   *         D1 respond to nonexistent account</li><li>
   *         D2 respond to already disabled account </li></ul>
   *
   * <p>Test parameter is the account number again.</p>
   *
   * <ul><li>T1 disable 12345, no error. (Covers S1-S5)</li><li>
   *         T2 disable New guy, deleted account error (Covers D1)</li><li>
   *         T3 disable disabled account, already disabled account error
   *          (Covers D2)</li></ul>
   */
  @Test
  public void checkHandleDisable() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  
  /**
   * Ensures statement, decision, & loop coverage of 'handleEnable'
   *
   * <ul><li>S1 pop off top</li><li>
   *         S2 retrieve account</li><li>
   *         S3-S4 check if account is valid</li><li>
   *         S5 set account as enabled</li><li>
   *         D1 respond to nonexistent account</li><li>
   *         D2 respond to already enabled account </li></ul>
   *
   * <p>Test parameter is the account number again.</p>
   *
   * <ul><li>T1 enable disabled account, no error. (Covers S1-S5)</li><li>
   *         T2 enable New guy, deleted account error (Covers D1)</li><li>
   *         T3 enable 12345, already active account error
   *          (Covers D2)</li></ul>
   */
  @Test
  public void checkHandleEnable() {
    try { //TODO
      fail("not implemented");
    } catch (Exception e) {
      failUnexpectedException(e);
    }
  }
  
  /**
   * Confirms that the stdout storage used in other tests works as expected.
   */
  @Test
  public void testNothing() {
    assertNotNull(System.out);
    assertNotNull(numberForConsoleOutTest);
    System.out.println(numberForConsoleOutTest);
    assertEquals("Failed to read stdout properly", String.format("%s%n", numberForConsoleOutTest.toString()), consoleOutContent.toString());
    System.out.println(numberForConsoleOutTest);
    assertEquals("Failed to read stdout in entirety", String.format("%s%n%s%n", numberForConsoleOutTest.toString(), numberForConsoleOutTest.toString()), consoleOutContent.toString());
  }
  
  /**
   * Confirms, along with testNothing, that the storage is reset as expected for
   * each test.
   */
  @Test
  public void testNothingEvenStill() {
    assertNotNull(System.out);
    assertNotNull(numberForConsoleOutTest);
    System.out.print(numberForConsoleOutTest);
    assertEquals("Failed to read stdout once", String.format("%s", numberForConsoleOutTest.toString()), consoleOutContent.toString());
  }
  
  /**
  * Extracts one of the named customer's accounts.
  * @param name Customer's name
  * @param whichOne 0 indicates first, 1 indicates second, etc.
  */
  private Account getAccount(String name, int whichOne) {
    Map<String, ArrayList<Account>> accountTable = transactionCalculator.getAccountTable();
    return accountTable.get(name).get(whichOne);
  }
  
  /**
  * Surrounds transactions with login/logout
  * it both alters the transactions param and returns the pointer.
  */
  private Vector<Transaction> logSandwich(String name, Vector<Transaction> transactions){
    transactions.add(0, makeTransaction(TransactionType.LOGIN, name, 0, 0.0, 
                     name.compareTo("admin") == 0 ? "A " : "S "));
    transactions.add(makeTransaction(TransactionType.LOGOUT));
    return transactions;
  }
  
  /**
  * Resets data, (unless accountTable is null) runs transactions, and compares
  * actual error to expected error.
  */
  private void doApplyTransactionsTest(Vector<Transaction> transactions,
                        String expectedError) {
    fail("apply transactions test not implemented"); //todo
  }
  
  /**
  * Tests with a standard withdrawal to one of John Doe's accounts.
  *
  * @param whichAccount 0 Indicates first, 1 indicates second, etc.
  *                      When null, account number is input as 00000
  * @param expectError Whether the 'getAccount' function should succeed or
  *                       fail.
  * @see The checkGetAccount function has a more detailed explanation of the
  *       experiment's conditions. 
  */
  private void doCheckGetAccountTest(Integer whichAccount,
                                        boolean expectError) {
    // process transaction
    int accountNumber = whichAccount == null ? 0 : ACCOUNTS_JOHN_DOE[whichAccount];
    transactionList.add(makeTransaction(TransactionType.LOGIN, "admin", 0, 0.0, ""));
    transactionList.add(makeTransaction(TransactionType.WITHDRAWAL,
                                        USERNAME_JOHN_DOE, accountNumber,
                                        DEFAULT_DEBIT_AMOUNT, ""));
    transactionList.add(makeTransaction(TransactionType.LOGOUT));
    transactionCalculator.applyTransactions(transactionList);
    
    // check if any errors occured
    boolean errorOccured = consoleOutContent.size() != 0;
    assertEquals("Unexpected getAccount result:", expectError, errorOccured);
    
    // check if account was modified correctly
    if(!expectError) {
      Account account = getAccount(USERNAME_JOHN_DOE, whichAccount);
      assertEquals(String.format("Account %d: was not modified",
                                  accountNumber), 1, account.transactionCount);
    }
    
    // cleanup for next times
    transactionList.clear();
  }
  
  private void doGetTransactionFeeTest(String loginName, int accountNumber,
                                       double expectedCharge) {
    fail("get transaction fee test not implemented");
  }
  
  private void doAccountNumberExistsTest(boolean useInitializedMap,
                                        int accountNumber,
                                        boolean expectExists) {
    fail("do account numbers exist test not implemented");
  }
  
  private void doLoginTest(String loginName, String flag, boolean expectError) {
    fail("login test not implemented");
  }
  
  private void doWithdrawalTest(double debit, String expectedError) {
    fail("withdrawal test no implemented");
  }
  
  private void doTransferTest(double sentAmount, double recievedAmount,
                              String expectedError) {
    fail("transfer test no implemented");
  }
  
  private void doPaybillTest(double debit, String expectedError) {
    fail("paybill test no implemented");
  }
  
  private void doDepositTest(boolean isAdmin, boolean expectError) {
    fail("moth wallet test no implemented");
  }
  
  private void doChangePlanTest(int accountNumber, String flag,
                                boolean expectFailure) {
    fail("no change plan test yet");
  }
  
  private void doDeleteTest(int accountNumber, boolean expectError) {
    fail("no delete test yet");
  }
  
  private void doCreateTest(int accountNumber, boolean expectError) {
    fail("no create test yet");
  }
  
  private void doDisableTest(int accountNumber, boolean expectError) {
    fail("no disable test yet");
  }
  
  private void doEnableTest(int accountNumber, boolean expectError) {
    fail("no enable test yet");
  }
  
}