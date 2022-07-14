package Controller;

import Model.ListaDisponibiliDAO;
import Model.Tag;
import Model.Vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

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

        String uploadPath = getServletContext().getRealPath("")  + UPLOAD_DIRECTORY;
        System.out.println("1");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdir();
        String Titolo=request.getParameter("Titolo");
        String Titol=Titolo.replace(" ","_");
        Part image=request.getPart("Upload");
        String fileName = getFileName(image);
        String []arr=fileName.split("[.]");
        String estenzione=arr[1];
        Titol=Titol+"."+estenzione;
        fileName=Titol;
        image.write(uploadPath + File.separator + fileName);
        System.out.println("111");
        request.setAttribute("message", "File " + fileName + " has uploaded successfully!");
        Integer Quantita = Integer.parseInt(request.getParameter("Quantita"));
        Double Prezzo=Double.parseDouble(request.getParameter("Prezzo"));
        String Artista=request.getParameter("Artista");
        HttpSession snn=request.getSession();
        ArrayList<Tag> list= (ArrayList<Tag>) snn.getAttribute("Tags");
        ArrayList<Tag> vinil =new ArrayList<>();
        if(list!=null)
            for(int i=0;i<list.size();i++){
                if(request.getParameter(list.get(i).getNome())!=null){
                    System.out.println("prova "+request.getParameter(list.get(i).getNome()));
                    vinil.add(list.get(i));
                }
            }
        System.out.println("1111");
        ListaDisponibiliDAO service=new ListaDisponibiliDAO();
        Vinile v=new Vinile();
        v.setUrl(File.separator+UPLOAD_DIRECTORY+File.separator+Titol);
        v.setPrezzo(Prezzo);
        v.setTitolo(Titolo);
        v.setArtista(Artista);
        v.setTags(vinil);
        service.insertVinil(v,Quantita);
        System.out.println("11111");
        response.sendRedirect("./Admin?src=adminVinile");
        /*
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin?src=adminVinile");
            dispatcher.forward(request, response);
        */
    }

}
