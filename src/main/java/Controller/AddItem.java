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
        int id= Integer.parseInt(request.getParameter("id"));
        int quantita=Integer.parseInt(request.getParameter("quantita"));
        HttpSession snn=request.getSession();
        ListaVinili servise= (ListaVinili) snn.getAttribute("libreria");
        Ordine carrello= (Ordine) snn.getAttribute("carrello");
        Vinile act=servise.findViniliFromId(id);
        Prodotto p=new Prodotto();
        p.setArticolo(act);
        p.setQuantita(quantita);
        carrello.addProdotto(p,servise);
        Utente u= (Utente) snn.getAttribute("utente");
        if(u!=null){
            OrdineDAO.uploadOrdine(u,carrello,servise);
        }
        snn.setAttribute("carrello",carrello);
        snn.setAttribute("refresh",true);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
