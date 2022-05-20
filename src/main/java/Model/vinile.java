package Model;

import java.util.Objects;

public class vinile {
    private int PK;
    private String Titolo;
    private double prezzo;
    private int quantita;
    private String url;
    private String artista;

    public vinile(int PK, String titolo, double prezzo, int quantita, String url, String artista) {
        this.PK = PK;
        Titolo = titolo;
        this.prezzo = prezzo;
        this.quantita = quantita;
        this.url = url;
        this.artista = artista;
    }

    public int getPK() {
        return PK;
    }

    public void setPK(int PK) {
        this.PK = PK;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
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

    public boolean equals(vinile o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        vinile vinile = (vinile) o;
        return PK == vinile.PK && Titolo.equals(vinile.Titolo) && url.equals(vinile.url) && artista.equals(vinile.artista);
    }

    public void removeNumItem(int quatita) {
        this.quantita-=quatita;
    }
}
