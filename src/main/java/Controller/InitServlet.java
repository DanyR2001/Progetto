package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.lang.invoke.MutableCallSite;
import java.util.ArrayList;


@WebServlet(name = "InitServlet", value = "/InitServlet")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        listaVinili libreria;
        ordine carrello;
        utente user;
        ArrayList<tag> Tags=tagsDAO.getAll();
        HttpSession session=request.getSession();
        session.setAttribute("tags",Tags);
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
    }
}
