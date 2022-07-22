package Controller;

import Model.ListaVinili;
import Model.Tag;
import Model.TagsDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Search", value = "/Search")
public class Search extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn= request.getSession();
        ListaVinili libreria= (ListaVinili) snn.getAttribute("libreria");
        if(snn!=null&&libreria!=null) {
            if (!snn.isNew()) {
                String cont = request.getParameter("String");
                if (cont != null) {
                    List vin;
                    if (cont.length() >= 1)
                        vin = libreria.getTitleContain(cont);
                    else
                        vin = libreria.getAvailableVinili().getAllVinili();
                    snn.setAttribute("listaResult", vin);
                    snn.setAttribute("String", cont);
                    if (snn.getAttribute("tags") == null)
                        snn.setAttribute("tags", TagsDAO.getAll());
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
                    dispatcher.forward(request, response);
                } else {
                    String radio = request.getParameter("choose");
                    List resp = null;
                    if (radio.equals("Testo")) {
                        String testo = request.getParameter("search");
                        if (testo != null) {
                            if (testo.length() >= 1)
                                resp = libreria.getTitleContain(testo);
                            else
                                resp = libreria.getAvailableVinili().getAllVinili();
                            snn.setAttribute("String", testo);
                        }
                    } else if (radio.equals("Tag")) {
                        String[] selected = request.getParameterValues("cheackbox");
                        if (selected != null) {
                            if (selected.length > 0)
                                resp = libreria.getListFromTag(selected);
                            else
                                resp = libreria.getAvailableVinili().getAllVinili();
                        } else
                            resp = libreria.getAvailableVinili().getAllVinili();
                        snn.setAttribute("String", "");
                    }
                    snn.setAttribute("listaResult", resp);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/search.jsp");
                    dispatcher.forward(request, response);
                }
            } else {
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
