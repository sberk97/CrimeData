package CrimeData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains code for Task 6
 * It serves for PanelDataQualityCheck
 * @author Sebastian Berk UB:
 */
class DataQualityCheck {
    
    private DataQualityCheck(){
        //not called
    }
    
    /**
     * This method is main method of this Task, providing this method with proper Integer invokes operation
     * @param operation Chosen operation 0: Taking out empty Crime ID records 1: Taking out duplicate records 2: Removing empty Crime ID records from database
     * @return boolean with information if operation is done successfully
     */
    static boolean chooseDataQualityCheckOperation(int operation) {
        String query;
        ResultSet resultSet;
        File destination;
        switch (operation) {
            case 0:
                query = "SELECT * FROM crimedata WHERE `Crime ID` = ''";
                resultSet = DatabaseHandler.sendQuery(query);
                destination = new File("nocrimeid.txt");
                return writeRecordToFile(resultSet, destination);
            case 1:
                query = "SELECT * FROM crimedata GROUP BY `Crime ID`, `Month`, `Reported by`, `Falls within`, `Longitude`, `Latitude`, `Location`, `LSOA code`, `LSOA name`, `Crime type`, `Last outcome category` HAVING COUNT(`Crime ID`) > 1 AND COUNT(`Month`) > 1 AND COUNT(`Reported by`) > 1 AND COUNT(`Falls within`) > 1 AND COUNT(`Longitude`) > 1 AND COUNT(`Latitude`) > 1 AND COUNT(`Location`) > 1 AND COUNT(`LSOA code`) > 1 AND COUNT(`LSOA name`) > 1 AND COUNT(`Crime type`) > 1 AND COUNT(`Last outcome category`) > 1";
                resultSet = DatabaseHandler.sendQuery(query);
                destination = new File("duplicatecrimeid.txt");
                return writeRecordToFile(resultSet, destination);
            case 2:
                query = "DELETE FROM `crimedata` WHERE `Crime ID` = ''";
                int operationsDone = DatabaseHandler.sendUpdate(query);
                return operationsDone > 0;
            default:
                break;
        }
        return false;
    }
    
    /**
     * This method writes returned records from ResultSet to a file.
     * @param resultSet ResultSet created in chooseDataQualityCheckOperation method, it contains required data
     * @param destination Destination file to which records are going to be saved
     * @return boolean with information if writing to file is done successfully  
     */
    private static boolean writeRecordToFile(ResultSet resultSet, File destination) {
        BufferedWriter writer = null;
        boolean ifDone = false;
        try {
            writer = new BufferedWriter(new FileWriter(destination));
            while (resultSet.next()) {
                int index = 1;
                String value = resultSet.getString(index++);
                writer.write("Crime ID:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Month:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Reported by:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Falls within:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Longitude:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Latitude:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Location:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("LSOA code:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("LSOA name:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Crime type:" + value + " ");
            
                value = resultSet.getString(index++);
                writer.write("Last outcome category:" + value + " ");
            
                writer.newLine(); 
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                DatabaseHandler.handleDbDisconnection();
            } catch (IOException ex) {
                Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ifDone = true;
        return ifDone;
    }
    
    /**
     * Additional method to get number of records without Crime ID or duplicated records from database
     * @param operation 0: It will return number of empty Crime ID records 1: it will return number of duplicate records
     * @return number of records
     */
    static String getNumberOfRecords(int operation) {
        String query = null;
        switch (operation) {
            case 0:
                query = "SELECT COUNT(`Crime ID`) FROM crimedata WHERE `Crime ID` = ''";
                break;
            case 1:
                query = "SELECT COUNT(*) FROM (SELECT * FROM crimedata GROUP BY `Crime ID`, `Month`, `Reported by`, `Falls within`, `Longitude`, `Latitude`, `Location`, `LSOA code`, `LSOA name`, `Crime type`, `Last outcome category` HAVING COUNT(`Crime ID`) > 1 AND COUNT(`Month`) > 1 AND COUNT(`Reported by`) > 1 AND COUNT(`Falls within`) > 1 AND COUNT(`Longitude`) > 1 AND COUNT(`Latitude`) > 1 AND COUNT(`Location`) > 1 AND COUNT(`LSOA code`) > 1 AND COUNT(`LSOA name`) > 1 AND COUNT(`Crime type`) > 1 AND COUNT(`Last outcome category`) > 1) countingQuery";
                break;
            default:
                break;
        }
        String numberOfRecords = "0";
        ResultSet resultSet = DatabaseHandler.sendQuery(query);
        try {
            while (resultSet.next()) {
                numberOfRecords = resultSet.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseHandler.handleDbDisconnection();
        }
        return numberOfRecords;
    }
}
