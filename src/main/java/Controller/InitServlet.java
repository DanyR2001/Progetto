package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;


@WebServlet(name = "InitServlet", value = "/InitServlet")
public class InitServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListaVinili libreria;
        Ordine carrello;
        Utente user;
        ArrayList<Tag> Tags= TagsDAO.getAll();
        HttpSession session=request.getSession();
        session.setAttribute("tags",Tags);
        RequestDispatcher dispatcher=null;
        System.out.println("--Inizio servlet--");
        System.out.println("--if(1;2)--");

        if(session.isNew()){
            System.out.println("--(1)--");
            ListaDisponibiliDAO service=new ListaDisponibiliDAO();      //se la sessione è nuova, prendo i vinili disponibili dal dd, creo un
            libreria=service.getAll();                                  //nuovo carrello e metto tutto nella nuova sessione
            carrello=new Ordine();
            session.setAttribute("libreria",libreria);
            session.setAttribute("carrello",carrello);
        }
        else{                                                           //la sessione esiste
            System.out.println("--(2)--");
            libreria= (ListaVinili) session.getAttribute("libreria");
            carrello= (Ordine) session.getAttribute("carrello");
            user= (Utente) session.getAttribute("utente");
            System.out.println("--if(3;4;5;6;7;8;9;10)--");
            if(libreria==null&&carrello==null&&user==null){             //la sessione c'è ma non ho la lista di vinili, nè il carrello nè l'utente
                System.out.println("--(3)--");
                ListaDisponibiliDAO service=new ListaDisponibiliDAO();
                libreria=service.getAll();
                carrello=new Ordine();
                session.setAttribute("libreria",libreria);
                session.setAttribute("carrello",carrello);
            }
            else if(libreria==null&&carrello==null&&user!=null){
                if(user.isAdmin_bool()){
                    dispatcher = request.getRequestDispatcher("/Admin");
                }
                else {
                    System.out.println("--(4)--");
                    ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                    libreria = service.getAll();
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria);
                    System.out.println("--if(4.1)--");
                    if (carrelloDb == null) {
                        System.out.println("--(4.1)--");
                        carrelloDb = new Ordine();
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
                    ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                    libreria = service.getAll();
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria);
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
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria);
                    if (carrelloDb == null) {
                        System.out.println("--(6.1)--");
                        carrello = new Ordine();
                        ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                        libreria = service.getAll();
                        //devo creare nel db un nuovo ordine
                        OrdineDAO.insertOrdine(user, carrello);
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
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria);
                    //carrello.join(carrelloDb,libreria);
                    if (carrelloDb == null) {
                        System.out.println("--(7.1)--");
                        OrdineDAO.insertOrdine(user, carrello);
                        //Funziona
                    } else {
                        System.out.println("--(7.2)--");
                        carrello.toPrint();
                        System.out.println("--DB--");
                        carrelloDb.toPrint();
                        carrello.join(carrelloDb, libreria);
                        OrdineDAO.uploadOrdine(user, carrello, libreria);
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
                carrello=new Ordine();
                session.setAttribute("carrello",carrello);
            }
            else if(libreria==null&&carrello!=null&&user==null){
                System.out.println("--(10)--");
                ListaDisponibiliDAO service=new ListaDisponibiliDAO();
                libreria=service.getAll();
                session.setAttribute("libreria",libreria);
            }
        }
        if(dispatcher==null)
            dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
