package model;

public class Ruangan {

    private String id_ruangan;
    private String nama;
    private String kapasitas;
    private String kategori;
    private String status;

    public Ruangan() {
    }

    public Ruangan(String id_ruangan, String nama, String kapasitas, String kategori) {
        this.id_ruangan = id_ruangan;
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.kategori = kategori;
    }

    public Ruangan(String id_ruangan, String nama, String kapasitas, String kategori, String status) {
        this.id_ruangan = id_ruangan;
        this.nama = nama;
        this.kapasitas = kapasitas;
        this.kategori = kategori;
        this.status = status;
    }

    public String getId_ruangan() {
        return id_ruangan;
    }

    public void setId_ruangan(String id_ruangan) {
        this.id_ruangan = id_ruangan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ruangan{" +
                "id_ruangan='" + id_ruangan + '\'' +
                ", nama='" + nama + '\'' +
                ", kapasitas='" + kapasitas + '\'' +
                ", kategori='" + kategori + '\'' +
                '}';
    }
}
