package Model;

public class prodotto {
    private vinile articolo;
    private int quantita;
    private double prezzo;

    public prodotto(){}

    public prodotto(vinile v){
        articolo=v;
    }
    public vinile getArticolo() {
        return articolo;
    }

    public void setArticolo(vinile atricolo) {
        this.articolo = atricolo;
    }

    private double cal_cost(){
        return this.articolo.getPrezzo()*this.quantita;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantia) {
        this.quantita = quantia;
        this.prezzo=cal_cost();
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public boolean equals(prodotto p){
        if(this.articolo.equals(p.getArticolo()))
            return true;
        return false;
    }

    public boolean esattamenteLoStesso(prodotto p){
        if(this.articolo.equals(p.getArticolo())&&quantita==p.getQuantita()&&prezzo==p.getPrezzo())
            return true;
        return false;
    }

    public void fusion(prodotto p){
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
