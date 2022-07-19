<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.ListaVinili" %>
<%@ page import="Model.Tag" %>
<%@ page import="Model.Vinile" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 14/06/22
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<a href="RedirectTo?src=Tag" >Aggiungi un nuovo tag</a>
<table>
<%
    ArrayList<Tag> lib= (ArrayList<Tag>) session.getAttribute("tags");
    if(lib!=null){
        %>
    <tr><td>Nome tag</td><td>Id</td><td></td></tr>
    <%
        for(int i=0;i<lib.size();i++){
            Tag t=lib.get(i);
%>
    <tr>
        <td><%=t.getNome()%></td><td><%=t.getId_tag()%></td><td><a href="RedirectTo?src=Tag&id=<%=t.getId_tag()%>">Modifica tag</a></td>
    </tr>
<% }
}
%>
</table>

</body>
</html>
