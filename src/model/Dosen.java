package model;

public class Dosen {
    private int no;
    private String nama;

    public Dosen() {
    }

    public Dosen(int no, String nama) {
        this.no = no;
        this.nama = nama;
    }

    public int getNo() {
        return no;
    }

    public String getNama() {
        return nama;
    }

    @Override
    public String toString() {
        return "Dosen{" +
                "no=" + no +
                ", nama='" + nama + '\'' +
                '}';
    }
}
