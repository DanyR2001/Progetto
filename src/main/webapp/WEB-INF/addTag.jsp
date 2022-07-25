<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.Tag" %>
<%@ page import="Model.Vinile" %>
<%@ page import="Model.ListaVinili" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="css/iframe.css" type="text/css"/>
</head>
<body>
    <main id="mainV">
        <div class="fix">
            <a href="Admin?src=adminTag"><img src="img/back-button.png" class="image"><p>Pagina precedenete</p></a>
        </div>
        <div class="box">
            <form action="AddTag">
                <div class="input-wrap">
                    <h1>Aggiunta tag</h1>
                </div>
                <div class="input-wrap">
                        <label for="Nome">Nome Tag</label>
                        <input id="Nome" type="text" name="Nome" required>
                </div>
                <div class="input-wrap">
                    Seleziona il vinile al quale lo vuoi collegare:
                </div>
                <div class="tag-list">
                                <%
                                   ListaVinili vin= (ListaVinili) session.getAttribute("libreria");
                                   if(vin != null) {
                                       ArrayList<Vinile> list = vin.getAllVinili();
                                       if (list != null) {
                                           for (int i=0;i<list.size();i++){
                                               %>
                                                <div class="margin">
                                                    <input type="checkbox" id="<%=list.get(i).getPK()%>" name="tag" value="<%=list.get(i).getPK()%>">
                                                    <lable for ="<%=list.get(i).getPK()%>"><%=list.get(i).getTitolo()%></lable>
                                                </div>
                                                <%
                                           }
                                       }
                                   }
                                %>
                            </div>
                <div class="button-add-tag">
                    <input type="submit" class="button" value="Aggiungi">
                </div>
            </form>
        </div>
    </main>
</body>
</html>
