package Model;

import java.util.ArrayList;
import java.util.List;

public class ListaVinili {
    ArrayList<Vinile> list;
    ArrayList<Integer> size;

    public ListaVinili(){
        list = new ArrayList<>();
        size= new ArrayList<>();
    }

    public Vinile findViniliFromId(Integer id){
        if(id==null)
            return null;
        if(list.size()>0)
            for(Vinile v: list)
                if(v.getPK()==id)
                    return v;
        return null;
    }

    public ArrayList<Vinile> getAllVinili(){
        if(list.size()>0)
            return list;
        return null;
    }

    public ListaVinili getAvailableVinili(){
        ListaVinili ret=null;
        if(list.size()>0){
            ret=new ListaVinili();
            for(int i=0;i<list.size();i++)
                if(size.get(i)>0) {
                    ret.add(list.get(i),size.get(i));
                }
        }
        return ret;
    }

    public ListaVinili getFromTag(Tag Tag){
        ListaVinili ret=null;
        if(Tag!=null) {
            ret = new ListaVinili();
            for (int i = 0; i < list.size(); i++) {
                Vinile v = list.get(i);
                if (v.getTags() != null) {
                    for (int j = 0; j < v.getTags().size(); j++)
                        if (v.getTags().get(j).equals(Tag))
                            ret.add(v,size.get(i));
                }
            }
            return ret;
        }
        return null;
    }

    public void add(Vinile v, Integer disponibilita){
        list.add(v);
        size.add(disponibilita);
    }

    public boolean isAvailable(Vinile v){
        System.out.println("----(il vinilie "+v.getTitolo()+" e disponibili? controllo)----");
        if(list.size()>0)
            for (int i=0;i<list.size();i++) {
                if (list.get(i).equals(v)) {
                    if (size.get(i) > 0) {
                        System.out.println("----("+v.getTitolo()+" si)----");
                        return true;
                    }
                    else {
                        System.out.println("----("+v.getTitolo()+" no)----");
                        return false;
                    }
                }
            }
        return false;
    }

    public Integer getQuantitaVin(Vinile v){
        if(list.size()>0)
            for (int i=0;i<list.size();i++)
                if(list.get(i).equals(v))
                    return size.get(i);
        return -1;
    }

    public void setQuantitaVin(Vinile v, Integer quantita){
        if(list.size()>0)
            for (int i=0;i<list.size();i++)
                if(list.get(i).equals(v)){
                    size.remove(i);
                    size.add(i,quantita);
                }
    }

    public Vinile get(int index){
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

    public List<Vinile> getTitleContain(String name){
        List<Vinile> ret=new ArrayList<>();
        for(int i=0;i<list.size()&&name.length()>0;i++){
            Vinile v=list.get(i);
            if((v.getTitolo().toLowerCase()+" "+v.getArtista().toLowerCase()).contains(name.toLowerCase())){
                if(isAvailable(v))
                    ret.add(v);
            }
        }
        if(ret.size()==0)
            return null;
        return ret;
    }

    public List<Vinile> getListFromTag(String[] tags){
        List<Vinile> ret=new ArrayList();
        for(Vinile v: list) {
            boolean flag=false;
            for (int i=0;i<tags.length&&flag==false;i++) {
                flag = true;
                if(v.getTags()!=null) {
                    for (int j = 0; j < v.getTags().size(); j++) {
                        Tag x = v.getTags().get(j);
                        if (x.getNome().equals(tags[i]))
                            flag = false;
                    }
                }
            }
            if(flag==false&& isAvailable(v))
                ret.add(v);
        }
        if(ret.size()>0)
            return ret;
        return null;
    }


    public boolean toShow(Ordine carrello) {
        boolean flag = true;
            for (int i = 0; i < list.size(); i++){
                Prodotto p = carrello.getItem(list.get(i));
                if(p != null) {
                    if ((size.get(i) - p.getQuantita()) > 0) {
                        return true;
                    }else {
                        flag = false;
                    }
                } else {
                    return true;
                }
            }
        return flag;
    }

    public boolean isPresent(Vinile vin){
        boolean flag= false;
        for(Vinile v: list)
            if(v.equals(vin))
                flag=true;
        return flag;
    }
}
