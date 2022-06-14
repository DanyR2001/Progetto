package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.lang.invoke.MutableCallSite;


@WebServlet(name = "InitServlet", value = "/InitServlet")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        listaVinili libreria;
        ordine carrello;
        utente user;
        HttpSession session=request.getSession();
        RequestDispatcher dispatcher=null;
        System.out.println("--Inizio servlet--");
        System.out.println("--if(1;2)--");
        if(session.isNew()){
            System.out.println("--(1)--");
            listaDisponibiliDAO service=new listaDisponibiliDAO();
            libreria=service.getAll();
            carrello=new ordine();
            session.setAttribute("libreria",libreria);
            session.setAttribute("carrello",carrello);
        }
        else{
            System.out.println("--(2)--");
            libreria= (listaVinili) session.getAttribute("libreria");
            carrello= (ordine) session.getAttribute("carrello");
            user= (utente) session.getAttribute("utente");
            System.out.println("--if(3;4;5;6;7;8;9;10)--");
            if(libreria==null&&carrello==null&&user==null){
                System.out.println("--(3)--");
                listaDisponibiliDAO service=new listaDisponibiliDAO();
                libreria=service.getAll();
                carrello=new ordine();
                session.setAttribute("libreria",libreria);
                session.setAttribute("carrello",carrello);
            }
            else if(libreria==null&&carrello==null&&user!=null){
                if(user.isAdmin_bool()){
                    dispatcher = request.getRequestDispatcher("/Admin");
                }
                else {
                    System.out.println("--(4)--");
                    listaDisponibiliDAO service = new listaDisponibiliDAO();
                    libreria = service.getAll();
                    ordine carrelloDb = ordineDAO.getCarrelloFromDb(user, libreria);
                    System.out.println("--if(4.1)--");
                    if (carrelloDb == null) {
                        System.out.println("--(4.1)--");
                        carrelloDb = new ordine();
                    }
                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrelloDb);
                }
            }
            else if(libreria==null&&carrello!=null&&user!=null){
                if(user.isAdmin_bool()){
                    dispatcher = request.getRequestDispatcher("/Admin");
                }
                else {
                    //qua non ci possiamo mai entrare esistera sempre una libreria
                    System.out.println("--(5)--");
                    listaDisponibiliDAO service = new listaDisponibiliDAO();
                    libreria = service.getAll();
                    ordine carrelloDb = ordineDAO.getCarrelloFromDb(user, libreria);
                    System.out.println("--if(5.1;5.2)--");
                    if (carrelloDb == null) {
                        System.out.println("--(5.1)--");
                        //inserire quello della sessione nel db
                        //ordineDAO.insertOrdine(user,carrello);
                    } else {
                        System.out.println("--(5.2)--");
                        if (carrelloDb.getCarrello() != null) {
                            System.out.println("--(5.3)--");
                            carrello.join(carrelloDb, libreria);
                            //devo aggiornare il DB
                            //ordineDAO.uploadOrdine(user,carrello,libreria);
                        }
                    }
                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrello);
                }
            }
            else if(libreria!=null&&carrello==null&&user!=null){
                if(user.isAdmin_bool()){
                     dispatcher = request.getRequestDispatcher("/Admin");
                }
                else {
                    System.out.println("--(6)--");
                    ordine carrelloDb = ordineDAO.getCarrelloFromDb(user, libreria);
                    if (carrelloDb == null) {
                        System.out.println("--(6.1)--");
                        carrello = new ordine();
                        listaDisponibiliDAO service = new listaDisponibiliDAO();
                        libreria = service.getAll();
                        //devo creare nel db un nuovo ordine
                        ordineDAO.insertOrdine(user, carrello);
                    }
                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrello);
                }
            }
            else if(libreria!=null&&carrello!=null&&user!=null){
                System.out.println("--(7)--");
                if(user.isAdmin_bool()){
                     dispatcher = request.getRequestDispatcher("/Admin");
                }
                else {
                    ordine carrelloDb = ordineDAO.getCarrelloFromDb(user, libreria);
                    //carrello.join(carrelloDb,libreria);
                    if (carrelloDb == null) {
                        System.out.println("--(7.1)--");
                        ordineDAO.insertOrdine(user, carrello);
                        //Funziona
                    } else {
                        System.out.println("--(7.2)--");
                        carrello.toPrint();
                        System.out.println("--DB--");
                        carrelloDb.toPrint();
                        carrello.join(carrelloDb, libreria);
                        ordineDAO.uploadOrdine(user, carrello, libreria);
                        //funziona
                    }
                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrello);
                }
            }
            else if(libreria!=null&&carrello!=null&&user==null){
                System.out.println("--(8)--");
                //do nothig
            }
            else if(libreria!=null&&carrello==null&&user==null){
                System.out.println("--(9)--");
                carrello=new ordine();
                session.setAttribute("carrello",carrello);
            }
            else if(libreria==null&&carrello!=null&&user==null){
                System.out.println("--(10)--");
                listaDisponibiliDAO service=new listaDisponibiliDAO();
                libreria=service.getAll();
                session.setAttribute("libreria",libreria);
            }
        }
        if(dispatcher==null)
            dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
        /*System.out.println("---------------");
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
        dispatcher.forward(request, response);*/
    }
}
