package model;

public class Dosen {
    private String no;
    private String nama;

    public Dosen() {
    }

    public Dosen(String no, String nama) {
        this.no = no;
        this.nama = nama;
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

    @Override
    public String toString() {
        return "Dosen{" +
                "no='" + no + '\'' +
                ", nama='" + nama + '\'' +
                '}';
    }
}
