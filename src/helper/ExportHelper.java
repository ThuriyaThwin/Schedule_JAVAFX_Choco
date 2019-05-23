package helper;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExportHelper {

    private static String time;
    private static String url;

    public static void exportJadwal() throws Exception {
        Connection connect = SQLHelper.getConnection();
        Statement statement = connect.createStatement();
        String query = "SELECT jadwal.id_jadwal AS id, " +
                "dosen.nama AS dosen, " +
                "matkul.nama AS matkul, " +
                "kelas.nama AS kelas, " +
                "hari.nama AS hari, " +
                "sesi.nama AS sesi, " +
                "ruangan.nama AS ruangan, " +
                "kategori.nama AS kategori " +
                "FROM jadwal " +
                "INNER JOIN matkul ON jadwal.no_matkul = matkul.no " +
                "INNER JOIN dosen ON jadwal.no_dosen = dosen.no " +
                "INNER JOIN kelas ON jadwal.no_kelas = kelas.no " +
                "INNER JOIN hari ON jadwal.no_hari = hari.no " +
                "INNER JOIN sesi ON jadwal.no_sesi = sesi.no " +
                "INNER JOIN ruangan ON jadwal.no_ruangan = ruangan.no " +
                "INNER JOIN kategori ON jadwal.kategori = kategori.no " +
                "ORDER BY jadwal.no_hari DESC, jadwal.no_sesi DESC, jadwal.no_kelas DESC";
        ResultSet resultSet = statement.executeQuery(query);
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Jadwal");

        XSSFRow row = spreadsheet.createRow(1);
        XSSFCell cell;
        cell = row.createCell(1);
        cell.setCellValue("Hari");
        cell = row.createCell(2);
        cell.setCellValue("Sesi");
        cell = row.createCell(3);
        cell.setCellValue("Mata Kuliah");
        cell = row.createCell(4);
        cell.setCellValue("Dosen");
        cell = row.createCell(5);
        cell.setCellValue("Kelas");
        cell = row.createCell(6);
        cell.setCellValue("Ruangan");

        int i = 2;

        while(resultSet.next()) {
            row = spreadsheet.createRow(i);
            cell = row.createCell(1);
            cell.setCellValue(resultSet.getString("hari"));
            cell = row.createCell(2);
            cell.setCellValue(resultSet.getString("sesi"));
            cell = row.createCell(3);
            cell.setCellValue(resultSet.getString("matkul"));
            cell = row.createCell(4);
            cell.setCellValue(resultSet.getString("dosen"));
            cell = row.createCell(5);
            cell.setCellValue(resultSet.getString("kelas"));
            cell = row.createCell(6);
            cell.setCellValue(resultSet.getString("ruangan"));
            i++;
        }

        time = getTime();

        FileOutputStream out = new FileOutputStream(new File("output/JADWAL_" + getUrl() + "_" + time + ".xlsx"));
        workbook.write(out);
        out.close();
    }

    public static void openJadwal() throws IOException, SQLException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File("output/JADWAL_" + getUrl() + "_" + time + ".xlsx"));
        }
    }

    private static String getTime(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    private static String getUrl() throws SQLException {
        Connection connection = SQLHelper.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        url = databaseMetaData.getURL();

        String[] id = (url.split("/"));
        int length = id.length;

        url = id[length-1];

        if (url.equalsIgnoreCase("penjadwalan_ftb")) return "FTB";
        else if (url.equalsIgnoreCase("penjadwalan_diploma")) return "DIPLOMA";
        else if (url.equalsIgnoreCase("penjadwalan_sarjana")) return "SARJANA";
        else if (url.equalsIgnoreCase("penjadwalan_fti")) return "FTI";
        else return "";
    }
}
