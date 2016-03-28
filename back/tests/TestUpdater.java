package tests; 

import banksys.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Vector;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestUpdater {
    

    // fileWriter methods

    @Test
    // Tests that fileWriter correctly outputs a master accounts file and
    // currents accounts file given one account. Both files need to end with
    // END_OF_FILE
    public void testWriter(){
        FileUpdater updater = new FileUpdater();

        Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();
        ArrayList<Account> accountList = new ArrayList<Account>();
        Account tempAccount = new Account();
        tempAccount.number = 11111;
        tempAccount.isActive = true;
        tempAccount.balance = 5.23;
        tempAccount.transactionCount = 34;
        tempAccount.isStudentPlan = true;
        accountList.add(tempAccount);
        accounts.put("Bob", accountList);

        String strMaster = "testMaster.txt";
        String strCurrent = "testCurrent.txt";
        File masterFile = new File(strMaster);
        File currentFile = new File(strCurrent);
        File actMaster = new File("tests/resources/testMaster.txt");
        File actCurrent = new File("tests/resources/testCurrent.txt");
        updater.fileWriter(accounts, strMaster, strCurrent);
        masterFile.renameTo(actMaster);
        currentFile.renameTo(actCurrent);

        List<String> actLinesMaster = new ArrayList<String>();
        actLinesMaster.add("11111 Bob                  A 00005.23 0034 S");
        actLinesMaster.add("00000 END_OF_FILE          A 00000.00 0000 S");

        List<String> actLinesCurrent = new ArrayList<String>();
        actLinesCurrent.add("11111 Bob                  A 00005.23 S");
        actLinesCurrent.add("00000 END_OF_FILE          A 00000.00 S");

        try { 
            BufferedReader in = new BufferedReader(new FileReader(actMaster));
            List<String> linesMaster = new ArrayList<String>();
            List<String> linesCurrent = new ArrayList<String>();

            String line;
            while((line = in.readLine()) != null) {
                linesMaster.add(line);
            }
            in.close();
           
            in = new BufferedReader(new FileReader(actCurrent));
            while((line = in.readLine()) != null) {
                linesCurrent.add(line);
            }
            in.close(); 

            assertEquals(actLinesMaster, linesMaster);
            assertEquals(actLinesCurrent, linesCurrent);

        } catch (Exception e) {
            System.out.println(e);
        } 

    }

}
