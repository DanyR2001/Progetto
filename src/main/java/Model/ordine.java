package Model;

import jakarta.servlet.http.HttpSession;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.sql.Date;

public class ordine {
    private ArrayList<prodotto> list;
    private boolean evaso;
    private double prezzo;
    private Date dataEvasione;
    private Integer codice;
    private String via;
    private Integer cap;
    private Integer civico;

    public ordine() {
        this.list = new ArrayList<>();
        prezzo=0;
        this.evaso = false;
        this.dataEvasione = null;
        this.codice=null;
    }

    static boolean isPresent(ArrayList<prodotto> l,prodotto pr){
        boolean flag=false;
        if(l!=null)
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

    public void join(ordine p,listaVinili service){
        ArrayList<prodotto> op=p.getCarrello();
        if(codice==null&&p.getCodice()!=null)
            codice=p.getCodice();
        int i=0,j=0;
        if(list!=null&&p.getCarrello()!=null) {
            System.out.println("join");
            for (i = 0; i < this.list.size(); i++) {
                System.out.println("join 1");
                prodotto prodottoLoc = list.get(i);
                if (op != null)
                    System.out.println("join 2");
                if(service.isAvable(list.get(i).getArticolo())) {
                    System.out.println("join 3");
                    for (j = 0; j < op.size(); j++) {
                        System.out.println("join 4");
                        prodotto prodottoVar = op.get(j);
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
                    list.remove(i);
                    }
            }
            if (p.getCarrello() != null) {
                System.out.println("join 9");
                for (prodotto pr : p.getCarrello()) {
                    System.out.println("join 10");
                    if (!isPresent(list, pr)) {
                        System.out.println("join 11");
                        if(service.isAvable(pr.getArticolo())) {
                            if (pr.getQuantita() > service.getQuantitaVin(pr.getArticolo()))
                                pr.setQuantita(service.getQuantitaVin(pr.getArticolo()));
                            list.add(pr);
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
    }

    public void addProdotto(prodotto p,listaVinili service){
        boolean flag=false;
        if(list != null) {
            for (prodotto tmp : list){
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

    public prodotto getItem(vinile v){
        for(prodotto p: list)
            if(p.getArticolo().equals(v)){
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
        DecimalFormat dr=new DecimalFormat("0.00");
        return Double.parseDouble(dr.format(prezzo).replace(",","."));
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
            for(prodotto p :list) {
                System.out.println(" " + p.getArticolo().getTitolo() + " " + p.getQuantita());
            }
        }
    }
}


