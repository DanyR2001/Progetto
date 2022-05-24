<%@ page import="java.util.Enumeration" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<%
        HttpSession snn=request.getSession();
        ordine carrello= (ordine) session.getAttribute("carrello");
        utente u = (utente) snn.getAttribute("utente");
        if(u!=null)
            if(u.isAdmin_bool()){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
                dispatcher.forward(request, response);
            }
%>
<head>

    <title>JSP - Hello World</title>
    <link rel="stylesheet"
          href="./css/style.css"
          type="text/css"/>
    <script>

       /* var root = document.documentElement;
        const lists = document.querySelectorAll('.hs');

        lists.forEach(el => {
            const listItems = el.querySelectorAll('li');
            const n = el.children.length;
            el.style.setProperty('--total', n);
        });*/
    </script>
</head>
<body>
<ul class="navbar">
    <li><a href="index.jsp" class="spec">Home</a></li>
    <%if(u==null){%>
    <li><a href="access.jsp" class="spec">Accedi/Registrati</a></li>
    <%}%>
    <li class="dropdown">
        <a href="javascript:void(0)" class="dropbtn">Dropdown</a>
        <div class="dropdown-content">
            <a href="#" class="element">Link 1</a>
            <a href="#" class="element">Link 2</a>
            <a href="#" class="element">Link 3</a>
        </div>
    </li>
    <li><a href="carrello.jsp" class="spec"><%
        if(carrello.getCarrello()!=null)
            out.print(carrello.getNumItem());
        else
            out.print(0);
    %> elementi nel carrello</a></li>

    <%
        if(u!=null) {
            System.out.println(" utente " + u.getNome());
            out.print("<li> <a href='Logout'>Logout</a></li>\n");
            out.print("<li> <a>Bentornato " + u.getNome() + " continua con gli acquisti:</a></li>\n");
        }
    %>
</ul>
<h1><%= "Hello World!" %>
</h1>
<div >
    <ul class="hs full">
        <%
            listaVinili list1= ((listaVinili) snn.getAttribute("libreria"));
            listaVinili list=list1.getAvableVinil();
            for(int i=0;i<list.size();i++) {
                prodotto temp=carrello.getItem(list.get(i));
                if(temp!=null) {
                    if (list.getMaxDisp(i) - temp.getQuantita() > 0) {
                        out.print("<li class=\"item\"><img src=\"" +application.getContextPath()+ list.get(i).getUrl() + "\"><a href=\"item.jsp?id=" + list.get(i).getPK() + "\"> " + list.get(i).getTitolo() + "</a></li>\n");
                    }
                }
                else{
                    System.out.println("disponibilità " + list.getMaxDisp(i));
                        out.print("<li class=\"item\"><img src=\"" + application.getContextPath()+list.get(i).getUrl() + "\"><a href=\"item.jsp?id=" + list.get(i).getPK() + "\"> " + list.get(i).getTitolo() + "</a></li>\n");
                    }
            }
        %>

    </ul>
</div>
<br/>
</body>
</html>