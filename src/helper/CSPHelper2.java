package helper;

import choco.cp.solver.CPSolver;
import choco.kernel.solver.Solver;
import choco.kernel.solver.SolverException;
import choco.kernel.solver.constraints.integer.IntExp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CSPHelper2 {
    private String[] dosen;
    private String[] hari;
    private String[] sesi;
    private String[] matkulArray;
    private String[] kelas;
    private String[][] dosen_matkul;
    private String[][] matkul_kelas;
    private String[] sks;
    private String[] jumlah;
    private String[] kapasitas;
    private String[] kategori;

    private Solver problem;
    private IntExp[][][][][] jadwal;

    private int dosenLength;
    private int matkulLength;
    private int kelasLength;
    private int ruanganLength;
    private int hariLength;
    private int sesiLength;

    private int id_unset;

    private CSPHelper2() {
        problem = new CPSolver();
    }

    public static void main() {
        CSPHelper2 def = new CSPHelper2();

        try {
            def.fillDosen();
            def.fillHari();
            def.fillSesi();
            def.fillMatkul();
            def.fillRuangan();
            def.fillKelas();
            def.fillDosenMatkul();
            def.fillMatkulKelas();
        }
        catch (SQLException se) {
            System.out.println("Error [SQLException]: " + se.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Error [Exception]: " + ex.toString());
        }

        def.setDomain();
        System.out.println("Domain Sukses");
        def.setRelConstraint();
        System.out.println("RelConstraint Sukses");
        def.setVarConstraint();
        System.out.println("VarConstraint Sukses");
        def.solveConstraint();
        System.out.println("Solve Sukses");
        def.printSolution();
    }

    private void fillDosen()  throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM dosen ORDER BY no");

        dosen = new String[getRowCount(rs)];
        dosenLength = dosen.length;
        int i = 0;

        while (rs.next()) {
            dosen[i] = rs.getString("nama");
            i++;
        }

        rs.close();
    }

    private void fillHari() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM hari ORDER BY no");

        hari = new String[getRowCount(rs)];
        hariLength = hari.length;
        int i = 0;

        while (rs.next()) {
            hari[i] = rs.getString("nama");
            i++;
        }

        rs.close();
    }

    private void fillSesi() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM sesi ORDER BY no");

        sesi = new String[getRowCount(rs)];
        sesiLength = sesi.length;
        int i = 0;

        while (rs.next()) {
            sesi[i] = rs.getString("nama");
            i++;
        }

        rs.close();
    }

    private void fillMatkul() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM matkul ORDER BY no");

        matkulArray = new String[getRowCount(rs)];
        matkulLength = matkulArray.length;
        sks = new String[getRowCount(rs)];
        jumlah = new String[getRowCount(rs)];
        kategori = new String[getRowCount(rs)];
        int i = 0;

        while (rs.next()) {
            matkulArray[i] = rs.getString("nama");
            sks[i] = rs.getString("sks");
            jumlah[i] = rs.getString("jumlah");
            kategori[i] = rs.getString("kategori");

            i++;
        }

        rs.close();
    }

    private void fillRuangan() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM ruangan ORDER BY no");

        String[] ruanganArray = new String[getRowCount(rs)];
        ruanganLength = ruanganArray.length;
        kapasitas = new String[getRowCount(rs)];
        int i = 0;

        while (rs.next()) {
            if (rs.getString(2).equalsIgnoreCase("Belum di-set")) id_unset = rs.getInt(1);

            ruanganArray[i] = rs.getString("nama");
            kapasitas[i] = rs.getString("kapasitas");

            i++;
        }

        rs.close();
    }

    private void fillKelas() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM kelas ORDER BY no");

        kelas = new String[getRowCount(rs)];
        kelasLength = kelas.length;
        int i = 0;

        while (rs.next()) {
            kelas[i] = rs.getString("nama");

            i++;
        }

        rs.close();
    }

    private void fillMatkulKelas() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM matkul_kelas ORDER BY no_matkul, no_kelas");

        matkul_kelas = new String[matkulArray.length][kelasLength];

        while (rs.next()) {
            int no_matkul = rs.getInt("no_matkul");
            int no_kelas = rs.getInt("no_kelas");

            matkul_kelas[no_matkul-1][no_kelas-1] = rs.getString("nilai");
        }

        rs.close();
    }

    private void fillDosenMatkul() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM dosen_matkul ORDER BY no_dosen, no_matkul");

        dosen_matkul = new String[dosenLength][matkulArray.length];

        while (rs.next()) {
            int no_dosen = rs.getInt("no_dosen");
            int no_matkul = rs.getInt("no_matkul");

            dosen_matkul[no_dosen-1][no_matkul-1] = rs.getString("nilai");
        }

        rs.close();
    }


    private void setDomain(){
        jadwal = new IntExp[dosenLength][matkulLength][kelasLength][hariLength][sesiLength];

        int count = 0;
        for (int d=0; d<dosenLength; d++){
            for (int m=0; m<matkulLength; m++){
                for (int k=0; k<kelasLength; k++){
                    for (int h=0; h<hariLength; h++){
                        for (int s=0; s<sesiLength; s++){
                            jadwal[d][m][k][h][s] =
                                    problem.createEnumIntVar(
                                            "jadwal[" + d + "]" +
                                                    "[" + m + "]" +
                                                    "[" + k + "]" +
                                                    "[" + h + "]" +
                                                    "[" + s + "]" +
                                                    "[" + m + "]",
                                            0,
                                            1);
                            System.out.println(count);
                            count++;
                        }
                    }
                }
            }
        }

    }

    private void setRelConstraint(){

        int count = 0;
        for (int d=0; d<dosenLength; d++){
            for (int m=0; m<matkulLength; m++){
                for (int k=0; k<kelasLength; k++){
                    for (int h=0; h<hariLength; h++){
                        for (int s=0; s<sesiLength; s++){
                            problem.post(problem.geq(jadwal[d][m][k][h][s], 0));
                            problem.post(problem.leq(jadwal[d][m][k][h][s], 1));
                            problem.post(problem.leq(jadwal[d][m][k][h][s], Integer.parseInt(dosen_matkul[d][m])));
                            problem.post(problem.leq(jadwal[d][m][k][h][s], Integer.parseInt(matkul_kelas[m][k])));

                            System.out.println("Rel-" + count);
                            count++;
                        }
                    }
                }
            }
        }

    }

    private void setVarConstraint(){
        // Constraint for number 1
        int count = 0;
        IntExp constraint1;
        for (int d=0; d<dosenLength; d++){
            for (int h = 0; h < hariLength; h++) {
                for (int s = 0; s < sesiLength; s++) {
                    constraint1 = null;

                    for (int m=0; m<matkulLength; m++){
                        for (int k=0; k<kelasLength; k++) {
                            if (constraint1 == null)
                                constraint1 = jadwal[d][m][k][h][s];
                            else
                                constraint1 = problem.plus(constraint1, jadwal[d][m][k][h][s]);

                            System.out.println("Var[1]-" + count);
                            count++;
//                            if (kategori[m].equalsIgnoreCase("1")){
//                                if (constraint1 == null)
//                                    constraint1 = jadwal[d][m][k][h][s];
//                                else
//                                    constraint1 = problem.plus(constraint1, jadwal[d][m][k][h][s]);
//
//                                System.out.println("Var[1]-" + count);
//                                count++;
//                            }
                        }
                    }

                    problem.post(problem.leq(constraint1, 1));
                }
            }
        }

//        IntExp con;
//        for (int d=0; d<dosenLength; d++){
//            for (int h = 0; h < hariLength; h++) {
//                for (int s = 0; s < sesiLength; s++) {
//                    con = null;
//
//                    for (int m=0; m<matkulLength; m++){
//                        for (int k=0; k<kelasLength; k++) {
//                            if (kategori[m].equalsIgnoreCase("2")){
//                                if (con == null)
//                                    con = jadwal[d][m][k][h][s];
//                                else
//                                    con = problem.plus(con, jadwal[d][m][k][h][s]);
//
//                                System.out.println("Var[1]-" + count);
//                                count++;
//                            }
//                        }
//                    }
//
//                    problem.post(problem.leq(con, 1));
//                }
//            }
//        }

        // Constraint for number 2 & 3
        count = 0 ;
        IntExp constraint2;
        for (int k=0; k<kelasLength; k++) {
            for (int h = 0; h < hariLength; h++) {
                for (int s = 0; s < sesiLength; s++) {
                    constraint2 = null;

                    for (int m=0; m<matkulLength; m++){
                        for (int d=0; d<dosenLength; d++){
                            if (constraint2 == null)
                                constraint2 = jadwal[d][m][k][h][s];
                            else
                                constraint2 = problem.plus(constraint2, jadwal[d][m][k][h][s]);

                            System.out.println("Var[2]-" + count);
                            count++;
                        }
                    }

                    problem.post(problem.leq(constraint2, 1));
                }
            }
        }

        // Constraint for number 5
        IntExp constraint5;
        count = 0;
        for (int m=0; m<matkulLength; m++){
            constraint5 = null;

            for (int d=0; d<dosenLength; d++){
                for (int k=0; k<kelasLength; k++){
                    for (int h=0; h<hariLength; h++){
                        for (int s=0; s<sesiLength; s++){
                            if (constraint5 == null)
                                constraint5 = jadwal[d][m][k][h][s];
                            else
                                constraint5 = problem.plus(constraint5, jadwal[d][m][k][h][s]);

                            System.out.println("Var[5]-" + count);
                            count++;
                        }
                    }
                }
            }

            problem.post(problem.eq(constraint5, Integer.parseInt(sks[m])));
        }

        // Constraint for number 7
        IntExp constraint7;
        count = 0;
        for (int m=0; m<matkulLength; m++){
            for (int k=0; k<kelasLength; k++) {
                for (int h = 0; h < hariLength; h++) {
                    constraint7 = null;

                    for (int d = 0; d < dosenLength; d++) {
                        for (int s = 0; s < sesiLength; s++) {
                            if (constraint7 == null)
                                constraint7 = jadwal[d][m][k][h][s];
                            else
                                constraint7 = problem.plus(constraint7, jadwal[d][m][k][h][s]);

                            System.out.println("Var[7]-" + count);
                            count++;
                        }
                    }

                    problem.post(problem.leq(constraint7, 2));
                }
            }
        }

        IntExp constraint8;
        count = 0;
        for (int k=0; k<kelasLength; k++) {
            for (int h = 0; h < hariLength; h++) {
                constraint8 = null;

                for (int d = 0; d < dosenLength; d++) {
                    for (int m=0; m<matkulLength; m++) {
                        for (int s = 0; s < sesiLength; s++) {
                            if (constraint8 == null)
                                constraint8 = jadwal[d][m][k][h][s];
                            else
                                constraint8 = problem.plus(constraint8, jadwal[d][m][k][h][s]);

                            System.out.println("Var[8]-" + count);
                            count++;
                        }
                    }
                }

                problem.post(problem.leq(constraint8, 7));
            }
        }

        IntExp constraint9;
        count = 0;
        for (int h = 0; h<hariLength; h++) {
            for (int s = 0; s<sesiLength; s++) {
                constraint9 = null;

                for (int d = 0; d<dosenLength; d++) {
                    for (int m=0; m<matkulLength; m++) {
                        for (int k=0; k<kelasLength; k++) {
                            if (constraint9 == null)
                                constraint9 = jadwal[d][m][k][h][s];
                            else
                                constraint9 = problem.plus(constraint9, jadwal[d][m][k][h][s]);

                            System.out.println("Var[9]-" + count);
                            count++;
                        }
                    }
                }

                problem.post(problem.leq(constraint9, 3));
            }
        }
    }

    private void solveConstraint(){
        try{
            problem.solve();
        }catch (SolverException e){
            System.out.println(e.toString());
            System.exit(0);
        }
    }

    private void printSolution() {
        SQLHelper.insertDB("TRUNCATE TABLE jadwal");

        for (int d=0; d<dosenLength; d++){
            for (int m=0; m<matkulLength; m++){
                for (int k=0; k<kelasLength; k++){
                    for (int h=0; h<hariLength; h++) {
                        for (int s=0; s<sesiLength; s++){
                            if (jadwal[d][m][k][h][s].toString().substring(jadwal[d][m][k][h][s].toString().length() - 1).equals("1")) {
                                System.out.println(
                                        "jadwal[" + dosen[d] + "]" +
                                                "[" + matkulArray[m] + "]" +
                                                "[" + kelas[k] + "]" +
                                                "[" + hari[h] + "]" +
                                                "[" + sesi[s] + "] " +
                                                jadwal[d][m][k][h][s]
                                );

                                String sql = "INSERT INTO jadwal (no_dosen, no_matkul, no_kelas, no_hari, no_sesi, no_ruangan)"
                                        + "VALUES('" + (d + 1) + "', '" + (m + 1) + "', '" + (k + 1) + "', '" + (h + 1) + "', '" + (s + 1) + "', '" + id_unset + "')";
                                SQLHelper.insertDB(sql);
                            }
                        }
                    }
                }
            }
        }


        filterKapasitasRuangan();
        try {
            updateKategori();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        printSolverData();
    }

    private void filterKapasitasRuangan(){
        for (int m=0; m<matkulLength; m++){
            int able = 0;

            for (int r=0; r<ruanganLength; r++){

                int delta = Integer.parseInt(kapasitas[r]) - Integer.parseInt(jumlah[m]);
                int dos = (delta >= 0) ? 1:0;

                if (dos == 1) able++;

                if (r == 4){
                    String sql = "UPDATE jadwal SET tot_ruangan='" + able + "' WHERE no_matkul='" + (m+1) + "'";
                    SQLHelper.insertDB(sql);
                }
            }
        }
    }

    private void updateKategori() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM jadwal ORDER BY id_jadwal");

        int no_matkul;
        int kategori;

        while (rs.next()) {
            no_matkul = rs.getInt("no_matkul");

            ResultSet matkulResult = SQLHelper.getResultSet("SELECT * FROM matkul");

            while (matkulResult.next()){
                if (matkulResult.getInt("no") == no_matkul){
                    kategori = matkulResult.getInt("kategori");
                    String sql = "UPDATE jadwal SET kategori='" + kategori + "' WHERE no_matkul='" + no_matkul + "'";
                    SQLHelper.insertDB(sql);
                }
            }
        }

        rs.close();
    }

    private void printSolverData(){
        long[] time;
        long min;
        long sec;
        String bord = "====================";

        time = getTime(problem.getTimeCount());

        min = time[0];
        sec = time[1];

        System.out.println("\n" + bord);
        if (min > 0 || sec > 0) System.out.println("Time : " + min + " m " + sec + " s");
        else System.out.println("Time : " + sec + " s " + problem.getTimeCount() + " ms");
        System.out.println("Nodes : " + problem.getNodeCount());
        System.out.println("Backtracks : " + problem.getBackTrackCount());
        System.out.println(bord);
    }

    private long[] getTime(long ms){
        long[] time = new long[2];

        long min = (ms/1000)/60;
        long sec = (ms/1000)%60;

        time[0] = min;
        time[1] = sec;

        return time;
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