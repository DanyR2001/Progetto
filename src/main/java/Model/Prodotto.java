package Model;

import java.text.DecimalFormat;

public class Prodotto {
    private Vinile articolo;
    private int quantita;
    private double prezzo;

    public Prodotto(){}

    public Prodotto(Vinile v){
        articolo=v;
    }
    public Vinile getArticolo() {
        return articolo;
    }

    public void setArticolo(Vinile articolo) {
        this.articolo = articolo;
    }

    private double cal_cost(){
        return this.articolo.getPrezzo()*this.quantita;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
        this.prezzo=cal_cost();
    }

    public void setQuantitaOldOrder(int quantita) {
        this.quantita = quantita;
    }

    public double getPrezzo() {
        DecimalFormat dr=new DecimalFormat("0.00");
        return Double.parseDouble(dr.format(prezzo).replace(",","."));
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public boolean equals(Prodotto p){
        if(this.articolo.equals(p.getArticolo()))
            return true;
        return false;
    }

    public void fusion(Prodotto p){
        if(p.getArticolo().getTitolo().equals(this.articolo.getTitolo())) {
            this.quantita+=p.getQuantita();
            this.prezzo=cal_cost();
        }
    }

    @Override
    public String toString() {
        return "prodotto{" +
                "articolo=" + articolo +
                ", quantita=" + quantita +
                ", prezzo=" + prezzo +
                '}';
    }
}
