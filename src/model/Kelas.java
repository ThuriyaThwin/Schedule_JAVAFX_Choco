package model;

public class Kelas {
    private int no;
    private String nama;
    private String jumlah;

    public Kelas() {
    }

    public Kelas(int no, String nama, String jumlah) {
        this.no = no;
        this.nama = nama;
        this.jumlah = jumlah;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }
}
