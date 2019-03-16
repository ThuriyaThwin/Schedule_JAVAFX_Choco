package model;

public class MataKuliah {
    private String id_mata_kuliah;
    private String nama;
    private String kode;
    private String sks;

    public MataKuliah() {
    }

    public MataKuliah(String id_mata_kuliah, String nama, String kode, String sks) {
        this.id_mata_kuliah = id_mata_kuliah;
        this.nama = nama;
        this.kode = kode;
        this.sks = sks;
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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getSks() {
        return sks;
    }

    public void setSks(String sks) {
        this.sks = sks;
    }

    @Override
    public String toString() {
        return "MataKuliah{" +
                "id_mata_kuliah='" + id_mata_kuliah + '\'' +
                ", nama='" + nama + '\'' +
                ", kode='" + kode + '\'' +
                ", sks='" + sks + '\'' +
                '}';
    }
}
