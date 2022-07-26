package Controller;

import Model.Ordine;
import Model.OrdineDAO;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CompleteOrder", value = "/CompleteOrder")
public class CompleteOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        Utente user= (Utente) session.getAttribute("utente");
        Ordine carrello=(Ordine) session.getAttribute("carrello");
        if(session!=null&&carrello!=null) {
            if(!session.isNew()) {
                if (user == null) {
                    System.out.println("--(Utente non loggato)--");
                    session.setAttribute("login", false);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/access.jsp");
                    dispatcher.forward(request, response);
                } else {
                    System.out.println("--(Utente loggato)--");
                    if (carrello.getCarrello() == null) {
                        System.out.println("--(Lista prodotti vuota)--");
                        //non si fa nulla
                        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                        dispatcher.forward(request, response);
                    } else if (carrello.getCarrello().size() > 0) {
                        System.out.println("--(Lista prodotti presente)--");
                        //allora si conclude l'ordine
                        String via = request.getParameter("via");
                        String cap_s = request.getParameter("cap");
                        String citta = request.getParameter("citta");
                        if (via != null && cap_s != null && citta != null) {
                            Integer cap = Integer.parseInt(cap_s);
                            String sub[] = via.split(" ");
                            Integer civico = Integer.parseInt(sub[sub.length - 1]);
                            via = via.replace(" " + sub[sub.length - 1], "");
                            carrello.setCivico(civico);
                            carrello.setVia(via);
                            carrello.setCap(cap);
                            carrello.setCitta(citta);
                            OrdineDAO.completeOrdine(carrello);
                            session.removeAttribute("carrello");
                            System.out.println("--(Ordine completato)--");
                        }
                        RequestDispatcher dispatcher = request.getRequestDispatcher("InitServlet");
                        dispatcher.forward(request, response);
                    }
                }
            }
            else{
                response.sendError(500);
            }
        }
        else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
