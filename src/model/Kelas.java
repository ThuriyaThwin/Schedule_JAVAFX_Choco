package model;

public class Kelas {
    private int id_kelas;
    private String nama;
    private String prodi;
    private String jumlah;

    public Kelas() {
    }

    public Kelas(int id_kelas, String nama, String prodi, String jumlah) {
        this.id_kelas = id_kelas;
        this.nama = nama;
        this.prodi = prodi;
        this.jumlah = jumlah;
    }

    public int getId_kelas() {
        return id_kelas;
    }

    public void setId_kelas(int id_kelas) {
        this.id_kelas = id_kelas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    @Override
    public String toString() {
        return "Kelas{" +
                "id_kelas=" + id_kelas +
                ", nama='" + nama + '\'' +
                ", prodi='" + prodi + '\'' +
                ", jumlah='" + jumlah + '\'' +
                '}';
    }
}
