package model;

public class MataKuliah {
    private String id_mata_kuliah;
    private String nama;
    private String dosen;
    private String sks;
    private String kelas;

    public MataKuliah() {
    }

    public MataKuliah(String id_mata_kuliah, String nama, String dosen, String sks, String kelas) {
        this.id_mata_kuliah = id_mata_kuliah;
        this.nama = nama;
        this.dosen = dosen;
        this.sks = sks;
        this.kelas = kelas;
    }

    public String getId_mata_kuliah() {
        return id_mata_kuliah;
    }

    public void setId_mata_kuliah(String id_mata_kuliah) {
        this.id_mata_kuliah = id_mata_kuliah;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDosen() {
        return dosen;
    }

    public void setDosen(String dosen) {
        this.dosen = dosen;
    }

    public String getSks() {
        return sks;
    }

    public void setSks(String sks) {
        this.sks = sks;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    @Override
    public String toString() {
        return "MataKuliah{" +
                "id_mata_kuliah='" + id_mata_kuliah + '\'' +
                ", nama='" + nama + '\'' +
                ", dosen='" + dosen + '\'' +
                ", sks='" + sks + '\'' +
                ", kelas='" + kelas + '\'' +
                '}';
    }
}
