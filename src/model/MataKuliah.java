package model;

public class MataKuliah {
    private String no;
    private String nama;
    private String sks;
    private String jumlah;

    public MataKuliah(String no, String nama, String sks, String jumlah) {
        this.no = no;
        this.nama = nama;
        this.sks = sks;
        this.jumlah = jumlah;
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
