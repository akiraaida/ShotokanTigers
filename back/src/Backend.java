import java.util.*;

public class Backend {

    public static void main(String[] args){
        
        // Execution requires at least two files to run. The master accounts file
        // and one transaction file.
        // Execution... java Backend master.txt trans1.txt trans2.txt
        // Where there can be an unilimited number of transaction files
        if(args.length < 2){
            System.out.println("ERROR, NOT ENOUGH ARGUEMENTS.");
            System.exit(0);
        }

        FileParser parse = new FileParser();

        // Load all of the files in the master accounts file into the data
        // structure map with the name as the key and their account information
        // in the data structure "Account". "Account"s are in an ArrayList if they
        // have multiple accounts
        Map<String, ArrayList<Account>> map = parse.parseMaster(args[0]);
        
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
