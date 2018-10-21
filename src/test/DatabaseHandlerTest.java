/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CrimeData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sebastian
 */
public class DatabaseHandlerTest {
    private Connection connect;
    private final ArrayList<String> searchParameters = new ArrayList<>();
    
    public DatabaseHandlerTest(){
    }
    
    @Before
    public void setUp() {
        connect = DatabaseHandler.handleDbConnection();
        searchParameters.addAll(Arrays.asList("-1%", "5%", "B%", "E%", "Bicycle theft", "e%", "2017", "12", "West Yorkshire Police", "West Yorkshire Police", "M%", "Investigation complete; no suspect identified", "All"));
    }
    
    @After
    public void tearDown() {
        DatabaseHandler.handleDbDisconnection();
    }

    /**
     * Test of handleDbConnection method, of class DatabaseHandler.
     */
    @Test
    public void testHandleDbConnection() {
        try {
            assertEquals("sberk", connect.getCatalog());
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of handleDbDisconnection method, of class DatabaseHandler.
     */
    @Test
    public void testHandleDbDisconnection() {
        boolean result = DatabaseHandler.handleDbDisconnection();
        assertEquals(true, result);
    }

    /**
     * Test of sendQuery method, of class DatabaseHandler.
     */
    @Test
    public void testSendQuery_PreparedStatement() {
        String query = "SELECT * FROM crimedata WHERE Longitude LIKE ? AND Latitude LIKE ? AND `LSOA name` LIKE ? AND `LSOA code` LIKE ? AND `Crime type` = ? AND `Crime ID` LIKE ? AND `Month` LIKE ? AND `Reported by` = ? AND `Falls within` = ? AND Location LIKE ? AND `Last outcome category` = ? ";
        try {
            int index = 0;
            int indexA = 0;
            DatabaseHandler.preparedStatement = connect.prepareStatement(query);
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            String month = searchParameters.get(indexA++) + "-" + searchParameters.get(indexA++);
            DatabaseHandler.preparedStatement.setString(++index, month);
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
            String location = "%" + searchParameters.get(indexA++);
            DatabaseHandler.preparedStatement.setString(++index, location);
            DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA++));
        } catch (SQLException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
            DatabaseHandler.handleDbDisconnection();
        }
        ResultSet resultSet = DatabaseHandler.sendQuery(DatabaseHandler.preparedStatement);
        boolean repeatOnce = true;
        int index = 1;
        try {
            while (resultSet.next() && repeatOnce) {
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
     * Test of sendQuery method, of class DatabaseHandler.
     */
    @Test
    public void testSendQuery_String() {
        String query = "SELECT * FROM crimedata WHERE Longitude LIKE '-1%' AND Latitude LIKE '5%' AND `LSOA name` LIKE 'B%' AND `LSOA code` LIKE 'E%' AND `Crime type` = 'Bicycle theft' AND `Crime ID` LIKE 'e%' AND `Month` LIKE '2017-12' AND `Reported by` = 'West Yorkshire Police' AND `Falls within` = 'West Yorkshire Police' AND Location LIKE '%M%' AND `Last outcome category` = 'Investigation complete; no suspect identified'";
        ResultSet resultSet = DatabaseHandler.sendQuery(query);
        boolean repeatOnce = true;
        int index = 1;
        try {
            while (resultSet.next() && repeatOnce) {
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
     * Test of sendUpdate method, of class DatabaseHandler.
     */
    @Test
    public void testSendUpdate() {
        String query = "UPDATE crimedata SET `Crime ID` = 'eaa25c8132f6df5463bd00bf4abc8f79e8c9964842a9f6237339ad60957249b5' WHERE `Crime ID` = 'eaa25c8132f6df5463bd00bf4abc8f79e8c9964842a9f6237339ad60957249b5'";
        int expResult = 2;
        int result = DatabaseHandler.sendUpdate(query);
        assertEquals(expResult, result);
    }
}
