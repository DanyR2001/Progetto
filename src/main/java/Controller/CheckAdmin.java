package Controller;

import Model.Utente;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "CheckAdmin", value = "/CheckAdmin")
public class CheckAdmin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession(false);
        if(session!=null){
            if(!session.isNew()){
                String val=request.getParameter("val");
                if(val!=null){
                    if(val.equals("adminVinile")||val.equals("adminTag")){
                        Utente u= (Utente) session.getAttribute("utente");
                        if(u!=null){
                            if(!u.isAdmin_bool())
                                response.sendError(500);
                        } else{
                            response.sendError(500);
                        }
                    } else{
                        response.sendError(500);
                    }
                }else{
                    response.sendError(500);
                }
            }else{
                response.sendError(500);
            }
        }else{
            response.sendError(500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
