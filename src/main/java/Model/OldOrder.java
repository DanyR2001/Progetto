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

    public void setList(ArrayList<Ordine> list) {
        this.list = list;
    }
}
