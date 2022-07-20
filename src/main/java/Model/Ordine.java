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

    public Ordine() {
        this.list = new ArrayList<>();
        prezzo=0;
        this.evaso = false;
        this.dataEvasione = null;
        this.codice=null;
    }

    static boolean isPresent(ArrayList<Prodotto> l, Prodotto pr){
        boolean flag = false;
        if(l!=null)
            for(Prodotto p: l)
                if(p.equals(pr))
                    flag = true;
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

    public int join(Ordine p, ListaVinili service){

        int count = 0;
        ArrayList<Prodotto> op = p.getCarrello();
        if(codice==null&&p.getCodice()!=null)
            codice=p.getCodice();
        int i,j;
        if(list!=null&&op!=null) {
            System.out.println("join");
            for (i = 0; i < this.list.size(); i++) {
                System.out.println("join 1");
                Prodotto prodottoLoc = list.get(i);
                System.out.println("join 2");
                if(service.isAvable(list.get(i).getArticolo())) {
                    System.out.println("join 3");
                    for (j = 0; j < op.size(); j++) {
                        System.out.println("join 4");
                        Prodotto prodottoVar = op.get(j);
                            if (prodottoLoc.getArticolo().equals(prodottoVar.getArticolo())) {
                                System.out.println("join 5");
                                System.out.println("loc q " + prodottoLoc.getQuantita() + " var q " + prodottoVar.getQuantita());
                                if (prodottoLoc.getQuantita() + prodottoVar.getQuantita() > service.getQuantitaVin(prodottoLoc.getArticolo())) {
                                    System.out.println("join 6");
                                    prodottoLoc.setQuantita(service.getQuantitaVin(prodottoLoc.getArticolo()));
                                } else {
                                    System.out.println("join 7");
                                    prodottoLoc.fusion(prodottoVar);
                                }
                            }
                        }
                    }
                else{
                    System.out.println("join 8");
                    count++;
                    list.remove(i);
                    }
            }
            if (p.getCarrello() != null) {
                System.out.println("join 9");
                for (Prodotto pr : p.getCarrello()) {
                    System.out.println("join 10");
                    if (!isPresent(list, pr)) {
                        System.out.println("join 11");
                        if(service.isAvable(pr.getArticolo())) {
                            if (pr.getQuantita() > service.getQuantitaVin(pr.getArticolo()))
                                pr.setQuantita(service.getQuantitaVin(pr.getArticolo()));
                            list.add(pr);
                        }
                        else{
                            count++;
                        }
                    }
                }
                refreshCost();
            }
        }
        if(list==null&&p.getCarrello()!=null){
            System.out.println("join 12");
            list=p.getCarrello();
            refreshCost();
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

    public void refreshCost(){
        double prezzo=0;
        for(Prodotto p: list)
            prezzo+=p.getPrezzo();
        this.prezzo=prezzo;
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

    public void toPrint(){
        if(list!=null){
            for(Prodotto p :list) {
                System.out.println(" " + p.getArticolo().getTitolo() + " " + p.getQuantita());
            }
        }
    }
}


