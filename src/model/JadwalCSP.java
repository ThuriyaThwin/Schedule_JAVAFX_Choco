package model;

public class JadwalCSP {
    private int id_jadwal;
    private int no_dosen;
    private String dosen;
    private int no_hari;
    private String hari;
    private int no_sesi;
    private String sesi;
    private int no_matkul;
    private String matkul;
    private int no_kelas;
    private String kelas;

    public JadwalCSP(int id_jadwal, int no_dosen, String dosen, int no_hari, String hari, int no_sesi, String sesi, int no_matkul, String matkul, int no_kelas, String kelas) {
        this.id_jadwal = id_jadwal;
        this.no_dosen = no_dosen;
        this.dosen = dosen;
        this.no_hari = no_hari;
        this.hari = hari;
        this.no_sesi = no_sesi;
        this.sesi = sesi;
        this.no_matkul = no_matkul;
        this.matkul = matkul;
        this.no_kelas = no_kelas;
        this.kelas = kelas;
    }

    public int getId_jadwal() {
        return id_jadwal;
    }

    public int getNo_dosen() {
        return no_dosen;
    }

    public String getDosen() {
        return dosen;
    }

    public int getNo_hari() {
        return no_hari;
    }

    public String getHari() {
        return hari;
    }

    public int getNo_sesi() {
        return no_sesi;
    }

    public String getSesi() {
        return sesi;
    }

    public int getNo_matkul() {
        return no_matkul;
    }

    public String getMatkul() {
        return matkul;
    }

    public int getNo_kelas() {
        return no_kelas;
    }

    public String getKelas() {
        return kelas;
    }

    @Override
    public String toString() {
        return "JadwalCSP{" +
                "id_jadwal=" + id_jadwal +
                ", no_dosen=" + no_dosen +
                ", dosen='" + dosen + '\'' +
                ", no_hari=" + no_hari +
                ", hari='" + hari + '\'' +
                ", no_sesi=" + no_sesi +
                ", sesi='" + sesi + '\'' +
                ", no_matkul=" + no_matkul +
                ", matkul='" + matkul + '\'' +
                ", no_kelas=" + no_kelas +
                ", kelas='" + kelas + '\'' +
                '}';
    }
}
