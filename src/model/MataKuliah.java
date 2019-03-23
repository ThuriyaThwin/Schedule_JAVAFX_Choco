package model;

public class MataKuliah {
    private String no;
    private String nama;
    private String sks;
    private String jumlah;

    public MataKuliah() {
    }

    public MataKuliah(String no, String nama, String sks, String jumlah) {
        this.no = no;
        this.nama = nama;
        this.sks = sks;
        this.jumlah = jumlah;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public void setSks(String sks) {
        this.sks = sks;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
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
