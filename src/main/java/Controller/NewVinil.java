package Controller;

import Model.ListaDisponibiliDAO;
import Model.Tag;
import Model.Utente;
import Model.Vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import javax.print.attribute.standard.OrientationRequested;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static Controller.Constants.UPLOAD_DIRECTORY;

@WebServlet(name = "NewVinil", value = "/NewVinil")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)

public class NewVinil extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename"))
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
        }
        return Constants.DEFAULT_FILENAME;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession snn=request.getSession();
        if(!snn.isNew()) {
            System.out.println("---(aggiunta vinilie)---");
            String Titolo = request.getParameter("Titolo");
            Part image = request.getPart("Upload");
            String quantita_s=request.getParameter("Quantita");
            Integer Quantita = null;
            if(quantita_s!=null)
                Quantita = Integer.parseInt(quantita_s);
            String prezzo_s=request.getParameter("Prezzo");
            Double Prezzo = null;
            if(prezzo_s!=null)
                Prezzo=Double.parseDouble(prezzo_s);
            String Artista = request.getParameter("Artista");
            Utente u= (Utente) snn.getAttribute("utente");
            if(Titolo!=null&&image!=null&&Quantita!=null&&Prezzo!=null&&u!=null) {
                if(u.isAdmin_bool()) {
                    System.out.println("---(tutti i campi corretti)---");
                    String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists())
                        uploadDir.mkdir();
                    String Titol = Titolo.replace(" ", "_");
                    String fileName = getFileName(image);
                    String[] arr = fileName.split("[.]");
                    String estenzione = arr[arr.length-1];
                    Titol = Titol + "." + estenzione;
                    fileName = Titol;
                    image.write(uploadPath + File.separator + fileName);
                    //request.setAttribute("message", "File " + fileName + " has uploaded successfully!");
                    ArrayList<Tag> list = (ArrayList<Tag>) snn.getAttribute("tags");
                    ArrayList<Tag> list_tag = new ArrayList<>();
                    if (list != null)
                        for (int i = 0; i < list.size(); i++) {
                            if (request.getParameter(list.get(i).getNome()) != null) {
                                System.out.println("prova " + request.getParameter(list.get(i).getNome()));
                                list_tag.add(list.get(i));
                            }
                        }
                    ListaDisponibiliDAO service = new ListaDisponibiliDAO();
                    Vinile v = new Vinile();
                    v.setUrl(File.separator + UPLOAD_DIRECTORY + File.separator + Titol);
                    v.setPrezzo(Prezzo);
                    v.setTitolo(Titolo);
                    v.setArtista(Artista);
                    v.setTags(list_tag);
                    service.insertVinil(v, Quantita);
                    response.sendRedirect("./Admin?src=adminVinile");
                }else{
                    response.sendError(500);
                }
            }
            else{
                response.sendError(500);
            }
        }else{
            response.sendError(500);
        }
    }

}
