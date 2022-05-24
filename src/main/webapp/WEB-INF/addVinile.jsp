<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.tag" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 24/05/22
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form method="post" action="NewVinil" enctype="multipart/form-data">
    Titolo:<input type="text" name="Titolo">
    Prezzo:<input type="number" step=".01" name="Prezzo">
    Quantita:<input type="number"  name="Quantita">
    Artista:<input type="text" name="Artista">
    Immgaine:<input type="file" name="Upload" />
    <%
        ArrayList<tag> list= (ArrayList<tag>) session.getAttribute("Tags");
        if(list!=null)
            for(int i=0;i<list.size();i++) {
                out.print("<input type='checkbox' id='"+list.get(i).getNome()+"' name='" + list.get(i).getNome() + "' value='" + list.get(i).getId_tag() + "'>");
                out.print("<lable for ='"+list.get(i).getNome()+"'>"+list.get(i).getNome()+"</lable>");
            }
    %>
    <input type="submit" name="carica">
</form>

</body>
</html>
