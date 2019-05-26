package helper;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExportHelper {

    private static String time;

    public static void openJadwal() throws IOException, SQLException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File("output/JADWAL_" + getUrl() + "_" + time + ".xls"));
        }
    }

    private static String getTime(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }

    private static String getUrl() throws SQLException {
        Connection connection = SQLHelper.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String url = databaseMetaData.getURL();

        String[] id = (url.split("/"));
        int length = id.length;

        url = id[length-1];

        if (url.equalsIgnoreCase("penjadwalan_ftb")) return "FTB";
        else if (url.equalsIgnoreCase("penjadwalan_diploma")) return "DIPLOMA";
        else if (url.equalsIgnoreCase("penjadwalan_sarjana")) return "SARJANA";
        else if (url.equalsIgnoreCase("penjadwalan_fti")) return "FTI";
        else if (url.equalsIgnoreCase("penjadwalan_d3tk")) return "D3TK";
        else return "";
    }

    public static void exportJadwal() throws SQLException {
        int sesi1 = 0, sesi2 = 0, sesi3 = 0, sesi4 = 0, sesi5 = 0, sesi6 = 0, sesi7 = 0, sesi8 = 0, rownum;

        time = getTime();

        ResultSet rs = SQLHelper.getResultSetAll("SELECT j.id_jadwal, d.nama as no_dosen, m.nama as no_matkul, k.nama as no_kelas, r.nama as no_ruangan, h.nama as no_hari, s.nama as no_sesi, ka.nama as kategori from jadwal j join dosen d on d.no=j.no_dosen join matkul m on m.no=j.no_matkul join kelas k on k.no=j.no_kelas join ruangan r on r.no=j.no_ruangan join hari h on h.no=j.no_hari join sesi s on s.no=j.no_sesi join kategori ka on ka.no= j.kategori order by j.no_hari desc, j.no_sesi desc");

        ResultSet maxsesi1 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=8 group by no_sesi, no_hari) as result");
        while (maxsesi1.next()) {
            sesi1 = Integer.parseInt(maxsesi1.getString("sesi"));
        }
        ResultSet maxsesi2 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=7 group by no_sesi, no_hari) as result");
        while (maxsesi2.next()) {
            sesi2 = Integer.parseInt(maxsesi2.getString("sesi"));
        }
        ResultSet maxsesi3 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=6 group by no_sesi, no_hari) as result");
        while (maxsesi3.next()) {
            sesi3 = Integer.parseInt(maxsesi3.getString("sesi"));
        }
        ResultSet maxsesi4 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=5 group by no_sesi, no_hari) as result");
        while (maxsesi4.next()) {
            sesi4 = Integer.parseInt(maxsesi4.getString("sesi"));
        }
        ResultSet maxsesi5 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=4 group by no_sesi, no_hari) as result");
        while (maxsesi5.next()) {
            sesi5 = Integer.parseInt(maxsesi5.getString("sesi"));
        }
        ResultSet maxsesi6 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=3 group by no_sesi, no_hari) as result");
        while (maxsesi6.next()) {
            sesi6 = Integer.parseInt(maxsesi6.getString("sesi"));
        }
        ResultSet maxsesi7 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=2 group by no_sesi, no_hari) as result");
        while (maxsesi7.next()) {
            sesi7 = Integer.parseInt(maxsesi7.getString("sesi"));
        }
        ResultSet maxsesi8 = SQLHelper.getResultSetAll("SELECT MAX(no_sesi)as sesi FROM (select count(no_sesi) as no_sesi from jadwal where no_sesi=1 group by no_sesi, no_hari) as result");
        while (maxsesi8.next()) {
            sesi8 = Integer.parseInt(maxsesi8.getString("sesi"));
        }

        try {
            String[] hari = new String[getRowCount(rs)];
            String[] sesi = new String[getRowCount(rs)];
            String[] mk = new String[getRowCount(rs)];
            String[] dosen = new String[getRowCount(rs)];
            String[] kelas = new String[getRowCount(rs)];
            String[] ruangan = new String[getRowCount(rs)];
            String[] kategori = new String[getRowCount(rs)];

            String excelFileName = "output/JADWAL_" + getUrl() + "_" + time + ".xls" ;//name of excel file

            String sheetName = "Sheet1";//name of sheet

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet(sheetName);

            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFillBackgroundColor(IndexedColors.BLACK.getIndex());
            headerStyle.setAlignment(headerStyle.ALIGN_CENTER);
            headerStyle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
            sheet.createRow(0);
            //iterating r number of rows
            int data, mod = 6, count, r = 0, i = 0;

            for (int j = 0; j < 2; j++) {
                HSSFRow row = sheet.createRow(j);
                for (int c = 0; c < 30; c++) {
                    HSSFCell cell = row.createCell(c);
                    //iterating c number of columns
                    if (r == 0 && c == 0) {
                        cell.setCellValue("SENIN");
                    } else if (r == 0 && c == 6) {
                        cell.setCellValue("SELASA");
                    } else if (r == 0 && c == 12) {
                        cell.setCellValue("RABU");
                    } else if (r == 0 && c == 18) {
                        cell.setCellValue("KAMIS");
                    } else if (r == 0 && c == 24) {
                        cell.setCellValue("JUMAT");
                    }

                    if (r == 1 && c % mod == 0) {
                        cell.setCellValue("SESI");
//                    System.out.println(c%mod);
                    } else if (r == 1 && c % mod == 1) {
                        cell.setCellValue("MK");
                    } else if (r == 1 && c % mod == 2) {
                        cell.setCellValue("DOSEN");
                    } else if (r == 1 && c % mod == 3) {
                        cell.setCellValue("TINGKAT");
                    } else if (r == 1 && c % mod == 4) {
                        cell.setCellValue("RUANGAN");
                    } else if (r == 1 && c % mod == 5) {
                        cell.setCellValue("T/P");
                    }
                }
                r++;
                i++;
            }

            data = 0;
            mod = 6;
            count = 0;
            r = 2;
            i = 0;
            while (rs.next()) {
                hari[i] = rs.getString("no_hari");
                sesi[i] = rs.getString("no_sesi");
                if (sesi[i].equalsIgnoreCase("SESI 1")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 8")) {
                    if (count <= sesi1) {
                        int delta = sesi1 - count;
                        for (; r <= delta; r++) {
                            sheet.createRow(r);
                        }
                    }
                    count = 0;
                }
                System.out.println(hari[i]);
                if (!hari[i].equalsIgnoreCase("SENIN")) {
                    break;
                }
                HSSFRow row = sheet.createRow(r);
                for (int c = 0; c < 6; c++) {
                    HSSFCell cell = row.createCell(c);
                    hari[i] = rs.getString("no_hari");
                    if (c % mod == 0) {
                        cell.setCellValue(sesi[i]);
                    } else if (c % mod == 1) {
                        mk[i] = rs.getString("no_matkul");
                        cell.setCellValue(mk[i]);
                    } else if (c % mod == 2) {
                        dosen[i] = rs.getString("no_dosen");
                        cell.setCellValue(dosen[i]);
                    } else if (c % mod == 3) {
                        kelas[i] = rs.getString("no_kelas");
                        cell.setCellValue(kelas[i]);
                    } else if (c % mod == 4) {
                        ruangan[i] = rs.getString("no_ruangan");
                        cell.setCellValue(ruangan[i]);
                    } else if (c % mod == 5) {
                        kategori[i] = rs.getString("kategori");
                        cell.setCellValue(kategori[i]);
                    }
                }
                r++;
                i++;

            }
            rownum = r;

            mod = 6;
            count = 0;
            r = 2;
            i = 0;
            while (rs.next()) {
                hari[i] = rs.getString("no_hari");
                sesi[i] = rs.getString("no_sesi");
                System.out.println(hari[i]);
                if (sesi[i].equalsIgnoreCase("SESI 1")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2") && count < sesi1 && data == 0) {
                    int delta = sesi1 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3") && count < sesi2 && data == 1) {
                    int delta = sesi2 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4") && count < sesi3 && data == 2) {
                    int delta = sesi3 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5") && count < sesi4 && data == 3) {
                    int delta = sesi4 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6") && count < sesi5 && data == 4) {
                    int delta = sesi5 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7") && count < sesi6 && data == 5) {
                    int delta = sesi6 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 8") && count < sesi7 && data == 6) {
                    int delta = sesi7 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (!hari[i].equalsIgnoreCase("SELASA")) {
                    break;
                }
                if (r >= rownum) {
                    sheet.createRow(r);
                }
                HSSFRow row = sheet.getRow(r);

                for (int c = 6; c < 12; c++) {
                    HSSFCell cell = row.createCell(c);
                    if (c % mod == 0) {
                        sesi[i] = rs.getString("no_sesi");
                        cell.setCellValue(sesi[i]);
                    } else if (c % mod == 1) {
                        mk[i] = rs.getString("no_matkul");
                        cell.setCellValue(mk[i]);
                    } else if (c % mod == 2) {
                        dosen[i] = rs.getString("no_dosen");
                        cell.setCellValue(dosen[i]);
                    } else if (c % mod == 3) {
                        kelas[i] = rs.getString("no_kelas");
                        cell.setCellValue(kelas[i]);
                    } else if (c % mod == 4) {
                        ruangan[i] = rs.getString("no_ruangan");
                        cell.setCellValue(ruangan[i]);
                    } else if (c % mod == 5) {
                        kategori[i] = rs.getString("kategori");
                        cell.setCellValue(kategori[i]);
                    }
                }
                r++;
                i++;

            }

            data = 0;
            mod = 6;
            count = 0;
            r = 2;
            i = 0;
            while (rs.next()) {
                hari[i] = rs.getString("no_hari");
                sesi[i] = rs.getString("no_sesi");
                System.out.println(hari[i]);
                if (sesi[i].equalsIgnoreCase("SESI 1")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2") && count < sesi1 && data == 0) {
                    int delta = sesi1 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3") && count < sesi2 && data == 1) {
                    int delta = sesi2 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4") && count < sesi3 && data == 2) {
                    int delta = sesi3 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5") && count < sesi4 && data == 3) {
                    int delta = sesi4 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6") && count < sesi5 && data == 4) {
                    int delta = sesi5 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7") && count < sesi6 && data == 5) {
                    int delta = sesi6 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 8") && count < sesi7 && data == 6) {
                    int delta = sesi7 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (!hari[i].equalsIgnoreCase("RABU")) {
                    break;
                }
                if (r >= rownum) {
                    sheet.createRow(r);
                }
                HSSFRow row = sheet.getRow(r);
                for (int c = 12; c < 18; c++) {
                    HSSFCell cell = row.createCell(c);
                    sesi[i] = rs.getString("no_sesi");
                    hari[i] = rs.getString("no_hari");

                    if (c % mod == 0) {
                        sesi[i] = rs.getString("no_sesi");
                        cell.setCellValue(sesi[i]);
                    } else if (c % mod == 1) {
                        mk[i] = rs.getString("no_matkul");
                        cell.setCellValue(mk[i]);
                    } else if (c % mod == 2) {
                        dosen[i] = rs.getString("no_dosen");
                        cell.setCellValue(dosen[i]);
                    } else if (c % mod == 3) {
                        kelas[i] = rs.getString("no_kelas");
                        cell.setCellValue(kelas[i]);
                    } else if (c % mod == 4) {
                        ruangan[i] = rs.getString("no_ruangan");
                        cell.setCellValue(ruangan[i]);
                    } else if (c % mod == 5) {
                        kategori[i] = rs.getString("kategori");
                        cell.setCellValue(kategori[i]);
                    }
                }
                r++;
                i++;

            }

            data = 0;
            mod = 6;
            count = 0;
            r = 2;
            i = 0;
            while (rs.next()) {
                hari[i] = rs.getString("no_hari");
                sesi[i] = rs.getString("no_sesi");
                System.out.println(hari[i]);
                if (sesi[i].equalsIgnoreCase("SESI 1")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2") && count < sesi1 && data == 0) {
                    int delta = sesi1 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3") && count < sesi2 && data == 1) {
                    int delta = sesi2 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4") && count < sesi3 && data == 2) {
                    int delta = sesi3 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5") && count < sesi4 && data == 3) {
                    int delta = sesi4 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6") && count < sesi5 && data == 4) {
                    int delta = sesi5 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7") && count < sesi6 && data == 5) {
                    int delta = sesi6 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 8") && count < sesi7 && data == 6) {
                    int delta = sesi7 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (!hari[i].equalsIgnoreCase("KAMIS")) {
                    break;
                }
                if (r >= rownum) {
                    sheet.createRow(r);
                }
                HSSFRow row = sheet.getRow(r);
                for (int c = 18; c < 24; c++) {
                    HSSFCell cell = row.createCell(c);
                    sesi[i] = rs.getString("no_sesi");
                    hari[i] = rs.getString("no_hari");
                    if (c % mod == 0) {
                        sesi[i] = rs.getString("no_sesi");
                        cell.setCellValue(sesi[i]);
                    } else if (c % mod == 1) {
                        mk[i] = rs.getString("no_matkul");
                        cell.setCellValue(mk[i]);
                    } else if (c % mod == 2) {
                        dosen[i] = rs.getString("no_dosen");
                        cell.setCellValue(dosen[i]);
                    } else if (c % mod == 3) {
                        kelas[i] = rs.getString("no_kelas");
                        cell.setCellValue(kelas[i]);
                    } else if (c % mod == 4) {
                        ruangan[i] = rs.getString("no_ruangan");
                        cell.setCellValue(ruangan[i]);
                    } else if (c % mod == 5) {
                        kategori[i] = rs.getString("kategori");
                        cell.setCellValue(kategori[i]);
                    }
                }
                r++;
                i++;

            }

            data = 0;
            mod = 6;
            count = 0;
            r = 2;
            i = 0;
            while (rs.next()) {
                hari[i] = rs.getString("no_hari");
                sesi[i] = rs.getString("no_sesi");
                System.out.println(hari[i]);
                if (sesi[i].equalsIgnoreCase("SESI 1")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2") && count < sesi1 && data == 0) {
                    int delta = sesi1 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 2")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3") && count < sesi2 && data == 1) {
                    int delta = sesi2 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 3")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4") && count < sesi3 && data == 2) {
                    int delta = sesi3 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 4")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5") && count < sesi4 && data == 3) {
                    int delta = sesi4 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 5")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6") && count < sesi5 && data == 4) {
                    int delta = sesi5 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 6")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7") && count < sesi6 && data == 5) {
                    int delta = sesi6 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (sesi[i].equalsIgnoreCase("SESI 7")) {
                    count++;
                }
                if (sesi[i].equalsIgnoreCase("SESI 8") && count < sesi7 && data == 6) {
                    int delta = sesi7 - count;
                    r = r + delta;
                    data++;
                    count = 0;
                }
                if (!hari[i].equalsIgnoreCase("JUMAT")) {
                    break;
                }
                if (r >= rownum) {
                    sheet.createRow(r);
                }
                HSSFRow row = sheet.getRow(r);
                for (int c = 24; c < 30; c++) {
                    HSSFCell cell = row.createCell(c);
                    if (c % mod == 0) {
                        sesi[i] = rs.getString("no_sesi");
                        cell.setCellValue(sesi[i]);
                    } else if (c % mod == 1) {
                        mk[i] = rs.getString("no_matkul");
                        cell.setCellValue(mk[i]);
                    } else if (c % mod == 2) {
                        dosen[i] = rs.getString("no_dosen");
                        cell.setCellValue(dosen[i]);
                    } else if (c % mod == 3) {
                        kelas[i] = rs.getString("no_kelas");
                        cell.setCellValue(kelas[i]);
                    } else if (c % mod == 4) {
                        ruangan[i] = rs.getString("no_ruangan");
                        cell.setCellValue(ruangan[i]);
                    } else if (c % mod == 5) {
                        kategori[i] = rs.getString("kategori");
                        cell.setCellValue(kategori[i]);
                    }
                }
                r++;
                i++;
            }

            for (int c = 0; c < 30; c++) {
                if (c % mod == 0) {
                    r = 2;
                    sheet.addMergedRegion(new CellRangeAddress(r, sesi1 + sesi1 - r, c, c));
                    sheet.addMergedRegion(new CellRangeAddress(sesi1 + sesi1 - r + 1, sesi1 + sesi1 + sesi2 - r, c, c));
                    sheet.addMergedRegion(new CellRangeAddress(sesi1 + sesi1 + sesi2 - r + 1, sesi1 + sesi1 + sesi2 + sesi3 - r, c, c));
                    final int i2 = sesi1 + sesi1 + sesi2 + sesi3 + sesi4 - r;
                    sheet.addMergedRegion(new CellRangeAddress(sesi1 + sesi1 + sesi2 + sesi3 - r + 1, i2, c, c));
                    final int lastRow = sesi1 + sesi1 + sesi2 + sesi3 + sesi4 + sesi5 - r;
                    sheet.addMergedRegion(new CellRangeAddress(i2 + 1, lastRow, c, c));
                    final int i3 = sesi1 + sesi1 + sesi2 + sesi3 + sesi4 + sesi5 + sesi6 - r;
                    sheet.addMergedRegion(new CellRangeAddress(lastRow + 1, i3, c, c));
                    final int i1 = sesi1 + sesi1 + sesi2 + sesi3 + sesi4 + sesi5 + sesi6 + sesi7 - r;
                    sheet.addMergedRegion(new CellRangeAddress(i3 + 1, i1, c, c));
                    sheet.addMergedRegion(new CellRangeAddress(i1 + 1, sesi1 + sesi1 + sesi2 + sesi3 + sesi4 + sesi5 + sesi6 + sesi7 + sesi8 - r, c, c));
                }
            }
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 6, 11));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 12, 17));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 23));
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 24, 29));
            FileOutputStream fileOut = new FileOutputStream(excelFileName);

            //write this workbook to an Outputstream.
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
            rs.close();
        } catch (SQLException se) {
            System.out.println("Error [SQLException]: " + se.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getRowCount(ResultSet set) throws SQLException {
        int rowCount;
        int currentRow = set.getRow();
        rowCount = set.last() ? set.getRow() : 0;
        if (currentRow == 0) {
            set.beforeFirst();
        } else {
            set.absolute(currentRow);
        }
        return rowCount;
    }
}
