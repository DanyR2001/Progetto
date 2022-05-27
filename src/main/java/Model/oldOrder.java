package Model;

import java.util.ArrayList;

public class oldOrder {
    ArrayList<ordine> list;

    public oldOrder(){
        list=new ArrayList<>();
    };

    public ArrayList<ordine> getList() {
        return list;
    }

    public void setList(ArrayList<ordine> list) {
        this.list = list;
    }
}
