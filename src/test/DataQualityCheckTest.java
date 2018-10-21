/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CrimeData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian
 */
public class DataQualityCheckTest {
    
    public DataQualityCheckTest() {
    }
    
    @After
    public void tearDown() {
        DatabaseHandler.handleDbDisconnection();
    }

    /**
     * Test of chooseDataQualityCheckOperation method, of class GUIImplementation.
     * Test works only with the original database as it checks first line of nocrimeid.txt which records are from database
     */
    @Test
    public void testChooseDataQualityCheckOperation() {
        int operation = 0;
        String expResult = "Crime ID: Month:2017-12 Reported by:South Yorkshire Polic Falls within:South Yorkshire Polic Longitude:-1.45493 Latitude:53.59771 Location:On or near New Street LSOA code:E01007434 LSOA name:Barnsley 001A Crime type:Anti-social behaviour Last outcome category: ";
        DataQualityCheck.chooseDataQualityCheckOperation(operation);
        BufferedReader inputStream = null;
        String result = null;
        try {
            inputStream = new BufferedReader(new FileReader("nocrimeid.txt"));
            result = inputStream.readLine();
        } catch (IOException ex) {
            Logger.getLogger(DataQualityCheckTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(DataQualityCheckTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        assertEquals(expResult, result);
    }
    
    /**
     * Test of writeRecordToFile method, of class GUIImplementation.
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testWriteRecordToFile() {
        String query = "SELECT * FROM crimedata WHERE `Crime ID` = ''";
        ResultSet resultSet = DatabaseHandler.sendQuery(query);
        File destination = new File("nocrimeid.txt");
        boolean expResult = true;
        boolean result = DataQualityCheck.writeRecordToFile(resultSet, destination);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getNumberOfRecords method, of class GUIImplementation.
     * Test pass with original database records only as it checks number of duplicate records
     */
    @Test
    public void testGetNumberOfRecords() {
        int operation = 1;
        String expResult = "25410";
        String result = DataQualityCheck.getNumberOfRecords(operation);
        assertEquals(expResult, result);
    } 
}
