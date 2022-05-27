package Controller;

import Model.oldOrder;
import Model.oldOrderDAO;
import Model.listaVinili;
import Model.utente;
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
            utente u= (utente) snn.getAttribute("utente");
            listaVinili lib= (listaVinili) snn.getAttribute("libreria");
            if(u!=null&&lib!=null){
                oldOrder old= oldOrderDAO.doRetriveById(u,lib);
                snn.setAttribute("OldOrdini",old);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/AreaPersonale.jsp");
                dispatcher.forward(request, response);
            }
            else{
                System.out.println("ciao");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
