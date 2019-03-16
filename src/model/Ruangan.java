package model;

public class Ruangan {

    private String id_ruangan;
    private String nama;
    private String jenis;
    private String kapasitas;

    public Ruangan() {
    }

    public Ruangan(String id_ruangan, String nama, String jenis, String kapasitas) {
        this.id_ruangan = id_ruangan;
        this.nama = nama;
        this.jenis = jenis;
        this.kapasitas = kapasitas;
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

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(String kapasitas) {
        this.kapasitas = kapasitas;
    }

    @Override
    public String toString() {
        return "Ruangan{" +
                "id_ruangan='" + id_ruangan + '\'' +
                ", nama='" + nama + '\'' +
                ", jenis='" + jenis + '\'' +
                ", kapasitas='" + kapasitas + '\'' +
                '}';
    }
}
