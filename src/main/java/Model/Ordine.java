package Model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.sql.Date;

public class Ordine {
    private ArrayList<Prodotto> list;
    private boolean evaso;
    private double prezzo;
    private Date dataEvasione;
    private Integer codice;
    private String via;
    private Integer cap;
    private Integer civico;
    private String citta;

    public Ordine() {
        this.list = new ArrayList<>();
        prezzo=0;
        this.evaso = false;
        this.dataEvasione = null;
        this.codice=null;
    }

    /**
     * Questo metodo verifica se un prodotto è contenuto in una lista di prodotti
     * @param l lista di prodotti
     * @param pr prodotto
     * @return true se pr è contenuto in l, altirmenti false
     */
    static boolean isPresent(ArrayList<Prodotto> l, Prodotto pr){
        System.out.println("-----(è presente il prodotto "+ pr.getArticolo().getTitolo()+" nel carrello?)----");
        boolean flag = false;
        if(l != null) //se la lista dei prodotti non è vuota
            for(Prodotto p: l)
                if(p.equals(pr)) //se il prodotto pr è nella lista
                    flag = true; //restituisco true
        System.out.println("-----("+flag+")----");
        return flag;
    }

    public void setList(ArrayList<Prodotto> list) {
        this.list = list;
    }

    public boolean isEvaso() {
        return evaso;
    }

    public void setEvaso(boolean evaso) {
        this.evaso = evaso;
    }

    public Date getDataEvasione() {
        return dataEvasione;
    }

    public void setDataEvasione(Date dataEvasione) {
        this.dataEvasione = dataEvasione;
    }

    public Integer getCodice() {
        return codice;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    /**
     * questo metodo fa il join tra il carrello del database, passato come primo parametro
     * e quello della sessione, su cui è chiamato il metodo, controllando se i prodotti sono
     * disponibili
     * @param p carrello del database
     * @param service lista dei vinili disponibili
     * @return il numero di vinili non più disponibili che vengono rimossi dal carrello
     */
    public int join(Ordine p, ListaVinili service){

        int count = 0; //inizializzo costante per contare i vinili non più disponibili
        ArrayList<Prodotto> op = p.getCarrello(); //prendo il carrello del DB
        if(codice == null && p.getCodice() != null)  //se il carrello della sessione no ha un codice, mentre quello del DB si
            codice=p.getCodice();   // setto il codice del carrello della sessione a quello del DB
        int i,j;
        if(list!=null && op!=null) { //se ho dei prodotti nel carrello della sessione e in quello del DB

            for (i = 0; i < this.list.size(); i++) { //per ogni prodotto nel carrello della sessione

                Prodotto prodottoLoc = list.get(i); //prendo il prodotto(1)

                if(service.isAvailable(list.get(i).getArticolo())) { //se il prodotto(1) è nella lista dei vinili disponibili

                    for (j = 0; j < op.size(); j++) { // per ogni prodotto(2) nel carrello del DB

                        Prodotto prodottoVar = op.get(j); //prendo il prodotto(2)
                        if (prodottoLoc.getArticolo().equals(prodottoVar.getArticolo())) { //se i due prodotti (sessione e DB) corrispondono

                            if (prodottoLoc.getQuantita() + prodottoVar.getQuantita() > service.getQuantitaVin(prodottoLoc.getArticolo())) { //se la somma delle due quantità è maggiore rispetto alla quantità totale disponibile del vinile

                                prodottoLoc.setQuantita(service.getQuantitaVin(prodottoLoc.getArticolo())); //setto la quantità del prodotto della sessione alla massima disponibilità
                            } else {

                                prodottoLoc.fusion(prodottoVar); //altrimenti unisco le due quantità
                            }
                        }
                    }
                } else { //se invece il prodotto non è nella lista dei vinili disponibili
                    count++; //incremento count
                    list.remove(i); //e lo rimuovo dal carrello
                }
            }

            if (p.getCarrello() != null) { //se il carrello del DB non è vuoto

                for (Prodotto pr : p.getCarrello()) { //per ogni prodotto nel carrello del DB

                    if (!isPresent(list, pr)) { //se il prodotto non è presente nei prodotti del carrello della sessione

                        if(service.isAvailable(pr.getArticolo())) { //se il prodotto è nella lista dei vinili disponibili
                            if (pr.getQuantita() > service.getQuantitaVin(pr.getArticolo())) // ma la sua quantità è maggiore rispetto a quella disponibile
                                pr.setQuantita(service.getQuantitaVin(pr.getArticolo())); //setto la sua quantità alla massima quantità disponibile
                            list.add(pr); //lo aggiungo il prodotto alla lista dei prodotti della sessione

                        } else{ //se non è disponibile incremento count
                            count++;
                        }
                    }
                }
                refreshCost(); //aggiorno il prezzo totale dell'ordine
            }
        }
        if(list==null && p.getCarrello()!=null){ //se il carrello della sessione è vuoto mentre quello del database no
            list = p.getCarrello(); //setto la liste dei prodotti della sessione a quella del DB
            refreshCost(); //aggiorno il prezzo totale
        }
        return count;
    }

    public void addProdotto(Prodotto p, ListaVinili service){
        boolean flag=false;
        if(list != null) {
            for (Prodotto tmp : list){
                if (tmp.getArticolo().equals(p.getArticolo())) {
                    if (tmp.getQuantita() + p.getQuantita() > service.getQuantitaVin(tmp.getArticolo()))
                        tmp.setQuantita(service.getQuantitaVin(tmp.getArticolo()));
                    else {
                        tmp.fusion(p);
                    }
                    flag = true;
                }
            }
            if (!flag) {
                list.add(p);
            }
            refreshCost();
        }
        else{
            list=new ArrayList<>();
            if(p.getQuantita()>service.getQuantitaVin(p.getArticolo()))
                p.setQuantita(service.getQuantitaVin(p.getArticolo()));
            list.add(p);
        }
    }

    public Prodotto getItem(Vinile v){
        for(Prodotto p: list)
            if(p.getArticolo().equals(v)){
                return p;
            }
        return null;
    }

    public ArrayList<Prodotto> getCarrello(){
        return list;
    }

    public int getNumItem(){
        return this.list.size();
    }

    public double getPrezzo() {
        DecimalFormat dr=new DecimalFormat("0.00");
        return Double.parseDouble(dr.format(prezzo).replace(",","."));
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    /**
     * questo metodo aggiorna il prezzo totale dell'ordine
     */
    public void refreshCost(){
        double prezzo = 0;
        for(Prodotto p: list)
            prezzo += p.getPrezzo();
        this.prezzo = prezzo;
    }

    public ArrayList<Vinile> check(){
        ArrayList<Vinile> ret=new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            Prodotto p = list.get(i);
            if (p.getQuantita() <= 0) {
                ret.add(list.get(i).getArticolo());
                list.remove(i);
            }
        }
        if(ret.size()>0)
            return ret;
        return null;
    }

    public String toString(){
        String x="";
        x+=" evaso:"+evaso+" prezzo:"+prezzo+" data:"+dataEvasione+" codice:"+codice+" ";
        for(Prodotto p: list)
            x+=p.toString();
        return x;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public Integer getCivico() {
        return civico;
    }

    public void setCivico(Integer civico) {
        this.civico = civico;
    }

    public String getCitta() {
        return citta;
    }

    public void toPrint(){
        if(list!=null){
            for(Prodotto p :list) {
                System.out.println(" " + p.getArticolo().getTitolo() + " " + p.getQuantita());
            }
        }
    }

    public void setCitta(String citta) {
        this.citta=citta;
    }
}


