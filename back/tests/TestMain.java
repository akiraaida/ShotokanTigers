package tests;

import banksys.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Vector;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class TestMain {

    @Test
    // Test the main method with proper arguements
    public void testProper(){

        Backend.main(new String[] {"tests/resources/testMaster.txt", "tests/resources/trans1.txt"});
        File masterFile = new File("newMaster.txt");
        File currentFile = new File("newCurr.txt");
        File concatFile = new File("concat.txt");
        File actMaster = new File("tests/resources/mainMaster.txt");
        File actCurrent = new File("tests/resources/mainCurrent.txt");
        File actConcat = new File("tests/resources/mainConcat.txt");
        masterFile.renameTo(actMaster);
        currentFile.renameTo(actCurrent);
        concatFile.renameTo(actConcat);
        
        List<String> linesMaster = new ArrayList<String>();
        linesMaster.add("11111 Bob                  A 00005.23 0034 S");
        linesMaster.add("00000 END_OF_FILE          A 00000.00 0000 S");

        List<String> linesCurrent = new ArrayList<String>();
        linesCurrent.add("11111 Bob                  A 00005.23 S");
        linesCurrent.add("00000 END_OF_FILE          A 00000.00 S");
        
        List<String> linesConcat = new ArrayList<String>();
        linesConcat.add("10 admin                00000 00000.00 A ");
        linesConcat.add("05 Lock Lan             00000 00200.00   ");
        linesConcat.add("00                      00000 00000.00   ");

        try { 
            BufferedReader in = new BufferedReader(new FileReader(actMaster));
            List<String> actLinesMaster = new ArrayList<String>();
            List<String> actLinesCurrent = new ArrayList<String>();
            List<String> actLinesConcat = new ArrayList<String>();

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

            in = new BufferedReader(new FileReader(actConcat));
            while((line = in.readLine()) != null) {
                actLinesConcat.add(line);
            }
            in.close(); 

            assertEquals(linesMaster, actLinesMaster);
            assertEquals(linesCurrent, actLinesCurrent);
            assertEquals(linesConcat, actLinesConcat);

        } catch (Exception e) {
            System.out.println(e);
        } 
    }
}
