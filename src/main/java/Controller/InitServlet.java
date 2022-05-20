package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;


@WebServlet(name = "InitServlet", value = "/InitServlet")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("---------------");
        listaVinili x=new listaVinili();
        ordine carrello= new ordine();
        HttpSession snn=request.getSession();
        listaVinili service= (listaVinili) snn.getAttribute("lista");
        if(service==null) {
            snn.setAttribute("lista", x);
            service = x;
        }
        if(!snn.isNew()) {
            System.out.println("\n ha 1 ");
            ordine temp= (ordine) snn.getAttribute("carrello");
            utente u= (utente) snn.getAttribute("utente");
            if(u!=null){
                System.out.println("\n 2 ");
                for(vinile v:service.getAllVinil())
                    System.out.println(" disponibili:"+v.getQuantita());
                ordine c= new ordineDAO().doRetrieveByUser(u,snn);
                for(vinile v:service.getAllVinil())
                    System.out.println(" disponibili:"+v.getQuantita());
                if(c!=null) {
                    System.out.println("\n 3 ");
                    if(c.getCarrello()!=null)
                        System.out.println(c.toString());
                    if(temp!=null&&temp.getCarrello()!=null&&c.getCarrello()!=null) {
                        System.out.println("\n 4 ");
                        temp.join(c);
                        temp.setCodice(c.getCodice());
                        temp.setEvaso(c.isEvaso());
                        System.out.println(temp.toString());
                        snn.setAttribute("carrello",temp);
                        ordineDAO.uploadOrdineStop(u,temp,snn);
                        System.out.println("bho \n");
                    }
                    if(temp!=null&&c.getCarrello()==null&&temp.getCarrello()!=null){
                        System.out.println("\n 12 ");
                        c.setList(temp.getCarrello());
                        c.setPrezzo(temp.getPrezzo());
                        System.out.println(c.toString());
                        snn.setAttribute("carrello", c);
                        c.check();
                        c.refreshCost();
                        for(int i=0;i<c.getCarrello().size();i++) {
                            int actual = c.getCarrello().get(i).getQuantita();
                            int remain = service.numDispVinil(c.getCarrello().get(i).getArticolo());
                            int total = remain + actual;
                            System.out.println("act order" + actual + " remin list " + remain + " total " + total );
                        }
                        ordineDAO.uploadOrdine(u,c,snn);
                    }
                    if(temp!=null&&c.getCarrello()==null&&temp.getCarrello()==null){
                        System.out.println("\n 13 ");
                        snn.setAttribute("carrello", carrello);
                        new ordineDAO().insertOrdine(u,temp);
                    }
                }
                else{
                    System.out.println("\n 6 ");
                    if(temp==null) {
                        snn.setAttribute("carrello", carrello);
                        new ordineDAO().insertOrdine(u,carrello);
                        System.out.println("7 ");
                    }
                    else {
                        System.out.println("\n 8 ");
                        new ordineDAO().insertOrdine(u,temp);
                        snn.setAttribute("carrello", temp);
                        //ordineDAO.uploadOrdine(u,temp);
                    }
                }
            }
            else {
                if (temp == null) {
                    System.out.println("\n 9 ");
                    snn.setAttribute("carrello", carrello);
                } else {
                    System.out.println("\n 11 ");
                    //snn.setAttribute("carrello", temp);
                }
            }
        }
        else{
            System.out.println("\n 10 ");
            snn.setAttribute("carrello",carrello);
        }
        System.out.println("-------------------");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
