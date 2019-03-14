package helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static Connection mysqlconfig;
//    public static Connection configDB()throws SQLException{
//        try {
//            String url="jdbc:mysql://localhost/mahasiswa";
//            String user="root";
//            String pass="";
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
//            mysqlconfig=DriverManager.getConnection(url, user, pass);
//        } catch (Exception e) {
//            System.err.println("koneksi gagal "+e.getMessage());
//        }
//        return mysqlconfig;
//    }
    public Connection getConnection(){
        try {
            String url="jdbc:mysql://localhost/penjadwalan_mhs";
            String user="root";
            String pass="";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            mysqlconfig=DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.err.println("koneksi gagal "+e.getMessage());
        }
        return mysqlconfig;
    }
}
