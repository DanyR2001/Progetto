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
        System.out.println("--(Inizio init servlet:)--");
        ListaVinili libreria;
        Ordine carrello;
        Utente user;
        HttpSession session = request.getSession();
        //session.setMaxInactiveInterval(10);
        if(session.getAttribute("tags")==null)
            session.setAttribute("tags",TagsDAO.getAll()); //metto i tag nella sessione
        RequestDispatcher dispatcher = null;
        if(session.isNew()){
            System.out.println("--(Sessione nuova)--");
            ListaDisponibiliDAO service = new ListaDisponibiliDAO();      //se la sessione è nuova, prendo i vinili disponibili dal DB, creo un
            libreria = service.getAll();                                  //nuovo carrello e metto tutto nella nuova sessione
            carrello = new Ordine();
            session.setAttribute("libreria",libreria);
            session.setAttribute("carrello",carrello);

        } else {                                                           //la sessione esiste
            System.out.println("--(Sessione non nuova)--");
            libreria= (ListaVinili) session.getAttribute("libreria");
            carrello= (Ordine) session.getAttribute("carrello");
            user= (Utente) session.getAttribute("utente");
            if(libreria == null && carrello == null && user == null){       //se nella sessione non ho la lista di vinili, nè il carrello, nè l'utente
                System.out.println("--(libreria = null, carrello = null , utente = null)--");
                ListaDisponibiliDAO service  = new ListaDisponibiliDAO();
                libreria = service.getAll();                                //prendo la lista dei vinili disponibili nel database
                carrello = new Ordine();                                    //creo un nuovo carrello
                session.setAttribute("tags", TagsDAO.getAll());
                session.setAttribute("libreria",libreria);              //metto tutto in sessione
                session.setAttribute("carrello",carrello);

            } else if(libreria == null && carrello == null && user != null) {  //se nella sessione ho l'utente ma non ho il carrello nè la lista di vinili disponibili
                System.out.println("--(libreria = null, carrello = null , utente != null)--");
                if(user.isAdmin_bool()){                                    //se l'utente è l'amministratore
                    System.out.println("--(u -> Admin)--");
                    dispatcher = request.getRequestDispatcher("/Admin"); //lo reindirizzo alla pagina dell'amministratore
                } else { //altrimenti
                    System.out.println("--(u -> NotAdmin)--");
                    ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                    libreria = service.getAll();                            //prendo i vinili disponibili dal database
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria); //prendo il carrello dal DB associato all'utente attuale

                    if (carrelloDb == null) { //se il carrello nel DB è vuoto
                        carrelloDb = new Ordine(); //creo un nuovo carrello
                    }

                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrelloDb); //inserisco lista vinili e carrello nella sessione
                }

            } else if(libreria == null && carrello != null && user != null) { //se nella sessione c'è l'utente e il carrello ma non la lista dei prodotti disponibili
                System.out.println("--(libreria = null, carrello != null , utente != null)--");
                if(user.isAdmin_bool()){
                    System.out.println("--(u -> Admin)--");
                    dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore
                } else {
                    //qua non ci possiamo mai entrare perchè ci sarà sempre una libreria
                    System.out.println("--(u -> NotAdmin)--");
                    ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                    libreria = service.getAll();
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria);

                    if (carrelloDb == null) {
                        //inserire quello della sessione nel db ---
                        //ordineDAO.insertOrdine(user,carrello); ---
                    } else {

                        if (carrelloDb.getCarrello() != null) {

                            int num=carrello.join(carrelloDb, libreria);
                            if(num>0)
                                session.setAttribute("numRemoved",num);
                            //devo aggiornare il DB ---
                            //ordineDAO.uploadOrdine(user,carrello,libreria); ---
                        }
                    }
                    session.setAttribute("libreria", libreria);
                    session.setAttribute("carrello", carrello);
                }
            } else if(libreria != null && carrello == null && user != null) { // se nella sessione ho l'utente, la lista dei vinili ma non il carrello
                System.out.println("--(libreria != null, carrello = null , utente != null)--");

                if(user.isAdmin_bool()){
                    dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore
                    System.out.println("--(u -> Admin)--");
                } else {
                    System.out.println("--(u -> NotAdmin)--");
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria); //prendo il carrello dal DB associato all'utente
                    if (carrelloDb == null) { //se il carrello del DB è vuoto

                        carrello = new Ordine(); //ne creo uno nuovo
                        ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                        libreria = service.getAll(); //prendo la lista dei vinili disponibili dal DB

                        //devo creare nel db un nuovo ordine ---
                        OrdineDAO.insertOrdine(user, carrello);
                    }
                    session.setAttribute("libreria", libreria); //metto nella sessione
                    session.setAttribute("carrello", carrello);
                }
            }
            else if(libreria!=null && carrello!=null && user!=null){ // se ho utente, carrello e lista vinili
                System.out.println("--(libreria != null, carrello != null , utente != null)--");
                if(user.isAdmin_bool()){
                     dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore
                    System.out.println("--(u -> Admin)--");
                } else {
                    System.out.println("--(u -> NotAdmin)--");
                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria); //prendo il carrelo dal DB

                    if (carrelloDb == null) { //se non ho un carrello nel DB
                        OrdineDAO.insertOrdine(user, carrello); //inserisco quello della sessione

                    } else { //se c'è il carrello nel database
                        System.out.println("--(Stampa session carrello)--");
                        carrello.toPrint();
                        System.out.println("--(Stampa DB carrello)--");
                        carrelloDb.toPrint();
                        System.out.println("--(join carrelli)--");
                        int num = carrello.join(carrelloDb, libreria); //faccio il join dei due carrelli e prendo il numero di vinili rimossi
                        System.out.println("--(fine join carrelli)--");
                        if(num>0)
                            session.setAttribute("numRemoved",num); //so ho rimosso qualche vinile, metto il loro numero nella sessione
                        OrdineDAO.uploadOrdine(user, carrello, libreria);

                    }
                    session.setAttribute("libreria", libreria); //metto nella sessione
                    session.setAttribute("carrello", carrello);
                }

            } else if(libreria != null && carrello != null && user==null) { //se ho il carrello e i vinili ma non ho l'utente
                System.out.println("--(libreria != null, carrello != null , utente = null)--");
                //do nothig
            } else if(libreria != null && carrello == null && user==null) { //se ho la libreria ma non ho il carrello nè l'utente
                System.out.println("--(libreria != null, carrello = null , utente = null)--");
                carrello = new Ordine();
                session.setAttribute("carrello",carrello); //creo un nuovo carrello e lo metto nella sessione
            } else if(libreria==null && carrello != null && user == null) { //se ho solo il carrello
                System.out.println("--(libreria = null, carrello != null , utente = null)--");
                ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                libreria = service.getAll();
                session.setAttribute("libreria",libreria); //prendo i vinili disponibili dal DB e li metto nella sessione
            }
        }
        if(dispatcher == null){
            dispatcher = request.getRequestDispatcher("/index.jsp"); //renderizzo alla homepage se l'utente non è un amministratore
        }
        System.out.println("--(Fine Init Servlet)--");
        dispatcher.forward(request, response);
    }
}
