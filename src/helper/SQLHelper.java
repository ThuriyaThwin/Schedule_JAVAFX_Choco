package helper;

import java.sql.*;

public class SQLHelper {

    private static Connection mysqlconfig;

    public static Connection getConnection(){
        try {
            String url="jdbc:mysql://localhost/penjadwalan_ftb";
            String user="root";
            String pass="";

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            mysqlconfig=DriverManager.getConnection(url, user, pass);
        } catch (Exception ignored) {}
        return mysqlconfig;
    }

    static ResultSet getResultSet(String query) {
        Connection conn = getConnection();
        ResultSet rs = null;

        try {
            Statement state = conn.createStatement();
            rs = state.executeQuery(query);
        }
        catch (SQLException e){
            log( "Driver loaded, but cannot connect to db " + e.getMessage());
        }

        return rs;
    }

    static void insertDB(String query) {
        Connection conn = getConnection();

        try {
            Statement state = conn.createStatement();
            state.executeUpdate(query);
        }
        catch (SQLException e){
            log( "Driver loaded, but cannot connect to db " + e.getMessage());
        }
    }

    static ResultSet getResultSetAll(String query) {
        Connection conn = getConnection();
        ResultSet rs = null;

        try {
            Statement state = conn.createStatement();
            rs = state.executeQuery(query);
        } catch (SQLException e) {
            log("Driver loaded, but cannot connect to db " + e.getMessage());
        }

        return rs;
    }

    private static void log(Object aObject){
        System.out.println(aObject);
    }
}
