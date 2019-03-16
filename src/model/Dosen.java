package model;

public class Dosen {
    private String nip;
    private String inisial;
    private String nama;

    public Dosen() {
    }

    public Dosen(String nip, String inisial, String nama) {
        this.nip = nip;
        this.inisial = inisial;
        this.nama = nama;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getInisial() {
        return inisial;
    }

    public void setInisial(String inisial) {
        this.inisial = inisial;
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
                "nip='" + nip + '\'' +
                ", inisial='" + inisial + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }
}
