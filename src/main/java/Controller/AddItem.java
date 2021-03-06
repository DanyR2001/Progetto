package Controller;

import Model.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AddItem", value = "/AddItem")
public class AddItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer id = null;
        String id_s=request.getParameter("id");
        if(id_s!=null)
            id=Integer.parseInt(id_s);
        Integer quantita=null;
        String quantita_s=request.getParameter("quantita");
        if(quantita_s!=null)
            quantita=Integer.parseInt(quantita_s);
        HttpSession snn=request.getSession(false);
        if(snn!=null&&quantita!=null&&id!=null) {
            ListaVinili servise= (ListaVinili) snn.getAttribute("libreria");
            Ordine carrello= (Ordine) snn.getAttribute("carrello");
            if(!snn.isNew()) {
                if(servise!=null&&carrello!=null) {
                    Vinile act = servise.findViniliFromId(id);
                    Prodotto p = new Prodotto();
                    p.setArticolo(act);
                    p.setQuantita(quantita);
                    carrello.addProdotto(p, servise);
                    Utente u = (Utente) snn.getAttribute("utente");
                    if (u != null) {
                        if(!u.isAdmin_bool()) {
                            OrdineDAO.uploadOrdine(u, carrello, servise);
                        }
                        else{
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/gestione.jsp");
                            dispatcher.forward(request, response);
                        }
                    }
                    snn.setAttribute("carrello", carrello);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                    dispatcher.forward(request, response);
                } else{
                    response.sendError(500);
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
