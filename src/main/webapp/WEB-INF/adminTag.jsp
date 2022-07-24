<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.ListaVinili" %>
<%@ page import="Model.Tag" %>
<%@ page import="Model.Vinile" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="css/iframe.css" type="text/css"/>
</head>
<body>
    <main id="mainV">
        <a href="RedirectTo?src=Tag" ><img src="img/add.png" class="image"><p>Aggiungi un nuovo tag</p></a>
        <div class="tag">
            <table class="tag-table">
                <%
                    ArrayList<Tag> lib= (ArrayList<Tag>) session.getAttribute("tags");
                    if(lib!=null){
                %>
                <tr>
                    <td class="al"><h3>Nome tag</h3></td>
                    <td class="al"><h3>Id</h3></td>
                    <td class="al"></td>
                </tr>
                <%
                    for(int i=0;i<lib.size();i++){
                        Tag t=lib.get(i);
                %>
                <tr>
                    <td><%=t.getNome()%></td>
                    <td class="center"><%=t.getId_tag()%></td>
                    <td class="center-link">
                        <div class="modify">
                            <a href="RedirectTo?src=Tag&id=<%=t.getId_tag()%>"><p>Modifica tag</p><img src="img/edit-button.png"></a>
                        </div>
                    </td>
                </tr>
                <% }
                }
                %>
            </table>
        </div>
    </main>

</body>
</html>
