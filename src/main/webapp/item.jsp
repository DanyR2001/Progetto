<%@ page import="Model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<%
    Integer Id = null;
    try {
        Id = Integer.parseInt(request.getParameter("id"));
    }catch (NumberFormatException e){
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }
    HttpSession snn = request.getSession();
    ListaVinili service = (ListaVinili) snn.getAttribute("libreria");
    Vinile v = service.findViniliFromId(Id);
    if(v==null) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
        dispatcher.forward(request, response);
    }
    Ordine carrello = (Ordine) snn.getAttribute("carrello");
    Prodotto eq = null;
    Utente u = (Utente) snn.getAttribute("utente");
    if(u != null)
        if(u.isAdmin_bool()) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
            dispatcher.forward(request, response);
        }
    if(carrello != null)
        if(carrello.getCarrello() != null)
            eq = carrello.getItem(v);
%>

<head>
    <title><%=v.getArtista()%> - <%=v.getTitolo()%> </title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>
    <link rel="stylesheet" href="css/item.css" type="text/css"/>


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
            else
                $("#suggestion-box").show();
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
        <%if(u == null){%>
        <a href="access.jsp"><p>Login</p><img src="img/enter.png" alt="login"></a> <!--se non c'è l'utente appare il login -->
        <%} else {%>
        <a href="Logout"><p>Logout</p><img src="img/logout.png" alt="logout"></a> <!-- se c'è, logout -->
        <%}%>
    </nav>

</header>

<main class="cart-page">
    <div class="box">
        <div class="inner-box">

                <div class="item-image">
                    <img src="<%=application.getContextPath()+ v.getUrl()%>">
                </div>
                <div class="item-info">

                    <form action="AddItem" class="item-info-form">

                        <h1><%=v.getTitolo()%></h1>
                        <sub><%=v.getArtista()%></sub>

                        <h2 class="prezzo"><%=v.getPrezzo()%>€</h2>

                        <div class="quantita">
                            <h3 class="inline">Quantit&#224;</h3>
                            <%
                                if (eq!=null){
                                    if(service.getMaxDispId(v.getPK())-eq.getQuantita()==0) {%>
                            <h3 class="inline">ESAURITO</h3>
                        </div >
                        <input value="Aggiungi al carrello" class="button" disabled>
                            <%}
                            else {%>
                            <input type="number" name="quantita" min="1" value="1" max="<%=(service.getMaxDispId(v.getPK()) - eq.getQuantita())%>" >
                        </div>
                            <input type="submit" value="Aggiungi al carrello" class="button">
                            <%}
                            } else{
                                if(service.getMaxDispId(v.getPK())!=0){%>
                            <input type="number" name="quantita" min="1" value="1" max="<%=service.getMaxDispId(v.getPK())%>">
                        </div>
                            <input type="submit" value="Aggiungi al carrello" class="button">
                            <%}
                            else {%>
                            <h3 class="inline">ESAURITO</h3>
                        </div>
                            <input value="Aggiungi al carrello" class="button" disabled>
                            <%}
                            }
                            %>

                        <input type="hidden" name="id" value="<%=v.getPK()%>">
                    </form>

                    <div class="item-info-tag">
                        <%
                            if(v.getTags()!=null){
                                if(v.getTags().size()>0){%>
                                <div class="item-info-tag-name">
                                    <h2>Tag:</h2>
                                </div>
                                <div class="item-info-tag-element">
                                    <%for(int i=0;i<v.getTags().size();i++){%>
                                    <div class="tag">
                                        <a href="Search?search=&cheackbox=<%=v.getTags().get(i).getNome()%>&choose=Tag">
                                            <h4><%=v.getTags().get(i).getNome()%></h4>
                                        </a>
                                    </div>
                                 <% }%>
                                </div>
                                <%}
                            }%>
                    </div>
                </div>
        </div>
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
