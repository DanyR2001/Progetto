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
    <title>Home - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/homepage.css" type="text/css"/>
    <link rel="stylesheet" href="css/header.css" type="text/css">

    <script src="./lib/jquery-3.6.0.js"> </script>

    <script>

        $(document).ready(function(){
            $("#search-box").keyup(function(){
                $.ajax({
                    type: "POST", //tipo di richiesta
                    url: "SuggestName", //url a cui inviare la richiesta
                    data:'keyword='+ $(this).val(), //dati passati al server: passa cosa c'è scritto nella barra di ricerca
                    success: function(data){
                        $("#suggestion-box").show(); //rende visibile il div
                        $("#suggestion-box").html(data);
                    }
                });
            });
        });

        $(document).click(function (e) {
            var target=$(e.target);
            if(!target.hasClass("c"))
                $("#suggestion-box").hide();
        })

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
    <img src="img/vynil.png" class="vinyl" alt="vinyl">
    <a href="index.jsp" class="logo">LostInTheLoop</a>

    <input type="checkbox" id="checkbox_toggle" />
    <label for="checkbox_toggle" class="hamburger">&#9776;</label>

    <a href="carrello.jsp" id="cart2"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter2"><%=carrello.getNumItem()%></span></a>
    <a href="Search?String=" class="ricerca"><img src="img/loupe.png"></a>

    <nav class="header-right" id="myLinks">
        <div class = "Search">
            <input type="text" class="c" id="search-box" placeholder="Search.." onkeypress="search()" >
                <div id="suggestion-box" class="c"></div>
        </div>
        <a href="index.jsp" ><p>Home</p><img src="img/home.png" alt="homepage" tooltip="Home"></a>
        <a href="AreaPersonale"><p>Profilo</p><img src="img/user%20(2).png" alt="profile">
            <% if(u!=null) { %>
            <span id="user"><%=u.getNome()%></span>
            <%}%>
        </a>
        <a href="carrello.jsp" id="cart1"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter1"><%=carrello.getNumItem()%></span></a>
        <%if(u==null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>

<main class="content">



    <div class="slider-wrap">

            <%
                ListaVinili list1 = ((ListaVinili) snn.getAttribute("libreria"));
                ArrayList<Tag> tags = (ArrayList<Tag>) snn.getAttribute("tags");
                for (Tag tag : tags) {
                    ListaVinili list = list1.getAvailableVinili().getFromTag(tag);
                    if (list.size() > 0 && list.toShow(carrello)) {
            %>
        <section class="slider">
            <div class="tag">
                <h2><%= tag.getNome() %></h2>
            </div>
            <div class="item-wrapper">

            <%
                        for (int i = 0; i < list.size(); i++) {
                        Prodotto temp = carrello.getItem(list.get(i));                  //viene controllata la disponibilità dei vinili in base a quelli che stanno nel carrello
                            if (temp != null) {
                                if ((list.getMaxDisp(i) - temp.getQuantita()) > 0) {
            %>



                        <a href="item.jsp?id=<%=list.get(i).getPK()%>" class="item-reference">
                            <div class="item-container">
                                <div class="item">
                                    <img class="item-img" src="<%=application.getContextPath()%><%=list.get(i).getUrl()%>" alt="<%=list.get(i).getTitolo()%>">
                                    <div class="item-info">
                                        <p class="item-titolo"><%=list.get(i).getTitolo()%></p>
                                        <p class="item-artista"><%=list.get(i).getArtista()%></p>
                                    </div>
                                </div>
                            </div>
                        </a>



            <%                  }
                            } else {
            %>

                        <a href="item.jsp?id=<%=list.get(i).getPK()%>" class="item-reference">
                            <div class="item-container">
                                <div class="item">
                                    <img class="item-img" src="<%=application.getContextPath()%><%=list.get(i).getUrl()%>" alt="<%=list.get(i).getTitolo()%>">
                                    <div class="item-info">
                                        <p class="item-titolo"><%=list.get(i).getTitolo()%></p>
                                        <p class="item-artista"><%=list.get(i).getArtista()%></p>
                                    </div>
                                </div>
                            </div>
                        </a>


            <%
                             }
                        }
            %>
            </div>
        </section>
            <%
                    }

                }
            %>

    </div>
</main>

<footer class="footer">
    <div class="footer-info">
        <a href="index.jsp" class="footer-link" >Home</a>
        <%if(u==null){%>
        <a href="access.jsp" class="footer-link" >Login</a>
        <%} else {%>
        <a href="Logout" class="footer-link">Logout </a>
        <%}%>
        <a href="carrello.jsp" class="footer-link">Carrello</a>
        <a href="Search?String=" class="footer-link">Ricerca</a>
    </div>
</footer>

</body>
</html>