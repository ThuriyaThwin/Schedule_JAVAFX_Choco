package model;

public class Dosen {
    private String nip;
    private String nama;

    public Dosen() {
    }

    public Dosen(String nip, String nama) {
        this.nip = nip;
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "Dosen{" +
                "nip=" + nip +
                ", nama='" + nama + '\'' +
                '}';
    }
}
