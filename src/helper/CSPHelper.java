package helper;

import choco.cp.solver.CPSolver;
import choco.kernel.solver.Solver;
import choco.kernel.solver.SolverException;
import choco.kernel.solver.constraints.integer.IntExp;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CSPHelper {
    private String[] dosen;
    private String[] hari;
    private String[] sesi;
    private String[] matkul;
    private String[] kelas;
    private String[][] dosen_matkul;
    private String[][] matkul_kelas;
    private String[] sks;

    private Solver problem;

    private CSPHelper() {
        problem = new CPSolver();
    }

    private void fillDosen()  throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM dosen ORDER BY no");

        dosen = new String[getRowCount(rs)];
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
        int i = 0;

        while (rs.next()) {
            sesi[i] = rs.getString("nama");
            i++;
        }

        rs.close();
    }

    private void fillMatkul() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM matkul ORDER BY no");

        matkul = new String[getRowCount(rs)];
        sks = new String[getRowCount(rs)];
//        jumlah = new String[getRowCount(rs)];
        int i = 0;

        while (rs.next()) {
            matkul[i] = rs.getString("nama");
            sks[i] = rs.getString("sks");
//            jumlah[i] = rs.getString("jumlah");
            i++;
        }

        rs.close();
    }

    private void fillKelas() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM kelas ORDER BY no");

        kelas = new String[getRowCount(rs)];
        int i = 0;

        while (rs.next()) {
            kelas[i] = rs.getString("nama");
            i++;
        }

        rs.close();
    }

    private void fillMatkulKelas() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM matkul_kelas ORDER BY no_matkul, no_kelas");

        matkul_kelas = new String[matkul.length][kelas.length];

        while (rs.next()) {
            int no_matkul = rs.getInt("no_matkul");
            int no_kelas = rs.getInt("no_kelas");

            matkul_kelas[no_matkul-1][no_kelas-1] = rs.getString("nilai");
        }

        rs.close();
    }

    private void fillDosenMatkul() throws SQLException {
        ResultSet rs = SQLHelper.getResultSet("SELECT * FROM dosen_matkul ORDER BY no_dosen, no_matkul");

        dosen_matkul = new String[dosen.length][matkul.length];

        while (rs.next()) {
            int no_dosen = rs.getInt("no_dosen");
            int no_matkul = rs.getInt("no_matkul");

            dosen_matkul[no_dosen-1][no_matkul-1] = rs.getString("nilai");
        }

        rs.close();
    }

    private void generateSolusi() {
        IntExp[][][][][] x = new IntExp[dosen.length][hari.length][sesi.length][matkul.length][kelas.length];

        for (int iDosen = 0; iDosen < dosen.length; iDosen++) {
            for (int iHari = 0; iHari < hari.length; iHari++) {
                for (int iSesi = 0; iSesi < sesi.length; iSesi++) {
                    for (int iMatkul = 0; iMatkul < matkul.length; iMatkul++) {
                        for (int iKelas = 0; iKelas < kelas.length; iKelas++) {
                            x[iDosen][iHari][iSesi][iMatkul][iKelas] = problem.createEnumIntVar(
                                    "x[" + iDosen + "]" +
                                            "[" + iHari + "]" +
                                            "[" + iSesi + "]" +
                                            "[" + iMatkul + "]" +
                                            "[" + iKelas + "]" , 0, 1);
                        }
                    }
                }
            }
        }

        for (int iHari = 0; iHari < hari.length; iHari++) {
            for (int iSesi = 0; iSesi < sesi.length; iSesi++) {
                for (int iMatkul = 0; iMatkul < matkul.length; iMatkul++) {
                    for (int iKelas = 0; iKelas < kelas.length; iKelas++) {
                        for (int iDosen = 0; iDosen < dosen.length; iDosen++) {
                            problem.post(problem.geq(x[iDosen][iHari][iSesi][iMatkul][iKelas], 0));
                            problem.post(problem.leq(x[iDosen][iHari][iSesi][iMatkul][iKelas], 1));
                            problem.post(problem.leq(x[iDosen][iHari][iSesi][iMatkul][iKelas], Integer.parseInt(matkul_kelas[iMatkul][iKelas])));
                            problem.post(problem.leq(x[iDosen][iHari][iSesi][iMatkul][iKelas], Integer.parseInt(dosen_matkul[iDosen][iMatkul])));
                        }
                    }
                }
            }
        }

        IntExp plus;
        for (int iMatkul=0; iMatkul < matkul.length; iMatkul++) {
            plus = null;

            for (int iDosen=0; iDosen < dosen.length; iDosen++) {
                for (int iHari=0; iHari < hari.length; iHari++) {
                    for (int iSesi=0; iSesi < sesi.length; iSesi++) {
                        for (int iKelas = 0; iKelas < kelas.length; iKelas++) {
                            if (plus == null)
                                plus = x[iDosen][iHari][iSesi][iMatkul][iKelas];
                            else
                                plus = problem.plus(plus, x[iDosen][iHari][iSesi][iMatkul][iKelas]);
                        }
                    }
                }
            }

            problem.post(problem.eq(plus, Integer.parseInt(sks[iMatkul])));
        }

        IntExp plus1;
        for (int iDosen=0; iDosen < dosen.length; iDosen++) {
            for (int iHari=0; iHari < hari.length; iHari++) {
                for (int iSesi=0; iSesi < sesi.length; iSesi++) {
                    plus1 = null;

                    for (int iMatkul=0; iMatkul < matkul.length; iMatkul++) {
                        for (int iKelas=0; iKelas < kelas.length; iKelas++) {
                            if (plus1 == null)
                                plus1 = x[iDosen][iHari][iSesi][iMatkul][iKelas];
                            else
                                plus1 = problem.plus(plus1, x[iDosen][iHari][iSesi][iMatkul][iKelas]);
                        }
                    }

                    problem.post(problem.leq(plus1, 1));
                }
            }
        }

        IntExp plus2;
        for (int iHari=0; iHari < hari.length; iHari++) {
            for (int iSesi=0; iSesi < sesi.length; iSesi++) {
                for (int iKelas=0; iKelas < kelas.length; iKelas++) {
                    plus2 = null;

                    for (int iDosen=0; iDosen < dosen.length; iDosen++) {
                        for (int iMatkul=0; iMatkul < matkul.length; iMatkul++) {
                            if (plus2 == null)
                                plus2 = x[iDosen][iHari][iSesi][iMatkul][iKelas];
                            else
                                plus2 = problem.plus(plus2, x[iDosen][iHari][iSesi][iMatkul][iKelas]);
                        }
                    }

                    problem.post(problem.leq(plus2, 1));
                }
            }
        }

        try {
            problem.solve();
        }
        catch (SolverException ce) {
            System.out.println("Error [SolverException]: " + ce);
            System.exit(0);
        }


        for (int iDosen = 0; iDosen < dosen.length; iDosen++) {
            for (int iHari = 0; iHari < hari.length; iHari++) {
                for (int iSesi = 0; iSesi < sesi.length; iSesi++) {
                    for (int iMatkul = 0; iMatkul < matkul.length; iMatkul++) {
                        for (int iKelas = 0; iKelas < kelas.length; iKelas++) {
                            if (x[iDosen][iHari][iSesi][iMatkul][iKelas].toString().substring(x[iDosen][iHari][iSesi][iMatkul][iKelas].toString().length() - 1).equals("1")) {
                                System.out.println(
                                        "x[" + dosen[iDosen] + "]" +
                                                "[" + hari[iHari] + "]" +
                                                "[" + sesi[iSesi] + "]" +
                                                "[" + matkul[iMatkul] + "]" +
                                                "[" + kelas[iKelas] + "] = " +
                                                x[iDosen][iHari][iSesi][iMatkul][iKelas]);

                                String sql = "INSERT INTO jadwal (no_dosen, no_hari, no_sesi, no_matkul, no_kelas)"
                                        + "VALUES('" + (iDosen+1) + "', '" + (iHari+1) + "', '" + (iSesi+1) + "', '" + (iMatkul+1) + "', '" + (iKelas+1) + "')";
                                SQLHelper.insertDB(sql);
                            }
                        }
                    }
                }
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

    public static void main() {
        CSPHelper def = new CSPHelper();

        try {
            def.fillDosen();
            System.out.println("Sukses 1");
            def.fillHari();
            System.out.println("Sukses 2");
            def.fillSesi();
            System.out.println("Sukses 3");
            def.fillMatkul();
            System.out.println("Sukses 4");
            def.fillKelas();
            System.out.println("Sukses 5");
            def.fillDosenMatkul();
            System.out.println("Sukses 6");
            def.fillMatkulKelas();
            System.out.println("Sukses 7");
        }
        catch (SQLException se) {
            System.out.println("Error [SQLException]: " + se.getMessage());
        }
        catch (Exception ex) {
            System.out.println("Error [Exception]: " + ex.toString());
        }

        def.generateSolusi();
    }
}
