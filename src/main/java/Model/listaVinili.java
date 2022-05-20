package Model;

import java.util.ArrayList;

public class listaVinili {
    ArrayList<vinile> list;
    ArrayList<Integer> size;

    public listaVinili(){
        listaDisponibiliDAO service=new listaDisponibiliDAO();
        list=service.getDisponibili();
        size=new ArrayList<Integer>();
        for(vinile v: list)
            size.add(v.getQuantita());
    }

    public void aggiorna(vinile v, int quatita){
        for(vinile v1: list)
            if(v1.equals(v))
                if(v1.getQuantita()>=quatita)
                    v1.removeNumItem(quatita);
    }

    public void setDisponibili(vinile v,int quntita){
        for(vinile v1: list)
            if(v1.equals(v))
                v1.setQuantita(quntita);
    }
    public vinile findVinilieFromId(int id){
        if(list.size()>0)
            for(vinile v: list)
                if(v.getPK()==id)
                    return v;
        return null;
    }

    public ArrayList<vinile> getAllVinil(){
        if(list.size()>0)
            return list;
        return null;
    }

    public ArrayList<vinile> getAvableVinil(){
        ArrayList<vinile> disp=null;
        if(list.size()>0){
            disp=new ArrayList<>();
            for(vinile v: list)
                if(v.getQuantita()>0)
                    disp.add(v);
        }
        return disp;
    }

    public boolean isAvable(vinile v){
        if(list.size()>0)
            for(vinile a: list)
                if(v.equals(a))
                    if(a.getQuantita()>0)
                        return true;
                    else
                        return false;
        return false;
    }

    public int numDispVinil(vinile v){
        int size=0;
        if(list.size()>0)
            for(vinile a: list)
                if(v.equals(a))
                    size=a.getQuantita();
        return size;
    }

    public Integer getMaxDisp(int index){
        return size.get(index);
    }
}
