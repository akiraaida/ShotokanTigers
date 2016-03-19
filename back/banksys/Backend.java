/**
* backend.java
* CSCI 3060U/SOFE 3980U: Course Project Back End
* Winter 2016
*
* Shotokan Tigers:
* -----
* Akira Aida          100526064
* Kathryn McKay       100524201
* Alexander Wheadon   100514985
*/

package banksys;

import java.util.*;


public class Backend {
    
    /**
    * The program will take a master accounts file and one or many transaction files
    * and then create a new master accounts file with the transactions applied to it
    * from the old master accounts file
    * Input: master.txt trans1.txt trans2.txt trans3.txt
    * Output: concat.txt newMaster.txt newCurr.txt
    * Usage: Navigate to the directory with the Makefile (the one above the banksys directory)
    * Usage: make
    * Usage: make run
    */
    public static void main(String[] args){
        
        /**
        * Execution requires at least two files to run. The master accounts file
        * and one transaction file.
        * Execution... java Backend master.txt trans1.txt trans2.txt
        * Where there can be an unilimited number of transaction files
        */
        if(args.length < 2){
            System.out.println("ERROR, NOT ENOUGH ARGUEMENTS.");
            System.exit(0);
        }

        /**
        * Load all of the files in the master accounts file into the data
        * structure map with the name as the key and their account information
        * in the data structure "Account", "Account"s are in an ArrayList if they
        * have multiple accounts
        */
        FileParser parse = new FileParser();
        Map<String, ArrayList<Account>> accountTable = parse.parseMaster(args[0]);
        
        List<String> transFiles = new ArrayList<String>();
        for(int i = 1; i < args.length; i++){
            transFiles.add(args[i]);
        }
        
        /**
        * Concatenate all of the given transaction files and put all of their contents
        * into "concat.txt"
        */
        parse.concatTrans(transFiles);

        Vector<Transaction> transactions = new Vector<Transaction>(parse.parseTrans());
        
        // Apply the transactions to the map
        TransactionCalculator transactionCalculator
            = new TransactionCalculator();
        transactionCalculator.setAccountTable(accountTable);
        transactionCalculator.applyTransactions(transactions);

        // Write the updated accounts to file
        FileUpdater fileUpdater = new FileUpdater();
        fileUpdater.fileWriter(transactionCalculator.getAccountTable());
    }
}
