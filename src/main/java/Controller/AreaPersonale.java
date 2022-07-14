package Controller;

import Model.OldOrder;
import Model.OldOrderDAO;
import Model.ListaVinili;
import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AreaPersonale", value = "/AreaPersonale")
public class AreaPersonale extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        if(!snn.isNew()){
            Utente u= (Utente) snn.getAttribute("utente");
            ListaVinili lib= (ListaVinili) snn.getAttribute("libreria");
            if(u!=null&&lib!=null){
                OldOrder old= OldOrderDAO.doRetriveById(u,lib);
                snn.setAttribute("OldOrdini",old);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/AreaPersonale.jsp");
                dispatcher.forward(request, response);
            }
            else{
                System.out.println("ciao");
                snn.setAttribute("noLogArea",false);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/access.jsp");
                dispatcher.forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
