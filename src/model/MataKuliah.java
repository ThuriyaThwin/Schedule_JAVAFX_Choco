package model;

public class MataKuliah {
    private String no;
    private String nama;
    private String sks;
    private String jumlah;
    private String kategori;

    public MataKuliah(String no, String nama, String sks, String jumlah, String kategori) {
        this.no = no;
        this.nama = nama;
        this.sks = sks;
        this.jumlah = jumlah;
        this.kategori = kategori;
    }

    public String getNo() {
        return no;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getSks() {
        return sks;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setSks(String sks) {
        this.sks = sks;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    @Override
    public String toString() {
        return "MataKuliah{" +
                "no='" + no + '\'' +
                ", nama='" + nama + '\'' +
                ", sks='" + sks + '\'' +
                ", jumlah='" + jumlah + '\'' +
                '}';
    }
}
