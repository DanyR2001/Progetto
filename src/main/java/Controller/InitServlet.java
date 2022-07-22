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
        HttpSession session = request.getSession();
        session.setAttribute("tags",Tags); //metto i tag nella sessione
        RequestDispatcher dispatcher = null;

        if(session.isNew()){

            ListaDisponibiliDAO service = new ListaDisponibiliDAO();      //se la sessione è nuova, prendo i vinili disponibili dal DB, creo un
            libreria = service.getAll();                                  //nuovo carrello e metto tutto nella nuova sessione
            carrello = new Ordine();
            session.setAttribute("libreria",libreria);
            session.setAttribute("carrello",carrello);

        } else {                                                           //la sessione esiste

            libreria= (ListaVinili) session.getAttribute("libreria");
            carrello= (Ordine) session.getAttribute("carrello");
            user= (Utente) session.getAttribute("utente");

            if(libreria == null && carrello == null && user == null){       //se nella sessione non ho la lista di vinili, nè il carrello, nè l'utente

                ListaDisponibiliDAO service  = new ListaDisponibiliDAO();
                libreria = service.getAll();                                //prendo la lista dei vinili disponibili nel database
                carrello = new Ordine();                                    //creo un nuovo carrello
                session.setAttribute("libreria",libreria);              //metto tutto in sessione
                session.setAttribute("carrello",carrello);

            } else if(libreria == null && carrello == null && user != null) {  //se nella sessione ho l'utente ma non ho il carrello nè la lista di vinili disponibili

                if(user.isAdmin_bool()){                                    //se l'utente è l'amministratore
                    dispatcher = request.getRequestDispatcher("/Admin"); //lo reindirizzo alla pagina dell'amministratore

                } else { //altrimenti

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
                if(user.isAdmin_bool()){
                    dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore

                } else {
                    //qua non ci possiamo mai entrare esistera sempre una libreria ---

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

                if(user.isAdmin_bool()){
                     dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore

                } else {

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

                if(user.isAdmin_bool()){
                     dispatcher = request.getRequestDispatcher("/Admin"); //se l'utente è amministratore lo reindirizzo alla pagina dell'amministratore

                } else {

                    Ordine carrelloDb = OrdineDAO.getCarrelloFromDb(user, libreria); //prendo il carrelo dal DB

                    if (carrelloDb == null) { //se non ho un carrello nel DB
                        OrdineDAO.insertOrdine(user, carrello); //inserisco quello della sessione

                    } else { //se c'è il carrello nel database

                        carrello.toPrint();
                        carrelloDb.toPrint();
                        int num = carrello.join(carrelloDb, libreria); //faccio il join dei due carrelli e prendo il numero di vinili rimossi
                        if(num>0)
                            session.setAttribute("numRemoved",num); //so ho rimosso qualche vinile, metto il loro numero nella sessione
                        OrdineDAO.uploadOrdine(user, carrello, libreria);

                    }
                    session.setAttribute("libreria", libreria); //metto nella sessione
                    session.setAttribute("carrello", carrello);
                }

            } else if(libreria != null && carrello != null && user==null) { //se ho il carrello e i vinili ma non ho l'utente
                //do nothig
            } else if(libreria != null && carrello == null && user==null) { //se ho la libreria ma non ho il carrello nè l'utente

                carrello = new Ordine();
                session.setAttribute("carrello",carrello); //creo un nuovo carrello e lo metto nella sessione

            } else if(libreria==null && carrello != null && user == null) { //se ho solo il carrello

                ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                libreria = service.getAll();
                session.setAttribute("libreria",libreria); //prendo i vinili disponibili dal DB e li metto nella sessione
            }
        }
        if(dispatcher == null){
            dispatcher = request.getRequestDispatcher("/index.jsp"); //renderizzo alla homepage se l'utente non è un amministratore
        }
        dispatcher.forward(request, response);
    }
}
