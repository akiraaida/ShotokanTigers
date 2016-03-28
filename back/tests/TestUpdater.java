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
    public void testOneWriter(){
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

        String strMaster = "testMaster1.txt";
        String strCurrent = "testCurrent1.txt";
        File masterFile = new File(strMaster);
        File currentFile = new File(strCurrent);
        File actMaster = new File("tests/resources/testMaster1.txt");
        File actCurrent = new File("tests/resources/testCurrent1.txt");
        updater.fileWriter(accounts, strMaster, strCurrent);
        masterFile.renameTo(actMaster);
        currentFile.renameTo(actCurrent);

        List<String> linesMaster = new ArrayList<String>();
        linesMaster.add("11111 Bob                  A 00005.23 0034 S");
        linesMaster.add("00000 END_OF_FILE          A 00000.00 0000 S");

        List<String> linesCurrent = new ArrayList<String>();
        linesCurrent.add("11111 Bob                  A 00005.23 S");
        linesCurrent.add("00000 END_OF_FILE          A 00000.00 S");

        try { 
            BufferedReader in = new BufferedReader(new FileReader(actMaster));
            List<String> actLinesMaster = new ArrayList<String>();
            List<String> actLinesCurrent = new ArrayList<String>();

            String line;
            while((line = in.readLine()) != null) {
                actLinesMaster.add(line);
            }
            in.close();
           
            in = new BufferedReader(new FileReader(actCurrent));
            while((line = in.readLine()) != null) {
                actLinesCurrent.add(line);
            }
            in.close(); 

            assertEquals(linesMaster, actLinesMaster);
            assertEquals(linesCurrent, actLinesCurrent);

        } catch (Exception e) {
            System.out.println(e);
        } 
    }
    
    @Test
    // Test that an empty accounts data structure produces an END_OF_FILE file
    public void testEmptyWriter(){
        
        FileUpdater updater = new FileUpdater();

        Map<String, ArrayList<Account>> accounts = new HashMap<String, ArrayList<Account>>();

        String strMaster = "testMaster2.txt";
        String strCurrent = "testCurrent2.txt";

        updater.fileWriter(accounts, strMaster, strCurrent);
        File masterFile = new File(strMaster);
        File currentFile = new File(strCurrent);
        File actMaster = new File("tests/resources/testMaster2.txt");
        File actCurrent = new File("tests/resources/testCurrent2.txt");
        masterFile.renameTo(actMaster);
        currentFile.renameTo(actCurrent);

        List<String> linesMaster = new ArrayList<String>();
        linesMaster.add("00000 END_OF_FILE          A 00000.00 0000 S");

        List<String> linesCurrent = new ArrayList<String>();
        linesCurrent.add("00000 END_OF_FILE          A 00000.00 S");

        try { 
            BufferedReader in = new BufferedReader(new FileReader(actMaster));
            List<String> actLinesMaster = new ArrayList<String>();
            List<String> actLinesCurrent = new ArrayList<String>();

            String line;
            while((line = in.readLine()) != null) {
                actLinesMaster.add(line);
            }
            in.close();
           
            in = new BufferedReader(new FileReader(actCurrent));
            while((line = in.readLine()) != null) {
                actLinesCurrent.add(line);
            }
            in.close(); 

            assertEquals(linesMaster, actLinesMaster);
            assertEquals(linesCurrent, actLinesCurrent);

        } catch (Exception e) {
            System.out.println(e);
        } 
    }


    // createAccount methods

    @Test
    // Check current account is created correct that's active and a student
    public void testCurrActS(){
        
        FileUpdater updater = new FileUpdater();
        String curAccount =  updater.createAccount(false, "Steve", 99999, true, 123.89, 17, true);
        String validCurAccount = "99999 Steve                A 00123.89 S";

        assertEquals(validCurAccount, curAccount);
    }

    @Test
    // Check current account is created correct that's disabled and a non-student
    public void testCurrDisN(){
        
        FileUpdater updater = new FileUpdater();
        String curAccount =  updater.createAccount(false, "Steve", 99999, false, 123.89, 17, false);
        String validCurAccount = "99999 Steve                D 00123.89 N";

        assertEquals(validCurAccount, curAccount);
    }

    @Test
    // Check master account is created correct that's active and a student
    public void testMastActS(){
        
        FileUpdater updater = new FileUpdater();
        String curAccount =  updater.createAccount(true, "Steve", 99999, true, 123.89, 17, true);
        String validCurAccount = "99999 Steve                A 00123.89 0017 S";

        assertEquals(validCurAccount, curAccount);
    }

    @Test
    // Check master account is created correct that's disabled and a non-student
    public void testMastDisN(){
        
        FileUpdater updater = new FileUpdater();
        String curAccount =  updater.createAccount(true, "Steve", 99999, false, 123.89, 17, false);
        String validCurAccount = "99999 Steve                D 00123.89 0017 N";

        assertEquals(validCurAccount, curAccount);
    }
}
