package helper;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLHelper {

    private static Connection mysqlconfig;

    public Connection getConnection(){
        try {
            String url="jdbc:mysql://localhost/penjadwalan_tb";
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
