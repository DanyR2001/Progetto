package Model;

import java.util.ArrayList;

public class Vinile {
    private int PK;
    private String titolo;
    private double prezzo;
    private String url;
    private String artista;
    private ArrayList<Tag> Tags;



    public Vinile(int PK, String titolo, double prezzo, String url, String artista) {
        this.PK = PK;
        this.titolo = titolo;
        this.prezzo = prezzo;
        this.url = url;
        this.artista = artista;
    }

    public Vinile(){
    }

    public int getPK() {
        return PK;
    }

    public void setPK(int PK) {
        this.PK = PK;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vinile vinile = (Vinile) o;
        return PK == vinile.PK && titolo.equals(vinile.titolo) && url.equals(vinile.url) && artista.equals(vinile.artista);
    }

    public ArrayList<Tag> getTags() {
        return Tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        Tags = tags;
    }

}
