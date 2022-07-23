package Controller;

import Model.ListaDisponibiliDAO;
import Model.ListaVinili;
import Model.Utente;
import Model.Vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UploadItem", value = "/UploadItem")
public class UploadItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        Utente u= (Utente) snn.getAttribute("utente");
        if(!snn.isNew()) {
            if (u != null) {
                if(u.isAdmin_bool()) {
                    ListaVinili lib = (ListaVinili) snn.getAttribute("libreria");
                    if (lib != null) {
                        Integer Index = null;
                        String index_s = request.getParameter("index");
                        if (index_s != null)
                            Index = Integer.parseInt(index_s);
                        Integer Quantita = null;
                        String quantita_s = request.getParameter("quantita");
                        if (quantita_s != null)
                            Quantita = Integer.parseInt(quantita_s);
                        Double Prezzo = null;
                        String prezzo_s=request.getParameter("prezzo");
                        if(prezzo_s!=null)
                            Prezzo=Double.parseDouble(prezzo_s);
                        String Artista = request.getParameter("nameArtist");
                        if(Index!=null&&Quantita!=null&&Prezzo!=null&&Artista!=null) {
                            System.out.println("-------(Modifica articolo fatta)-------");
                            Vinile v = lib.get(Index);
                            ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                            v.setPrezzo(Prezzo);
                            v.setArtista(Artista);
                            lib.setQuantitaVin(v, Quantita);
                            service.changeById(v, Quantita);
                            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin?src=adminVinile");
                            dispatcher.forward(request, response);
                        } else{
                            response.sendError(500);
                        }
                    } else {
                        response.sendError(500);
                    }
                } else{
                    response.sendError(500);
                }
            } else {
                response.sendError(500);
            }
        } else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
