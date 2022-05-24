package Controller;

import Model.tag;
import Model.tagsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AddVinile", value = "/AddVinile")
public class AddVinile extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<tag> listaTags= tagsDAO.getAll();
        HttpSession snn=request.getSession();
        snn.setAttribute("Tags",listaTags);
        RequestDispatcher dispatcher = request.getRequestDispatcher("WEB-INF/addVinile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
