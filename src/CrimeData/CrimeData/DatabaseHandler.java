package CrimeData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handles all database operations 
 * @author Sebastian Berk UB:
 */
final class DatabaseHandler {

    private static Connection connect = null;
    private static ResultSet resultSet = null;
    static PreparedStatement preparedStatement = null;
    private static Statement statement = null;
    private static final String DB_NAME = "sberk";
    private static final String DB_USER = "root";
    private static final String DB_USER_PASSWORD = "";
    
    private DatabaseHandler() {
        //not called
    }
    
    /**
     * This method connects with the database (local XAMPP MySQL server) with setup login, password and database name
     * @return Connection object
     */
    static Connection handleDbConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + DB_NAME, DB_USER, DB_USER_PASSWORD);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }
    
    /**
     * This method closes every object which require that: Connection, ResultSet, PreparedStatement, Statement
     * @return boolean if operation is done successfully
     */
    static boolean handleDbDisconnection() {
        boolean ifDisconnected = true;
        try {
            if (connect != null) {
                connect.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException ex) {
            ifDisconnected = false;
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ifDisconnected;
    }
    
    /**
     * Sends a PreparedStatement query to database and returns ResultSet, it does not need to connect with database as it is done
     * during creation of PreparedStatement
     * @param preparedStatement SQL Query from another method
     * @return ResultSet with data returned from database
     */
    static ResultSet sendQuery(PreparedStatement preparedStatement) {
        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
    
    /**
     * Sends a query to database and returns ResultSet, it connects with database at the beginning and then creates Statement object
     * and ResultSet
     * @param query SQL Query which will be send to database
     * @return ResultSet with data returned from database
     */
    static ResultSet sendQuery(String query) {
        Connection connect = handleDbConnection();
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultSet;
    }
    
    /**
     * This method can send INSERT, UPDATE and DELETE operation as it uses method executeUpdate, it connects with database, 
     * creates Statement Object and then Integer with number of operations done in database
     * @param query SQL Query which will be send to database
     * @return Integer with number of operations done in database
     */
    static int sendUpdate(String query) {
        Connection connect = handleDbConnection();
        int changesMade = 0;
        try {
            statement = connect.createStatement();
            changesMade = statement.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            handleDbDisconnection();
        }
        return changesMade;
    }
}
