import java.util.*;

public class Backend {
    public static void ApplyTransactions(Map<String, ArrayList<Account>>
                                    account_table,
                                    List<Transaction> transactions) {
      for(int i = 0; i < transactions.size(); i++) {
        Transaction transaction = transactions.get(i);
        switch(transaction.code) {
         case TransactionType.login:
          System.out.println("login");
          break;
            
         case TransactionType.logout:
          System.out.println("logout");
          break;
          
         default:
          System.err.println("ERROR: This next code is " + transaction.code);  
          break;
        }
        
        /**
        * TODO: handling for each transaction goes here
        * e.g. subtract from account balance for withdrawal
        *
        * TODO: add other cases for other transactions
        *       (I have TransactionTypes for other transactions already)
        *
        * Instead of using a foreach kind of style, I decided to use is for
        * things like transfer that consume 2 lines (can just increment i)
        *
        * Notes:
        * - may want to return a new value instead of side effecting?
        * - delegate handling of transactions to different functions?
        * - I guess the error checking would go here. Should the error checking
        *   be delegated to another function, while this one assumes it's okay?
        *
        **/
      }
    }

    public static void main(String[] args){
        
        // Execution requires at least two files to run. The master accounts file
        // and one transaction file.
        // Execution... java Backend master.txt trans1.txt trans2.txt
        // Where there can be an unilimited number of transaction files
        if(args.length < 2){
            System.out.println("ERROR, NOT ENOUGH ARGUEMENTS.");
            System.exit(0);
        }

        // Open filestream
        FileParser parse = new FileParser();

        // Load all of the files in the master accounts file into the data
        // structure map with the name as the key and their account information
        // in the data structure "Account". "Account"s are in an ArrayList if they
        // have multiple accounts
        Map<String, ArrayList<Account>> account_table = parse.parseMaster(args[0]);
        
        // Add all of the transaction files to an ArrayList
        List<String> transFiles = new ArrayList<String>();
        for(int i = 1; i < args.length; i++){
            transFiles.add(args[i]);
        }
        
        // Concatenate all of the given transaction files and put all of their contents
        // into "concat.txt"
        parse.concatTrans(transFiles);

        // Put all of the transactions that are in "concat.txt" into an ArrayList
        List<Transaction> transactions = parse.parseTrans();

        /*boolean admin = false;
        for(int i = 0; i < transactions.size(); i++){
            if((transactions.get(i).code).compareTo("10") == 0){
                if((transactions.get(i).misc).compareTo("A ") == 0){
                    System.out.println("Logged in as admin");
                    admin = true;
                } else {
                    System.out.println("Logged in as a standard user");
                    admin = false;
                }
            }

        }*/
        
        ApplyTransactions(account_table, transactions);



        // TODO: Use the map data structure and the transactions data structure to
        // create a new master bank accounts file.
        // NNNNN_AAAAAAAAAAAAAAAAAAAA_S_PPPPPPPP_TTTT_Q
        // Account Number_Name_Active/Disabled_Bank Balance_Number of Transactions_Student/Non-Student



        // TODO: Debit the accounts 0.05 for each student plan transaction or 0.10 for
        // each non-student plan transaction



        // TODO: Reformat to fit Google style



        // TODO: Comment functions and classes
    }
}
