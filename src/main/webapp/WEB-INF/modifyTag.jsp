<%@ page import="Model.Tag" %>
<%@ page import="Model.ListaVinili" %>
<%@ page import="Model.Vinile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <link rel="stylesheet" href="css/iframe.css" type="text/css"/>
</head>


<%
    Tag t= (Tag) session.getAttribute("Tag_selected");
    Boolean flag=false;
    if(t==null){
        flag=true;
    }
%>
<script>
    <%
    if(flag){%>
        window.location.href = "./error.jsp";
    <%}%>
</script>
<body>
<main id="mainV">
    <div class="fix">
        <a href="Admin?src=adminTag"><img src="img/back-button.png" class="image"><p>Pagina precedenete</p></a>
    </div>
    <div class="box" id="box-tag">
        <div class="input-wrap">
            <%if(t!=null){%>
                <h1>Modifiche al Tag "<%=t.getNome()%>"</h1>
            <%}%>
        </div>
        <%if(t!=null){%>
            <fieldset class="user-information">
                <legend>ID = <%=t.getId_tag()%></legend>
                <form action="ModNameTag">
                    <label for="nome">Nome Tag:</label>
                    <input type="text" value="<%=t.getNome()%>" id="Nome" name="Nome" required>
                    <input type="hidden" name="ID_tag" value="<%=t.getId_tag()%>">
                    <input type="submit" value="Modifica nome tag" class="button">
                </form>
                <form action="RemoveTag" id="remove-tag">
                    <input type="hidden" name="ID_tag" value="<%=t.getId_tag()%>">
                </form>
                <form action="UpdateTagList" class="tag-list" id="list-tag">
                    <%
                        ListaVinili lib= (ListaVinili) session.getAttribute("libreria");
                        ListaVinili lib_tag= (ListaVinili) session.getAttribute("List_tag_selected");
                        for(int i=0;i<lib.size();i++){
                            if(lib_tag.isPresent(lib.get(i))){
                    %>
                    <input type="checkbox" name="vin" id="<%=lib.get(i).getPK()%>" value="<%=lib.get(i).getPK()%>" checked>
                    <label for="<%=lib.get(i).getPK()%>"><%=lib.get(i).getTitolo()%></label> <br>
                    <% }
                    else{%>
                    <input type="checkbox" name="vin" id="<%=lib.get(i).getPK()%>" value="<%=lib.get(i).getPK()%>" >
                    <label for="<%=lib.get(i).getPK()%>"><%=lib.get(i).getTitolo()%></label> <br>
                    <%}
                    }
                    %>
                </form>
                <div>
                    <input type="submit" value="Aggiorna la lista" form="list-tag" class="button">
                    <input type="submit" value="Rimuovi il tag"  form="remove-tag" class="button" id="remove">
                </div>

        </fieldset>
    <%}%>
    </div>
</main>
</body>
</html>
