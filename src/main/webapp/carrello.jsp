<%@ page import="java.util.ArrayList" %>
<%@ page import="Model.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    HttpSession snn=request.getSession();
    Ordine carrello= (Ordine) snn.getAttribute("carrello");
    Utente u = (Utente) snn.getAttribute("utente");
    ListaVinili service=(ListaVinili) snn.getAttribute("libreria");
    ArrayList<Vinile> listaRimossi= (ArrayList<Vinile>) snn.getAttribute("removedVinil");
    Boolean admin=false;
    if(u!=null)
        if(u.isAdmin_bool()){
            admin=true;
        }
%>
<head>
    <title>Carrello - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>
    <link rel="stylesheet" href="css/carrello.css" type="text/css"/>

    <script src="./lib/jquery-3.6.0.js"> </script>

    <script>
        <%if(admin){%>
        window.location.href = "./Admin";
        <% }%>
        function myFunction() {
            var x = document.getElementById("snackbar");
            if(x!=null) {
                x.className = "show";
                setTimeout(function () {
                    x.className = x.className.replace("show", "");
                }, 3000);
            }
        }

        $(document).ready(function(){
            myFunction();
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

        function submit(i){
            $("#"+i).submit;
        }

        $(document).click(function (e) {
            var target=$(e.target);
            if(!target.hasClass("c"))
                $("#suggestion-box").hide();
            else
                $("#suggestion-box").show();
        })

        //To suggest vinil name
        function selectSuggest(val) {
            var id="#"+val;
            var value=$(id).attr("value");
            $("#search-box").val(value);
            $("#suggestion-box").hide();
            window.location.href = "./item.jsp?id="+val;
        }
        function search(){
            var val= document.getElementById("search-box").value;
            if (event.keyCode == 13)
                window.location.href = "./Search?String="+val;
        }

    </script>
</head>
<body>
<%

    if(listaRimossi!=null) {
        for(Vinile v :listaRimossi) {
%>
    <div id="snackbar">"<%=v.getTitolo()%>" rimosso dal carrello.</div>
<%
        }
        snn.setAttribute("removedVinil",null);
    }
%>
    <header class="header">
    <img src="img/vynil.png" class="vinyl" alt="vinyl">
    <a href="index.jsp" class="logo">LostInTheLoop</a>

    <input type="checkbox" id="checkbox_toggle" />
    <label for="checkbox_toggle" class="hamburger">&#9776;</label>

    <a href="carrello.jsp" id="cart2"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter2"><%=carrello.getNumItem()%></span></a>
    <a href="Search?String=" class="ricerca"><img src="img/loupe.png"></a>

    <nav class="header-right" id="myLinks">
        <div class = "Search">
            <input type="text" class="c" id="search-box" placeholder="Cerca.." onkeypress="search()" >
            <div id="suggestion-box" class="c"></div>
        </div>
        <a href="index.jsp" ><p>Home</p><img src="img/home.png" alt="homepage" tooltip="Home"></a>
        <a href="AreaPersonale"><p>Profilo</p><img src="img/user%20(2).png" alt="profile">
            <% if(u != null) { %>
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
                <%
                    if(carrello.getCarrello().size()>0) {

                %>
                <div class="cart">
                    <div class="cart-text">
                        <h1>Carrello</h1>
                    </div>
                    <table>
                        <tr class="column-name">
                            <td></td>
                            <td><h2>Prodotto</h2></td>
                            <td class="al"><h2>Quantità</h2></td>
                            <td class="al"><h2>Prezzo</h2></td>
                            <td class="al"><h2>Subtotale</h2></td>
                        </tr>
                    <%
                        for(int i = 0; i < carrello.getNumItem(); i++) {
                            Prodotto p = carrello.getCarrello().get(i);
                    %>
                        <form action="UpdateCarrello" id="<%=i%>">
                            <input type="hidden" name="index" value="<%=i%>">
                            <tr class="table-item">

                                <td><img src="<%=application.getContextPath()%><%=p.getArticolo().getUrl()%>" alt="<%=p.getArticolo().getTitolo()%>" class="product-img"></td>
                                <td class="to-hide"><a href="item.jsp?id=<%=p.getArticolo().getPK()%>" class="product-titolo"><%=p.getArticolo().getTitolo()%></a></td>
                                <td class="quant">
                                    <div class="to-show">
                                        <a href="item.jsp?id=<%=p.getArticolo().getPK()%>" class="product-titolo"><%=p.getArticolo().getTitolo()%></a>
                                        <p><%=p.getArticolo().getPrezzo()%></p>
                                    </div>
                                    <div class="rem-qu">
                                        <select name="quantita" onchange="submit(<%=i%>)">
                                            <%
                                                for(int j=0;j<=service.getQuantitaVin(p.getArticolo());j++){
                                                    if(j==p.getQuantita()){%>
                                            <option selected="selected"><%=j%></option>
                                            <%}
                                            else{%>
                                            <option><%=j%></option>
                                            <%}
                                            }
                                            %>
                                        </select>
                                        <a href="UpdateCarrello?index=<%=i%>" class="remove to-show"><img src="img/delete.png" alt="delete"></a>
                                    </div>

                                </td>
                                <td class="to-hide" id="prezzo"><%=p.getArticolo().getPrezzo()%></td>
                                <td class="to-hide" id="subtotale"><%=p.getPrezzo()%></td>
                                <td class="to-hide"><a href="UpdateCarrello?index=<%=i%>" class="remove"><img src="img/delete.png" alt="delete"></a></td>
                            </tr>
                        </form>
                        <%
                        }
                        %>
                        <tr>
                            <td class = "empty-td"></td>
                            <td class = "empty-td"></td>
                            <td class = "empty-td"></td>
                            <td class="al"><h2>Totale</h2></td>
                            <td class="al"><h2><%=carrello.getPrezzo()%></h2></td>
                            <td></td>
                        </tr>

                    </table>

                    <form action="RedirectOrder" class="submit-order">
                        <input type="submit" value="Completa ordine" class="submit-order-btn">
                    </form>
                </div>
                <%
                    }  else {
                %>
                <div class="empty-cart">
                    <img src="img/cart.png" alt="cart">
                    <div class="empty-cart-text">
                        <h1>Carrello vuoto</h1>
                    </div>

                </div>
                <%
                    }
                %>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="footer-info">
            <a href="index.jsp" class="footer-link" >Home</a>
            <%if(u == null){%>
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
