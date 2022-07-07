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
        <form action="RemoveTag">
            <td><%=t.getNome()%></td><td><%=t.getId_tag()%></td><td><input type="hidden" name="ID_tag" value="<%=t.getId_tag()%>"><input type="submit" value="Elimina tag"></td>
        </form>
    </tr>
<% }
}
%>
</table>

</body>
</html>
