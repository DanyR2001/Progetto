<%@ page import="Model.tag" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.listaVinili" %>
<%@ page import="Model.tagsDAO" %>
<%@ page import="Model.vinile" %><%--
  Created by IntelliJ IDEA.
  User: danielerusso
  Date: 31/05/22
  Time: 22:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<%
    String val= (String) session.getAttribute("String");
    List<vinile> result= (List<vinile>) session.getAttribute("listaResult");
    List<tag> lista= (List<tag>) session.getAttribute("tags");
%>
<table>
    <tr><th>Ricerca</th><th>Tag:</th></tr>
    <tr>
        <td>
            <input type="text" id="search" name="search" value="<%out.print(val);%>" form='form1'>
        </td>
        <td>
        <%
            if(lista!=null)
                for(tag x: lista)
                    out.print("<input type='checkbox' form='form1' name='cheackbox' value=\""+x.getNome()+"\" id='"+x.getId_tag()+"'><lable for='"+x.getId_tag()+"'>"+x.getNome()+"</lable>");
        %>
    </td></tr>
    <tr><td>Filtra per:</td><td>
        <form action="Search" id="form1">
        <input type="submit" value="Cerca">
        </form>
    </td>
    </tr>
    <tr><td>
            <input type="radio" id="testo" name="choose" value="Testo" form='form1' required>
            <label for="testo">Casella di testo</label>
        </td><td>
            <input type="radio" id="tag" name="choose" value="Tag" form='form1'>
            <label for="tag">Tag</label>
        </td>
    </tr>
    <%
    if(result!=null)
        for(int i=0;i<result.size();i++){
            out.print("<tr><td> <img style='height=50px;width: 50px' src=\"" +application.getContextPath()+ result.get(i).getUrl() + "\"></td><td> <a href=\"item.jsp?id=" + result.get(i).getPK() + "\">"+ result.get(i).getTitolo() +"</a></td></tr>\n");
        }
    else
        out.print("Nessun risultato");
    %>
</table>

</body>
</html>
