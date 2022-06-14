package Controller;

import Model.listaDisponibiliDAO;
import Model.tag;
import Model.vinile;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static Controller.Constants.DEFAULT_FILENAME;
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
        try {
            String fileName = "";
            System.out.println("11");
            for (Part part : request.getParts()) {
                System.out.println("£" + part);
                fileName = getFileName(part);
                if(!fileName.equals(DEFAULT_FILENAME)){
                    String []arr=fileName.split("[.]");
                    String estenzione=arr[1];
                    Titol=Titol+"."+estenzione;
                    fileName=Titol;
                }
                System.out.println("£" + fileName);
                part.write(uploadPath + File.separator + fileName);
            }
            System.out.println("111");
            request.setAttribute("message", "File " + fileName + " has uploaded successfully!");
            Integer Quantita=Integer.parseInt(request.getParameter("Quantita"));
            Double Prezzo=Double.parseDouble(request.getParameter("Prezzo"));
            String Artista=request.getParameter("Artista");
            HttpSession snn=request.getSession();
            ArrayList<tag> list= (ArrayList<tag>) snn.getAttribute("Tags");
            ArrayList<tag> vinil=new ArrayList<>();
            if(list!=null)
                for(int i=0;i<list.size();i++){
                    if(request.getParameter(list.get(i).getNome())!=null){
                        System.out.println("prova "+request.getParameter(list.get(i).getNome()));
                        vinil.add(list.get(i));
                    }
                }
            System.out.println("1111");
            listaDisponibiliDAO service=new listaDisponibiliDAO();
            vinile v=new vinile();
            v.setUrl(File.separator+UPLOAD_DIRECTORY+File.separator+Titol);
            v.setPrezzo(Prezzo);
            v.setTitolo(Titolo);
            v.setArtista(Artista);
            v.setTags(vinil);
            service.insertVinil(v,Quantita);

        } catch (FileNotFoundException fne) {
            request.setAttribute("message", "There was an error: " + fne.getMessage());
            System.out.println("errore");
        }
        System.out.println("11111");
        response.sendRedirect("./Admin?src=adminVinile");
        /*
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin?src=adminVinile");
        dispatcher.forward(request, response);*/
    }

}