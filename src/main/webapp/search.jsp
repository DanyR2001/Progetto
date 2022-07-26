<%@ page import="Model.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="Model.Vinile" %>
<%@ page import="Model.Ordine" %>
<%@ page import="Model.Utente" %>
<%@ page import="java.util.Enumeration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<%
    HttpSession snn = request.getSession();
    Ordine carrello = (Ordine) session.getAttribute("carrello");
    Utente u = (Utente) snn.getAttribute("utente");
    String val= (String) session.getAttribute("String");
    List<Vinile> result= (List<Vinile>) session.getAttribute("listaResult");
    List<Tag> lista= (List<Tag>) session.getAttribute("tags");
    Boolean admin=false;
    if(u!=null)
        if(u.isAdmin_bool()){
            admin=true;
        }
%>
<head>
    <title>Ricerca - LP</title>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
    <link rel="stylesheet" href="css/header.css" type="text/css"/>
    <link rel="stylesheet" href="css/ricerca.css" type="text/css"/>

    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>

    <script src="./lib/jquery-3.6.0.js"> </script>
    <script>
        <%if(admin){%>
            window.location.href = "./Admin";
        <% }%>
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

    <main>
        <div class="search-panel" id="search-panel">
            <div class="panel">
                <div class="search">
                    <input type="text" id="search" name="search" value="<%=val%>" form="form1" placeholder="Cerca..">
                </div>
                <fieldset  class="tags">
                    <legend>Tag</legend>
                    <%
                        if(lista!=null)
                            for(Tag x: lista) {
                    %>
                    <input type="checkbox" form="form1" name="cheackbox" value="<%=x.getNome()%>" id="<%=x.getId_tag()%>">
                    <lable for="<%=x.getId_tag()%>"><%=x.getNome()%></lable>
                    <br>
                    <%
                            }
                    %>
                </fieldset>

                <fieldset class="tags">
                    <legend>Filtri</legend>
                    <input  class="filter" type="radio" id="testo" name="choose" value="Testo" form="form1" >
                    <label for="testo" class="filter-label">Testo</label>
                    <input class="filter" type="radio" id="tag" name="choose" value="Tag" form="form1" required>
                    <label for="tag" class="filter-label">Tag</label>
                </fieldset>

                <form action="Search" id="form1">
                    <input type="submit" value="Cerca" class="button">
                </form>
            </div>
        </div>

        <div class="show">

                <%
                    if(result != null) {
                        String choose = null;
                        String[] selected =null;
                        String testo=null;
                        Enumeration<String> name=request.getParameterNames();
                        while(name.hasMoreElements()){
                            String parameter=name.nextElement();
                            if(parameter.equals("search")||parameter.equals("cheackbox")||parameter.equals("choose")||parameter.equals("String")){
                                if(parameter.equals("choose"))
                                    choose=request.getParameter(parameter);
                                if(parameter.equals("cheackbox"))
                                    selected = request.getParameterValues(parameter);
                                if(parameter.equals("search")||parameter.equals("String")) {
                                    if(parameter.equals("String"))
                                        choose="Testo";
                                    testo = request.getParameter(parameter);
                                }
                                System.out.println(parameter);
                            }
                        }
                        if(choose!=null){
                            if(choose.equals("Tag")){
                %>
                <h3 class="tag">Tag ricercati: </h3>
                <%
                    if(selected!=null) {
                        for(int i=0;i<selected.length;i++){
                %>
                <h3 class="tag"> <%=selected[i]%> </h3>
                <%
                        }
                    }
                %>
                    <div class="show-item-wrap">
                        <div class="show-item">
                <%
                }
                else if(choose.equals("Testo")){
                %>

                <h3>Risultati ricerca per Testo: <%=testo%></h3>
                    <div class="show-item-wrap">
                        <div class="show-item">

                    <%
                            }
                        }
                        for(int i = 0; i < result.size(); i++){
                    %>

                    <a href="item.jsp?id=<%=result.get(i).getPK()%>" class="item-reference">
                        <div class="item-container">
                            <div class="item">
                                <img class="item-img" src="<%=application.getContextPath()%><%=result.get(i).getUrl()%>" alt="<%=result.get(i).getTitolo()%>">
                                <div class="item-info">
                                    <p class="item-titolo"><%=result.get(i).getTitolo()%></p>
                                    <p class="item-artista"><%=result.get(i).getArtista()%></p>
                                </div>
                            </div>
                        </div>
                    </a>
                    <%
                        }
                    } else {
                    %>
                    <p>Nessun Risultato</p>
                    <%
                        }
                    %>
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
