package Model;

import java.util.ArrayList;
import java.util.List;

public class listaVinili {
    ArrayList<vinile> list;
    ArrayList<Integer> size;

    public listaVinili(){
        list = new ArrayList<>();
        size= new ArrayList<>();
    }

    /*public void aggiorna(vinile v, int quatita){
        for(vinile v1: list)
            if(v1.equals(v))
                if(v1.getQuantita()>=quatita)
                    v1.removeNumItem(quatita);
    }*/

    /*public void setDisponibili(vinile v,int quntita){
        for(vinile v1: list)
            if(v1.equals(v))
                v1.setQuantita(quntita);
    }*/

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

    public listaVinili getAvableVinil(){
        listaVinili ret=null;
        if(list.size()>0){
            ret=new listaVinili();
            for(int i=0;i<list.size();i++)
                if(size.get(i)>0) {
                    ret.add(list.get(i),size.get(i));
                }
        }
        return ret;
    }

    public void add(vinile v,Integer disponibilita){
        list.add(v);
        size.add(disponibilita);
    }

    public boolean isAvable(vinile v){
        if(list.size()>0)
            for (int i=0;i<list.size();i++) {
                System.out.println("ciao 2");
                if (list.get(i).equals(v)) {
                    System.out.println("ciao 1");
                    if (size.get(i) > 0) {
                        System.out.println("ciao");
                        return true;
                    }
                }
            }
        return false;
    }

    public Integer getQuantitaVin(vinile v){
        if(list.size()>0)
            for (int i=0;i<list.size();i++)
                if(list.get(i).equals(v))
                    return size.get(i);
        return -1;
    }

    public void setQuantitaVin(vinile v,Integer quantita){
        if(list.size()>0)
            for (int i=0;i<list.size();i++)
                if(list.get(i).equals(v)){
                    size.remove(i);
                    size.add(i,quantita);
                }
    }



    /*public int numDispVinil(vinile v){
        int size=0;
        if(list.size()>0)
            for(vinile a: list)
                if(v.equals(a))
                    size=a.getQuantita();
        return size;
    }*/
    public vinile get(int index){
        return list.get(index);
    }
    public int size(){
        return list.size();
    }
    public Integer getMaxDisp(int index){
        return size.get(index);
    }

    public Integer getMaxDispId(int id){
        for(int i=0;i<list.size();i++)
            if(list.get(i).getPK() == id)
                return size.get(i);
        return 0;
    }

    public List<vinile> getTitleContein(String name){
        List<vinile> ret=new ArrayList<>();
        for(int i=0;i<list.size()&&name.length()>0;i++){
            vinile v=list.get(i);
            if(v.getTitolo().toLowerCase().contains(name.toLowerCase())){
                if(isAvable(v))
                    ret.add(v);
            }
        }
        if(ret.size()==0)
            return null;
        return ret;
    }

    public List<vinile> getListFromTag(String[] tags){
        List<vinile> ret=new ArrayList();
        for(vinile v: list) {
            boolean flag=false;
            for (int i=0;i<tags.length&&flag==false;i++) {
                if(v.getTags()!=null)
                    for (tag x : v.getTags())
                        if (x.getNome().equals(tags[i]))
                            flag = true;
            }
            if(flag&&isAvable(v))
                ret.add(v);
        }
        if(ret.size()>0)
            return ret;
        return null;
    }



}
