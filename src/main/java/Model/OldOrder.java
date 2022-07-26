package Model;

import java.util.ArrayList;

public class OldOrder {
    ArrayList<Ordine> list;

    public OldOrder(){
        list=new ArrayList<>();
    };

    public void add(Ordine x){
        if(x!= null)
            list.add(x);
    }

    public ArrayList<Ordine> getList() {
        return list;
    }

    /**
     * setta la lista degli ordini
     * @param list lista ordini
     */
    public void setList(ArrayList<Ordine> list) {
        this.list = list;
    }

    public Integer size(){
        if(list==null)
            return null;
        return list.size();
    }

}
