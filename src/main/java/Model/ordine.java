package Model;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;

public class ordine {
    private ArrayList<prodotto> list;
    private boolean evaso;
    private double prezzo;
    private Date dataEvasione;
    private Integer codice;

    public ordine() {
        this.list = new ArrayList<>();
        prezzo=0;
        this.evaso = false;
        this.dataEvasione = null;
        this.codice=null;
    }

    static boolean isPresent(ArrayList<prodotto> l,prodotto pr){
        boolean flag=false;
        for(prodotto p: l)
            if(p.equals(pr))
                flag=true;
        return flag;
    }

    public boolean inPresentSameArticle(prodotto pr){
        boolean flag=false;
        for(prodotto p: this.list)
            if(pr.esattamenteLoStesso(p))
                flag=true;
        return flag;
    }

    public void setList(ArrayList<prodotto> list) {
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

    public void join(ordine p){
        ArrayList<prodotto> op=p.getCarrello();
        int i=0,j=0;
        for(i=0;i<this.list.size();i++)
            for(j=0;j<op.size();j++)
                if(this.list.get(i).getArticolo().equals(op.get(j).getArticolo())) {
                    this.list.get(i).fusion(op.get(j));
                }
        for(prodotto pr: p.getCarrello())
            if(!isPresent(list,pr))
                list.add(pr);
        refreshCost();
    }

    public void addProdotto(prodotto p){
        boolean flag=false;
        for(prodotto tmp:list)
            if(tmp.getArticolo().equals(p.getArticolo())) {
                tmp.fusion(p);
                flag=true;
            }
        if(!flag) {
            list.add(p);
        }
        refreshCost();
    }

    public prodotto getItem(prodotto v){
        for(prodotto p: list)
            if(p.getArticolo().equals(v.getArticolo())){
                return p;
            }
        return null;
    }

    public ArrayList<prodotto> getCarrello(){
        return list;
    }

    public int getNumItem(){
        return this.list.size();
    }


    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public void refreshCost(){
        double prezzo=0;
        for(prodotto p: list)
            prezzo+=p.getPrezzo();
        this.prezzo=prezzo;
    }

    public void check(){
        for(int i=0;i<list.size();i++) {
            prodotto p = list.get(i);
            if (p.getQuantita() <= 0)
                list.remove(i);
        }
    }

    public String toString(){
        String x="";
        x+=" evaso:"+evaso+" prezzo:"+prezzo+" data:"+dataEvasione+" codice:"+codice+" ";
        for(prodotto p: list)
            x+=p.toString();
        return x;
    }
}


