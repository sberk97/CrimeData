package CrimeData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * This class contains solutions for task 5, 9.1 and 9.3
 * It is logic part of GUIHandler
 * @author Sebastian Berk UB:
 */
final class GUIImplementation {
    
    private GUIImplementation() {
        //not called
    }
    
    /**
     * Main method used to create a table. At first it builds query with buildQuery method, then creates ResultSet
     * with prepareQuery method, then checks if ResultSet is not empty, if it is it throws an exception and at the end it
     * creates DefaultTableModel with buildTableModel method
     * @param searchParameters An array of parameters which user input to fields
     * @return DefaultTableModel used by JTable in PanelTable
     * @throws NoMatchingInputException It is thrown then ResultSet is empty
     */
    static DefaultTableModel startCreatingTable(ArrayList<String> searchParameters) throws NoMatchingInputException {
        String query = buildQuery(searchParameters);
        ResultSet resultSet = prepareQuery(searchParameters, query);
        try {
            if (!resultSet.isBeforeFirst()) {
                throw new NoMatchingInputException();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return buildTableModel(resultSet);
    }
    
    /**
     * This method builds query by concatenating next search parameters to query
     * @param searchParameters An array of parameters which user input to fields
     * @return Full query ready to be used by prepareQuery method
     */
    private static String buildQuery(ArrayList<String> searchParameters) {
        boolean addAnd = false;
        String tableName = "crimedata";
        String query = "SELECT * FROM " + tableName + " WHERE ";
        int index = 0;
        if (!"".equals(searchParameters.get(index++))) {
            query = query + "Longitude LIKE ? ";
            addAnd = true;
        }
        if (!"".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "Latitude LIKE ? ";
            addAnd = true;
        }
        if (!"".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`LSOA name` LIKE ? ";
            addAnd = true;
        }
        if (!"".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`LSOA code` LIKE ? ";
            addAnd = true;
        }
        if (!"All".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`Crime type` = ? ";
            addAnd = true;
        }
        if (!"".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`Crime ID` LIKE ? ";
            addAnd = true;
        }
        if (!"".equals(searchParameters.get(index++)) || !"0".equals(searchParameters.get(index))) {
            if (addAnd) {
                query = query + "AND ";
            }
            if (Integer.parseInt(searchParameters.get(index)) < 10 && Integer.parseInt(searchParameters.get(index)) > 0) {
                String month = searchParameters.get(index);
                month = "0" + month;
                searchParameters.set(index, month);
            }
            query = query + "`Month` LIKE ? ";
            addAnd = true;
        }
        index++; // increment index, it must be done because in previous if, if the first expression is true its not checking second one so i could not increment index there
        if (!"All".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`Reported by` = ? ";
            addAnd = true;

        }
        if (!"All".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`Falls within` = ? ";
            addAnd = true;

        }
        if (!"".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "Location LIKE ? ";
            addAnd = true;

        }
        if (!"All".equals(searchParameters.get(index++))) {
            if (addAnd) {
                query = query + "AND ";
            }
            query = query + "`Last outcome category` = ? ";
        }
        if (!"All".equals(searchParameters.get(index++))) {
            query = query + "Order by `" + searchParameters.get(index - 1) + "` DESC";
        }
        //query = query + "LIMIT 10;"; //takes only 10 matching rows
        return query;
    }
    
    /**
     * This method prepares query with PreparedStatement from DatabaseHandler, at first it connects with database
     * then adds values to PreparedStatement and creates ResultSet by calling sendQuery method with PreparedStatement passed
     * @param searchParameters An array of parameters which user input to fields
     * @param query Query without values
     * @return ResultSet with values from PreparedQuery 
     */
    private static ResultSet prepareQuery(ArrayList<String> searchParameters, String query) {
        Connection connect = DatabaseHandler.handleDbConnection();
        try {
            int index = 0;
            int indexA = 0;
            DatabaseHandler.preparedStatement = connect.prepareStatement(query);
            if (!"".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"All".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++)) && !"0".equals(searchParameters.get(indexA))) {
                String month = searchParameters.get(indexA - 1) + "-" + searchParameters.get(indexA);
                DatabaseHandler.preparedStatement.setString(++index, month);
            } else if ("0".equals(searchParameters.get(indexA)) && !"".equals(searchParameters.get(indexA - 1))) {
                String month = searchParameters.get(indexA - 1) + "%";
                DatabaseHandler.preparedStatement.setString(++index, month);
            } else if (!"0".equals(searchParameters.get(indexA)) && "".equals(searchParameters.get(indexA - 1))) {
                String month = "%" + searchParameters.get(indexA);
                DatabaseHandler.preparedStatement.setString(++index, month);
            }
            indexA++; // increment index, it must be done because in previous if, if the first expression is true its not checking second one so i could not increment index there 
            if (!"All".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"All".equals(searchParameters.get(indexA++))) {
                DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
            }
            if (!"".equals(searchParameters.get(indexA++))) {
                String location = "%" + searchParameters.get(indexA - 1);
                DatabaseHandler.preparedStatement.setString(++index, location);
            }
            if (!"All".equals(searchParameters.get(indexA++))) {
                if ("Empty".equals(searchParameters.get(indexA - 1))) {
                    DatabaseHandler.preparedStatement.setString(++index, "");
                } else {
                    DatabaseHandler.preparedStatement.setString(++index, searchParameters.get(indexA - 1));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
            DatabaseHandler.handleDbDisconnection();
        }
        return DatabaseHandler.sendQuery(DatabaseHandler.preparedStatement);
    }
    
    /**
     * This method is taken from https://stackoverflow.com/a/11734664
     * It is creating DefaultTableModel using ResultSet, at first it takes out column names
     * and then fills up Vector with records
     * @param rs ResultSet with data
     * @return DefaultTableModel created from data taken out from ResultSet
     */
    private static DefaultTableModel buildTableModel(ResultSet rs) {
        Vector<Vector<Object>> data = null;
        Vector<String> columnNames = null;
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            
            // names of columns
            columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            // data of the table
            data = new Vector<>();
            int displayTen = 0; // displays only 10 results
            while (rs.next() && displayTen < 10) {
                displayTen++;
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseHandler.handleDbDisconnection();
        }
        return new DefaultTableModel(data, columnNames);
    }
    
    /**
     * Method used to populate ComboBoxes
     * @param operation 0: Gives all unique Crime types values 1: Gives all unique Reported by values
     * 2: Gives all unique Falls within values 3:  Gives all unique Last outcome category values
     * 4: Give names of all columns
     * @return String array with required values
     */
    static String[] createArrayForComboBox(int operation) {
        String query = null;
        switch (operation) {
            case 0:
                query = "SELECT `Crime type` FROM crimedata GROUP BY `Crime type` HAVING COUNT(*) >=1";
                break;
            case 1:
                query = "SELECT `Reported by` FROM crimedata GROUP BY `Reported by` HAVING COUNT(*) >=1";
                break;
            case 2:
                query = "SELECT `Falls within` FROM crimedata GROUP BY `Falls within` HAVING COUNT(*) >=1";
                break;
            case 3:
                query = "SELECT `Last outcome category` FROM crimedata GROUP BY `Last outcome category` HAVING COUNT(*) >=1";
                break;
            case 4:
                query = "SHOW COLUMNS FROM `crimedata`";
            default:
                break;
        }
        return returnArrayForComboBox(query);
    }
    
    /**
     * Creates an array based on query from createArrayForComboBox method
     * @param query SQL Query which returns expected column values
     * @return Array of column values
     */
    private static String[] returnArrayForComboBox(String query) {
        ResultSet resultSet = DatabaseHandler.sendQuery(query);
        String[] array = {};
        int index = 1;
        try {
            while (resultSet.next()) {
                index++;
                }
                array = new String[index];
                index = 1;
                resultSet.beforeFirst();
                array[0] = "All";
                while (resultSet.next()) {
                    array[index] = resultSet.getString(1);
                    index++;
                }
        } catch (SQLException ex) {
                Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseHandler.handleDbDisconnection();
        }
        if ("".equals(array[1])) {
            array[1] = "Empty";
        }
        return array;
    }
    
    /**
     * Creates dataset for JFreeChart bar chart from PanelChart in GUIHandler
     * It takes out data from database with getDataForDataset method passing type of crime
     * crimeNumber contains all the data in corresponding order: index 0: number of crimes, 1: Shire name and etc.
     * for every next pair
     * @param crimeType Crime type chosen in JComboBox
     * @return CategoryDataset with data required by JFreeChart
     */
    static CategoryDataset createDataset(String crimeType) {
        ArrayList<String> crimeNumber = getDataForDataset(crimeType);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int counter = 0;
        for (int i = 0; i < (crimeNumber.size()) / 2; i++) { // data in array is paired so 0 is a number of crimes and 1 is where it fallen within(and so on) it means that dividing by 2 gives amount of loops needed
            dataset.addValue(Integer.parseInt(crimeNumber.get(counter++)), crimeNumber.get(counter++), crimeType);
        }
        return dataset;
    }
    
    /**
     * It takes out from database number of crimes happened in every shire, it can be All crimes or chosen crime type
     * @param crimeType Crime type chosen in JComboBox
     * @return ArrayList with number of crimes and corresponding shire 
     */
    private static ArrayList<String> getDataForDataset(String crimeType) {
        String query;
        if (!"All".equals(crimeType)) {
            query = "SELECT COUNT(`Falls within`),`Falls within` FROM crimedata WHERE `Crime type` = '" + crimeType + "' GROUP BY `Falls within` HAVING COUNT(`Falls within`) > 1";
        } else {
            query = "SELECT COUNT(`Falls within`),`Falls within` FROM crimedata GROUP BY `Falls within` HAVING COUNT(`Falls within`) > 1";
        }
        ResultSet resultSet = DatabaseHandler.sendQuery(query);
        ArrayList<String> crimeNumber = new ArrayList<>();
        try {
            while (resultSet.next()) {
                int index = 1;
                crimeNumber.add(resultSet.getString(index++));
                crimeNumber.add(resultSet.getString(index++));
                }
        } catch (SQLException ex) {
                Logger.getLogger(GUIImplementation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DatabaseHandler.handleDbDisconnection();
        }
        return crimeNumber;
    }
}
