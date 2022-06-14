package Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "RedirectTo", value = "/RedirectTo")
public class RedirectTo extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String src=request.getParameter("src");
        if(src!=null){
            if(src.equals("Tag")||src.equals("Vinile")){
                RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/add"+src+".jsp");
                dispatcher.forward(request, response);
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
