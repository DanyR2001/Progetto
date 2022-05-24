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
        listaVinili servise= (listaVinili) snn.getAttribute("libreria");
        ordine carrello= (ordine) snn.getAttribute("carrello");
        vinile act=servise.findVinilieFromId(id);
        prodotto p=new prodotto();
        p.setArticolo(act);
        p.setQuantita(quantita);
        carrello.addProdotto(p,servise);
        utente u= (utente) snn.getAttribute("utente");
        if(u!=null){
            ordineDAO.uploadOrdine(u,carrello,servise);
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
