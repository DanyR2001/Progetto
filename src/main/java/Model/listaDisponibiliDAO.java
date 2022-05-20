package Model;

import Controller.QueryManger;

import java.util.ArrayList;

public class listaDisponibiliDAO {
    private ArrayList<vinile> disponibili;
    private QueryManger qr;
    private static final String DB_URL="jdbc:mysql://localhost:3306/TSPW?serverTimezone=UTC";
    private static final String USER="java";
    private static final String PAS="1234";

    private void aggiorna(){
        this.disponibili.removeAll(this.disponibili);
        String[][] list= qr.makeQueryWhitNameColum("select * from vinilidisp");
        System.out.println("list.lenght  "+list.length);
        for(int i=0;i<list.length;i++) {
            System.out.println("list["+i+"].lenght  "+list[i].length);
            for (int j = 0; j < list[i].length; j++)
                System.out.println("priva " + list[i][j] + " i:" + i + ", j:" + j);
        }
        for(int i=1;i<list.length;i++){
            this.disponibili.add(new vinile(Integer.parseInt(list[i][0]),list[i][1],Double.parseDouble(list[i][2]),Integer.parseInt(list[i][3]),list[i][4],list[i][5]));
        }
    }

    public listaDisponibiliDAO(){
        disponibili=new ArrayList<>();
        qr= new QueryManger(DB_URL,USER,PAS);
        aggiorna();
    }

    public ArrayList<vinile> getDisponibili() {
        aggiorna();
        return disponibili
                ;
    }
}


