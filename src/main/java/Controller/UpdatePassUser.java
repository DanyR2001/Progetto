package Controller;

import Model.Utente;
import Model.UtentiDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdatePassUser", value = "/UpdatePassUser")
public class UpdatePassUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        Utente u= (Utente) session.getAttribute("utente");
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        if(!session.isNew()&&u!=null){
           String oldPass=request.getParameter("pass-old");
           String newPass=request.getParameter("pass-new");
            if(oldPass!=null&&newPass!=null){
                Utente newUser=new Utente();
                newUser.setPassword(oldPass);
                if(newUser.getPasswordhash().equals(u.getPasswordhash())){
                    newUser.setPassword(newPass);
                    if(newUser.getPasswordhash().equals(u.getPasswordhash())){
                        session.setAttribute("noModify",true);
                    }
                    else {
                        u.setPassword(newPass);
                        UtentiDAO.doUpdate(u);
                        session.setAttribute("utente", u);
                        session.setAttribute("modify", true);
                    }
                }
                else {
                    session.setAttribute("noPassCorrect",true);
                    //non corrispondono errore
                }
                dispatcher = request.getRequestDispatcher("/WEB-INF/AreaPersonale.jsp");
            }
            else{
                response.sendError(500);
            }
        }
        else{
            response.sendError(500);
        }
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
