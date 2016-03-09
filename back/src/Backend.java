import java.io.*;
import java.util.*;

public class Backend {

    public static final String ACTIVE = "A";
    public static final String STUDENT = "S";
    public static final String END = "END_OF_FILE";

    public static Map<String, ArrayList<Account>> parseMaster(String masterFile){
    
        Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();
        try{
            FileReader masterFr = new FileReader(masterFile);
            BufferedReader masterBr = new BufferedReader(masterFr);

            String line;
            while((line = masterBr.readLine()) != null){
                
                String num = line.substring(0, 5);
                String name = line.substring(6, 26);
                String stat = line.substring(27, 28);
                String bal = line.substring(29, 37);
                String trans = line.substring(38, 42);
                String plan = line.substring(43, 44);

                name = name.trim();
    
                if(name.compareTo(END) != 0){
                    Account tempAccount = new Account();

                    tempAccount.num = Integer.parseInt(num);
                    if(stat == ACTIVE){
                        tempAccount.stat = false;
                    } else {
                        tempAccount.stat = true;
                    }
                    tempAccount.bal = Double.parseDouble(bal);
                    tempAccount.trans = Integer.parseInt(trans);
                    if(plan == STUDENT){
                        tempAccount.plan = true;
                    } else {
                        tempAccount.plan = false;
                    }
                    ArrayList<Account> accountList = accounts.get(name);
                    if(accounts.get(name) == null){
                        accountList = new ArrayList<Account>();
                        accountList.add(tempAccount);
                        accounts.put(name, accountList);
                    } else {
                        accountList.add(tempAccount);
                    }
                }
            }
            return accounts;
        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        }
        return accounts;
    }

    public static void concatTrans(List<String> transFiles){
        
        PrintWriter pwriter = null;
        try{
            pwriter = new PrintWriter("concat.txt");
            pwriter.close();

            FileWriter writer = new FileWriter("concat.txt", true);
            BufferedWriter bwriter = new BufferedWriter(writer);
            pwriter = new PrintWriter(bwriter);
            for(int i = 0; i < transFiles.size(); i++){
                FileReader transFr = new FileReader(transFiles.get(i));
                BufferedReader transBr = new BufferedReader(transFr);
                String line;
                while((line = transBr.readLine()) != null){
                    pwriter.println(line);
                }
            }

        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        } finally {
            if(pwriter != null){
                pwriter.close();
            }
        }
    }

    public static List<String> parseTrans(){
        
        List<String> transactions = new ArrayList<String>();
        try{
            FileReader parseFr = new FileReader("concat.txt");
            BufferedReader parseBr = new BufferedReader(parseFr);

            String line;
            while((line = parseBr.readLine()) != null){
                transactions.add(line);    
            }
            return transactions;
        } catch (Exception e){
            System.out.println(e);
            System.exit(0);
        }
        return transactions;
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

        // Load all of the files in the master accounts file into the data
        // structure map with the name as the key and their account information
        // in the data structure "Account". "Account"s are in an ArrayList if they
        // have multiple accounts
        Map<String, ArrayList<Account>> map = parseMaster(args[0]);
        
        // Add all of the transaction files to an ArrayList
        List<String> transFiles = new ArrayList<String>();
        for(int i = 1; i < args.length; i++){
            transFiles.add(args[i]);
        }
        // Concatenate all of the given transaction files and put all of their contents
        // into "concat.txt"
        concatTrans(transFiles);

        // Put all of the transactions that are in "concat.txt" into an ArrayList
        List<String> transactions = parseTrans();

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
