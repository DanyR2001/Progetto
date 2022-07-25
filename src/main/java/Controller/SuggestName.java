package Controller;

import Model.ListaVinili;
import Model.Vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SuggestName", value = "/SuggestName")
public class SuggestName extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String keyword=request.getParameter("keyword");
        if(keyword!=null){
            PrintWriter out = response.getWriter();
            HttpSession snn = request.getSession();
            out.println("<ul id='country-list'>");
            if(snn!=null){
                ListaVinili libreria= (ListaVinili) snn.getAttribute("libreria");
                if(libreria!=null){
                    if(keyword.length()>0) {
                        List<Vinile> lib = libreria.getTitleContain(keyword);
                        if (lib != null) {
                            for (Vinile v : lib) {
                                out.println("<li class=\"c\" value=\"" + v.getTitolo() + "\" id=\"" + v.getPK() + "\" onClick=\"selectSuggest('" + v.getPK() + "');\"> <p>" + v.getTitolo() + "</p><sub>" + v.getArtista() + "</sub></li>");
                            }
                        } else
                            out.println("<li class=\"c\" >Nessuna corrispondenza</li>");
                    }
                }
            }
            out.println("</ul>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
