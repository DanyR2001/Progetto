package Controller;

import Model.tag;
import Model.tagsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "AddTag", value = "/AddTag")
public class AddTag extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] cheackbox=request.getParameterValues("tag");
        String nome=request.getParameter("Nome");
        HttpSession snn=request.getSession(false);
        System.out.println("p1");
        if(snn!=null) {
            System.out.println("p2");
            ArrayList<tag> tags= (ArrayList<tag>) snn.getAttribute("tags");
            if (nome != null && cheackbox != null && tags!=null) {
                System.out.println("p3");
                if (cheackbox.length >= 1) {
                    System.out.println("p4");
                    tag t = tagsDAO.insertTag(nome);
                    for (String s : cheackbox) {
                        int x = Integer.parseInt(s);
                        tagsDAO.insertTagForVinil(x, t.getId_tag());
                    }
                    tags.add(t);
                    snn.setAttribute("tags",tags);
                    response.sendRedirect("./Admin?src=adminTag");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}