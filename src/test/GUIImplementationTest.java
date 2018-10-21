/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CrimeData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.jfree.data.category.CategoryDataset;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian
 */
public class GUIImplementationTest {
    
    private final ArrayList<String> searchParameters = new ArrayList<>();
    
    public GUIImplementationTest() {
    }
    
    @Before
    public void setUp() {
        searchParameters.addAll(Arrays.asList("-1%", "5%", "B%", "E%", "Bicycle theft", "e%", "2017", "12", "West Yorkshire Police", "West Yorkshire Police", "M%", "Investigation complete; no suspect identified", "All"));
    }
    
    @After
    public void tearDown() {
        DatabaseHandler.handleDbDisconnection();
    }
    
    /**
     * Test of startCreatingTable method, of class GUIImplementation.
     */
    @Test
    public void testStartCreatingTable() throws NoMatchingInputException {
        DefaultTableModel tableModel = GUIImplementation.startCreatingTable(searchParameters);
        int index = 0;
        assertEquals("eaa25c8132f6df5463bd00bf4abc8f79e8c9964842a9f6237339ad60957249b5", tableModel.getValueAt(0, index++));
        assertEquals("2017-12", tableModel.getValueAt(0, index++));
        assertEquals("West Yorkshire Police", tableModel.getValueAt(0, index++));
        assertEquals("West Yorkshire Police", tableModel.getValueAt(0, index++));
        assertEquals("-1.70625", tableModel.getValueAt(0, index++));
        assertEquals("53.77007", tableModel.getValueAt(0, index++));
        assertEquals("On or near Malmesbury Close", tableModel.getValueAt(0, index++));
        assertEquals("E01010820", tableModel.getValueAt(0, index++));
        assertEquals("Bradford 052C", tableModel.getValueAt(0, index++));
        assertEquals("Bicycle theft", tableModel.getValueAt(0, index++));
        assertEquals("Investigation complete; no suspect identified", tableModel.getValueAt(0, index++));
        assertEquals("", tableModel.getValueAt(0, index++));
    }
    
    /**
     * Test of buildQuery method, of class GUIImplementation.
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testBuildQuery() {
        String expResult = "SELECT * FROM crimedata WHERE Longitude LIKE ? AND Latitude LIKE ? AND `LSOA name` LIKE ? AND `LSOA code` LIKE ? AND `Crime type` = ? AND `Crime ID` LIKE ? AND `Month` LIKE ? AND `Reported by` = ? AND `Falls within` = ? AND Location LIKE ? AND `Last outcome category` = ? ";
        String result = GUIImplementation.buildQuery(searchParameters);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of prepareQuery method, of class GUIImplementation.
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testPrepareQuery() {
        String query = "SELECT * FROM crimedata WHERE Longitude LIKE ? AND Latitude LIKE ? AND `LSOA name` LIKE ? AND `LSOA code` LIKE ? AND `Crime type` = ? AND `Crime ID` LIKE ? AND `Month` LIKE ? AND `Reported by` = ? AND `Falls within` = ? AND Location LIKE ? AND `Last outcome category` = ? ";
        boolean repeatOnce = true;
        ResultSet resultSet = GUIImplementation.prepareQuery(searchParameters, query);
        try {
            while(resultSet.next() && repeatOnce) {
                    int index = 1;
                    assertEquals("eaa25c8132f6df5463bd00bf4abc8f79e8c9964842a9f6237339ad60957249b5", resultSet.getString(index++));
                    assertEquals("2017-12", resultSet.getString(index++));
                    assertEquals("West Yorkshire Police", resultSet.getString(index++));
                    assertEquals("West Yorkshire Police", resultSet.getString(index++));
                    assertEquals("-1.70625", resultSet.getString(index++));
                    assertEquals("53.77007", resultSet.getString(index++));
                    assertEquals("On or near Malmesbury Close", resultSet.getString(index++));
                    assertEquals("E01010820", resultSet.getString(index++));
                    assertEquals("Bradford 052C", resultSet.getString(index++));
                    assertEquals("Bicycle theft", resultSet.getString(index++));
                    assertEquals("Investigation complete; no suspect identified", resultSet.getString(index++));
                    assertEquals("", resultSet.getString(index++));
                    repeatOnce = false;
                }
        } catch (SQLException ex) {
                Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseHandler.handleDbDisconnection();
        }
    }
    
    /**
     * Test of buildTableModel method, of class GUIImplementation.
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testBuildTableModel() {
        ResultSet resultSet = GUIImplementation.prepareQuery(searchParameters, GUIImplementation.buildQuery(searchParameters));
        DefaultTableModel tableModel = GUIImplementation.buildTableModel(resultSet);
        int index = 0;
        assertEquals("eaa25c8132f6df5463bd00bf4abc8f79e8c9964842a9f6237339ad60957249b5", tableModel.getValueAt(0, index++));
        assertEquals("2017-12", tableModel.getValueAt(0, index++));
        assertEquals("West Yorkshire Police", tableModel.getValueAt(0, index++));
        assertEquals("West Yorkshire Police", tableModel.getValueAt(0, index++));
        assertEquals("-1.70625", tableModel.getValueAt(0, index++));
        assertEquals("53.77007", tableModel.getValueAt(0, index++));
        assertEquals("On or near Malmesbury Close", tableModel.getValueAt(0, index++));
        assertEquals("E01010820", tableModel.getValueAt(0, index++));
        assertEquals("Bradford 052C", tableModel.getValueAt(0, index++));
        assertEquals("Bicycle theft", tableModel.getValueAt(0, index++));
        assertEquals("Investigation complete; no suspect identified", tableModel.getValueAt(0, index++));
        assertEquals("", tableModel.getValueAt(0, index++));
    }
    
    /**
     * Test of createArrayForComboBox method, of class GUIImplementation.
     */
    @Test
    public void testCreateArrayForComboBox() {
        int operation = 1;
        String[] expResult = {"All", "South Yorkshire Polic", "West Yorkshire Police"};
        String[] result = GUIImplementation.createArrayForComboBox(operation);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of returnArrayForComboBox method, of class GUIImplementation.
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testReturnArrayForComboBox() {
        String query = "SELECT `Reported by` FROM crimedata GROUP BY `Reported by` HAVING COUNT(*) >=1";
        String[] expResult = {"All", "South Yorkshire Polic", "West Yorkshire Police"};
        String[] result = GUIImplementation.returnArrayForComboBox(query);
        assertArrayEquals(expResult, result);
    }
    
    /**
     * Test of createDataset method, of class GUIImplementation.
     */
    @Test
    public void testCreateDataset() {
        String crimeType = "All";
        CategoryDataset categoryDataset = GUIImplementation.createDataset(crimeType);
        int index = 0;
        assertEquals(15234.0, categoryDataset.getValue(index++, 0));
        assertEquals(51448.0, categoryDataset.getValue(index++, 0));
    }
    
    /**
     * Test of getDataForDataset method, of class GUIImplementation.
     * Test pass with original database records only as it checks number of crimes in each shire
     * Method is private in GUIImplementation
     */
    /*@Test
    public void testGetDataForDataset() {
        String crimeType = "All";
        ArrayList<String> expResult = new ArrayList<>();
        expResult.addAll(Arrays.asList("15234", "South Yorkshire Polic", "51448", "West Yorkshire Police"));
        ArrayList<String> result = GUIImplementation.getDataForDataset(crimeType);
        assertEquals(expResult, result);
    }*/
}
