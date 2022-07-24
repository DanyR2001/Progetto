<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Tag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/iframe.css" type="text/css"/>
</head>
<body>
<main id ="mainV">
    <div class="fix">
        <a href="Admin?src=adminVinile"><img src="img/back-button.png" class="image"><p>Pagina precedenete</p></a>
    </div>
    <div class="box">
        <form method="post" action="NewVinil" enctype="multipart/form-data">
            <div class="input-wrap center">
                <h1>Aggiunta vinile</h1>
            </div>
            <div class="input-wrap">
                <label for="titolo">Titolo</label>
                <input type="text" id="titolo" name="Titolo" required>
            </div>
            <div class="input-wrap">
                <label for="Prezzo">Prezzo</label>
                <input type="number" min="0" id="Prezzo" step=".01" name="Prezzo" required>
            </div>
            <div class="input-wrap">
                <label for="Quantita">Quantita</label>
                <input type="number" min="0" id="Quantita" name="Quantita" required>
            </div>
            <div class="input-wrap">
                <label for="Artista">Artista</label>
                <input type="text" id="Artista" name="Artista" required>
            </div>
            <div class="input-wrap">
                <label for="Upload">Immagine</label>
                <input type="file" id="Upload" accept=".png,.jpeg,.jpg,.svg" name="Upload" required />
            </div>
            <div class="tags">
            <%
                ArrayList<Tag> list= (ArrayList<Tag>) session.getAttribute("tags");
                if(list!=null)
                    for(int i=0;i<list.size();i++) {
                        %>
                        <div class="input-wrap">
                            <lable for="<%=list.get(i).getNome()%>"><%=list.get(i).getNome()%></lable>
                            <input type="checkbox" id="<%=list.get(i).getNome()%>" name="<%=list.get(i).getNome()%>" value="<%=list.get(i).getId_tag()%>">
                        </div>
                        <%
                    }
            %>
            </div>
            <div class="input-wrap center">
                <input type="submit" class="button" name="carica">
            </div>
        </form>
    </div>
</main>
</body>
</html>
