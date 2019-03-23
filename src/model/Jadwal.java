package model;

public class Jadwal {

    private String dosen;
    private String dosenId;
    private String mataKuliah;
    private String mataKuliahId;
    private String kelas;
    private String kelasId;

    public Jadwal() {
    }

    public Jadwal(String dosen, String dosenId, String mataKuliah, String mataKuliahId, String kelas, String kelasId) {
        this.dosen = dosen;
        this.dosenId = dosenId;
        this.mataKuliah = mataKuliah;
        this.mataKuliahId = mataKuliahId;
        this.kelas = kelas;
        this.kelasId = kelasId;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getDosenId() {
        return dosenId;
    }

    public void setDosenId(String dosenId) {
        this.dosenId = dosenId;
    }

    public String getMataKuliah() {
        return mataKuliah;
    }

    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }

    public String getMataKuliahId() {
        return mataKuliahId;
    }

    public void setMataKuliahId(String mataKuliahId) {
        this.mataKuliahId = mataKuliahId;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getKelasId() {
        return kelasId;
    }

    public void setKelasId(String kelasId) {
        this.kelasId = kelasId;
    }

    @Override
    public String toString() {
        return "Jadwal{" +
                "dosen='" + dosen + '\'' +
                ", dosenId='" + dosenId + '\'' +
                ", mataKuliah='" + mataKuliah + '\'' +
                ", mataKuliahId='" + mataKuliahId + '\'' +
                ", kelas='" + kelas + '\'' +
                ", kelasId='" + kelasId + '\'' +
                '}';
    }
}
