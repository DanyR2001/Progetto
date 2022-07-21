package Controller;

import Model.Tag;
import Model.TagsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "UpdateTagList", value = "/UpdateTagList")
public class UpdateTagList extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        String[] cheackbox=request.getParameterValues("vin");
        Tag t= (Tag) session.getAttribute("Tag_selected");
        if(!session.isNew()) {
            System.out.println("banana 1");
            if (cheackbox != null && t != null) {
                System.out.println("banana 2");
                TagsDAO.remeveTagVinilByID(t.getId_tag());
                for (int i = 0; i < cheackbox.length; i++) {
                    System.out.println("banana 3 "+cheackbox[i]);
                    TagsDAO.insertTagForVinil(Integer.parseInt(cheackbox[i]), t.getId_tag());
                }
                session.setAttribute("Tag_selected", null);
                response.sendRedirect("./Admin?src=adminTag");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
