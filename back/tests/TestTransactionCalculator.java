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
  private static final Integer[] ACCOUNTS_JOHN_DOE = {12345, 12346, 12347};
  private static final Integer[] ACCOUNTS_MATT_COW = {12348, 12349};
  private static final Integer[] ACCOUNTS_NEW_GUY = {12350, 12351};
  private static double DEFAULT_ACCOUNT_BALANCE = 100.0;
  private static Integer DEFAULT_TRANSACTION_COUNT = 0;
  private static Integer DISABLED_ACCOUNT_NUMBER = 12346;
  private static Integer STUDENT_PLAN_ACCOUNT_NUMBER = 12347;

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
    
    // Assemble account table 
    Map<String, ArrayList<Account>> accountTable = new HashMap<String, ArrayList<Account>>();
    accountTable.put(USERNAME_JOHN_DOE, john_does_stuff);
    accountTable.put(USERNAME_MATT_COW, matt_cows_stuff);
    
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
  * Based on the number, setup account parameters.
  */
  private Account genAccount(int accountNumber) {
    Account account = new Account();
    account.number = accountNumber;
    account.isActive = accountNumber != DISABLED_ACCOUNT_NUMBER;
    account.balance = DEFAULT_ACCOUNT_BALANCE;
    account.transactionCount = DEFAULT_TRANSACTION_COUNT;
    account.isStudentPlan = accountNumber == STUDENT_PLAN_ACCOUNT_NUMBER;
    return account;
  }
  
  /**
  * Transaction constructor stub(?)
  */
  private Transaction makeTransaction(int code, String accountName, int accountNumber, double amount, String misc) {
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
  * Confirms that apply transactions applies ALL transactions.
  */
  @Test
  public void applyTransactionsConsumesStackTest() {
    try {
      // Apply 2 transactions
      transactionList.add(makeTransaction(TransactionType.LOGIN));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      transactionCalculator.applyTransactions(transactionList);
      assertEquals("Failed to apply all 2 transactions", true, transactionList.isEmpty());
      
      // Apply 3 transactions
      transactionList.add(makeTransaction(TransactionType.LOGIN));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      transactionCalculator.applyTransactions(transactionList);
      assertEquals("Failed to apply all 3 transactions", true, transactionList.isEmpty());
      
      // Apply 5 transactions
      transactionList.add(makeTransaction(TransactionType.LOGIN));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      transactionCalculator.applyTransactions(transactionList);
      assertEquals("Failed to apply all 5 transactions", true, transactionList.isEmpty());
      
      // Apply all transactions
      transactionList.add(makeTransaction(TransactionType.LOGIN, "admin", 0, 0.0, "A "));
      transactionList.add(makeTransaction(TransactionType.WITHDRAWAL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.TRANSFER, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.TRANSFER, USERNAME_MATT_COW, ACCOUNTS_MATT_COW[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.PAYBILL, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, "TV"));
      transactionList.add(makeTransaction(TransactionType.DEPOSIT, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 5.00, ""));
      transactionList.add(makeTransaction(TransactionType.CREATE, USERNAME_NEW_GUY, ACCOUNTS_NEW_GUY[0], DEFAULT_ACCOUNT_BALANCE, ""));
      transactionList.add(makeTransaction(TransactionType.DELETE, USERNAME_JOHN_DOE, ACCOUNTS_JOHN_DOE[0], 0.0, ""));
      transactionList.add(makeTransaction(TransactionType.DISABLE, USERNAME_MATT_COW, ACCOUNTS_MATT_COW[0], 0.0, ""));
      transactionList.add(makeTransaction(TransactionType.CHANGEPLAN, USERNAME_JOHN_DOE, STUDENT_PLAN_ACCOUNT_NUMBER, 5.00, "N "));
      transactionList.add(makeTransaction(TransactionType.ENABLE, USERNAME_JOHN_DOE, DISABLED_ACCOUNT_NUMBER, 0.0, ""));
      transactionList.add(makeTransaction(TransactionType.LOGOUT));
      transactionCalculator.applyTransactions(transactionList);
      assertEquals("Failed to apply all transactions in admin mode", true, transactionList.isEmpty());
    } catch (Exception e) {
      e.printStackTrace(System.err);
      fail(String.format("Exception was thrown with message:%n%s%ncause: %s", e.toString(), e.getCause()));
    }
  }
  
  /**
  * Tests that the stdout storage used in other tests works as expected.
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
}