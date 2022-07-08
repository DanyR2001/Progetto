<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<%
        HttpSession snn = request.getSession();
        Ordine carrello = (Ordine) session.getAttribute("carrello");
        Utente u = (Utente) snn.getAttribute("utente");
        if(u!=null)
            if(u.isAdmin_bool()){
                RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
                dispatcher.forward(request, response);
            }
%>
<head>
    <title>Homepage</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="./css/style1.css" type="text/css">
    <link rel="stylesheet" href="./css/style.css" type="text/css"/>

    <script src="./lib/jquery-3.6.0.js"> </script>

    <script>
        $(document).ready(function(){
            $("#search-box").keyup(function(){
                $.ajax({
                    type: "POST", //tipo di richiesta
                    url: "SuggestName", //url a cui inviare la richiesta
                    data:'keyword='+ $(this).val(), //dati passati al server: passa cosa c'è scritto nella barra di ricerca
                    success: function(data){
                        $("#suggestion-box").show(); //mostra i suggeriemnti
                        $("#suggestion-box").html(data);
                        $("#search-box").css("background","#FFF");
                    }
                });
            });
        });

        //To select country name
        function selectSuggest(val) {
            $("#search-box").val(val);
            $("#suggestion-box").hide();
            var id=document.getElementById(val).getAttribute("value");
            window.location.href = "./item.jsp?id="+id;
        }
        function search(){
            var val= document.getElementById("search-box").value;
            if (event.keyCode == 13)
                window.location.href = "./Search?String="+val;
        }
    </script>

</head>

<body>
<header class="header">
    <img src="img/vynil.png" class="vynil" alt="vynil">
    <a href="index.jsp" class="logo">LostInTheLoop</a>

    <input type="checkbox" id="checkbox_toggle" />
    <label for="checkbox_toggle" class="hamburger">&#9776;</label>
    <a href="carrello.jsp" id="cart2"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter2"><%=carrello.getNumItem()%></span></a>
    <a href="Search?String=" class="ricerca"><img src="img/loupe.png"></a>

    <nav class="header-right" id="myLinks">
        <div class = "Search">
            <input type="text" id="search-box" placeholder="Search.." onkeypress="search();">
            <div id="suggestion-box"></div>
        </div>
        <a href="index.jsp"><p>Homepage</p><img src="img/home.png" alt="homepage"></a>
        <a href="AreaPersonale"><p>Profilo</p><img src="img/user%20(2).png" alt="profile"></a>
        <a href="carrello.jsp" id="cart1"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter1"><%=carrello.getNumItem()%></span></a>
        <%if(u==null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Login</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>


    <!--
    <li><a href="carrello.jsp" class="spec">

    <%
      /*  if(u!=null) {
            System.out.println(" utente " + u.getNome());
            out.print("<li> <a href='Logout'>Logout</a></li>\n");
            out.print("<li> <a>Bentornato " + u.getNome() + " continua con gli acquisti:</a></li>\n"); da aggiungere
        }

       */
    %>
    -->


<div>
    <ul class="hs full">
        <%
            ListaVinili list1= ((ListaVinili) snn.getAttribute("libreria"));
            ArrayList<Tag> tags= (ArrayList<Tag>) snn.getAttribute("tags");
            for(int j=0;j<tags.size();j++) {
                ListaVinili list = list1.getAvailableVinili().getFromTag(tags.get(j));
                if(list.size()>0) {
                    out.print("<p style='color:white '>vinili tipo tag: "+tags.get(j).getNome()+"</p>");
                    for (int i = 0; i < list.size(); i++) {
                        Prodotto temp = carrello.getItem(list.get(i));
                        if (temp != null) {
                            if (list.getMaxDisp(i) - temp.getQuantita() > 0) {
                                out.print("<li class=\"item\"><a href=\"item.jsp?id=" + list.get(i).getPK() + "\"> <img src=\"" + application.getContextPath() + list.get(i).getUrl() + "\">" + list.get(i).getTitolo() + "</a></li>\n");
                            }
                        } else {
                            System.out.println("disponibilità " + list.getMaxDisp(i));
                            out.print("<li class=\"item\"><a href=\"item.jsp?id=" + list.get(i).getPK() + "\"> <img src=\"" + application.getContextPath() + list.get(i).getUrl() + "\">" + list.get(i).getTitolo() + "</a></li>\n");
                            //out.print("<li class=\"item\"><img src=\"" + application.getContextPath()+list.get(i).getUrl() + "\"><a href=\"item.jsp?id=" + list.get(i).getPK() + "\"> " + list.get(i).getTitolo() + "</a></li>\n");
                        }
                    }
                }
                out.print("</ul>");
                out.print("<br>");
                out.print("<ul class=\"hs full\">");
            }
        %>

    </ul>
</div>
<br/>
</body>
</html>