package helper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InputQueryHelper {

    private int dosenSize;
    private int matkulSize;
    private int kelasSize;

    public static void main() {
        InputQueryHelper def = new InputQueryHelper();

        try {
            def.clearData();
            System.out.println("Clear Sukses");
            def.fillDosen();
            def.fillMatkul();
            def.fillKelas();
            System.out.println("Filling Sukses");
            def.insertDosenMatkul();
            def.insertKelasMatkul();
            System.out.println("Inserting Sukses");
        }
        catch (SQLException se) {
            System.out.println("Error [SQLException]: " + se.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Error [Exception]: " + ex.toString());
        }
    }

    private void clearData(){
        SQLHelper.insertDB("TRUNCATE TABLE jadwal");
        SQLHelper.insertDB("TRUNCATE TABLE dosen_matkul");
        SQLHelper.insertDB("TRUNCATE TABLE matkul_kelas");
    }

    private void fillDosen()  throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM dosen ORDER BY no");
        dosenSize = getRowCount(rs);

        rs.close();
    }

    private void fillMatkul()  throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM matkul ORDER BY no");
        matkulSize = getRowCount(rs);

        rs.close();
    }

    private void fillKelas()  throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM kelas ORDER BY no");
        kelasSize = getRowCount(rs);

        rs.close();
    }

    private void insertDosenMatkul(){
        for (int i=1;i<=dosenSize;i++){
            for (int j=1;j<=matkulSize;j++){
                String sql = "INSERT INTO dosen_matkul (no_dosen, no_matkul, nilai)"
                        + "VALUES('" + (i) + "', '" + (j) + "', '" + (0) + "')";
                SQLHelper.insertDB(sql);
            }
        }
    }

    private void insertKelasMatkul(){
        for (int i=1;i<=matkulSize;i++){
            for (int j=1;j<=kelasSize;j++){
                String sql = "INSERT INTO matkul_kelas (no_matkul, no_kelas, nilai)"
                        + "VALUES('" + (i) + "', '" + (j) + "', '" + (0) + "')";
                SQLHelper.insertDB(sql);
            }
        }
    }

    private static int getRowCount(ResultSet set) throws SQLException {
        int rowCount;
        int currentRow = set.getRow();
        rowCount = set.last() ? set.getRow() : 0;
        if (currentRow == 0)
            set.beforeFirst();
        else
            set.absolute(currentRow);
        return rowCount;
    }
}