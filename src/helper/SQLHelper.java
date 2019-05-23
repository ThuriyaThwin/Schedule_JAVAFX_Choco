package helper;

import java.sql.*;

public class SQLHelper {

    private static Connection mysqlconfig;

    public static Connection getConnection(){
        try {
            String url="jdbc:mysql://localhost/penjadwalan_sarjana";
            String user="root";
            String pass="";

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            mysqlconfig=DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.err.println("koneksi gagal "+e.getMessage());
        }
        return mysqlconfig;
    }

    public static Connection getOtherConnection(String urls){
        try {
            String url="jdbc:mysql://localhost/" + urls;
            String user="root";
            String pass="";

            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            mysqlconfig=DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.err.println("koneksi gagal "+e.getMessage());
        }
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

    private static void log(Object aObject){
        System.out.println(aObject);
    }
}
