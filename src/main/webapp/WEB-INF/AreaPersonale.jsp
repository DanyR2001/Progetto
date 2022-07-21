<%@ page import="Model.OldOrder" %>
<%@ page import="Model.Ordine" %>
<%@ page import="Model.Prodotto" %>
<%@ page import="Model.Utente" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
  OldOrder list= (OldOrder) session.getAttribute("OldOrdini");
  Utente u= (Utente) session.getAttribute("utente");
  Ordine carrello = (Ordine) session.getAttribute("carrello");
  if(u!=null)
    if(u.isAdmin_bool()){
      RequestDispatcher dispatcher = request.getRequestDispatcher("/Admin");
      dispatcher.forward(request, response);
    }
%>
<head>

  <title>Area Personale - LP</title>

  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
  <link rel="stylesheet" href="css/header.css" type="text/css">
  <link rel="stylesheet" href="css/areaPersonale.css" type="text/css">

  <script src="./lib/jquery-3.6.0.js"> </script>

  <script>
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

    function show(x){
      $(".order-frame").hide();
      if(x==1){
        $(".title-order p").text("Modifica info");
        $(".form-modify").css("display", "flex");
        $(".pass-modify").hide();
        $("#bt1").attr("disabled",true);
        $("#bt2").attr("disabled",false);
      }
      else if(x==2){
        $(".title-order p").text("Cambia password");
        $(".pass-modify").css("display", "flex");
        $(".form-modify").hide();
        $("#bt1").attr("disabled",false);
        $("#bt2").attr("disabled",true);
      }
    }

    function cancel(x){
      $(".order-frame").show();
      $(".title-order p").text("I miei ordini");
      $("#bt1").attr("disabled",false);
      $("#bt2").attr("disabled",false);
      if(x==1) {
        $(".form-modify").hide();
      }else if(x==2){
        $(".pass-modify").hide();
      }
    }

  </script>
</head>
<body>
<%
  Boolean flag= (Boolean) session.getAttribute("noModify");
  if(flag!=null)
    if(flag) {

    %>
      <div id="snackbar">Non ci sono state modifiche.</div>
    <%
      session.setAttribute("noModify",null);
    }
  Boolean flag1= (Boolean) session.getAttribute("noPassCorrect");
  if(flag1!=null)
    if(flag1) {
    %>
      <div id="snackbar">La password non coincide, riprova.</div>
    <%
      session.setAttribute("noPassCorrect",null);
    }
      Boolean flag2= (Boolean) session.getAttribute("modify");
      if(flag2!=null)
        if(flag2) {
    %>
        <div id="snackbar">Modifiche avvenute correttamente.</div>
<%
      session.setAttribute("modify",null);
    }
%>
  <header class="header">
  <img src="img/vynil.png" class="vinyl" alt="vinyl">
  <a href="index.jsp" class="logo">LostInTheLoop</a>

  <input type="checkbox" id="checkbox_toggle" />
  <label for="checkbox_toggle" class="hamburger">&#9776;</label>

  <a href="carrello.jsp" id="cart2"><img src="img/shopping-cart.png" alt="cart"><span id="cart-counter2"><%=carrello.getNumItem()%></span></a>
  <a href="Search?String=" class="ricerca"><img src="img/loupe.png" alt="loupe"></a>

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
  <main class="cart-page">
    <div class="box">
      <div class="inner-box">
        <div class="user-info">

            <h1 class="welcome">Bentornato <%=u.getNome()%></h1>

          <fieldset class="user-information">
            <legend>Dati personali</legend>
            <div class="info">
              <p class="title">Mail</p>
              <p class="data"><%= u.getMail()%></p>
            </div>
            <div class="info">
              <p class="title">Data di nascita</p>
              <p class="data"> <%=u.getDataNascita().getDate()%>/<%=u.getDataNascita().getMonth()+1%>/<%=u.getDataNascita().getYear()+1900%></p>
            </div>
            <div class="info">
              <p class="title">Indirizzo</p>
              <p class="data"> Via <%=u.getVia()%> n°<%=u.getCivico()%>, <%=u.getCap()%></p>
            </div>
          </fieldset>

          <div class="buttons">
            <button id="bt1" onclick="show(1)" class="button">Modifica info</button>
            <button id="bt2" onclick="show(2)" class="button">Cambia password</button>
          </div>

        </div>

        <div class="orders">
          <div class="title-order">
            <p>I miei ordini</p>
          </div>

          <div class="order-info">

            <div class="margin">

              <div class="pass-modify">
                <form method="post" action="UpdatePassUser" id="UpPass">
                  <div class="input-wrap">
                    <label for="reg-pass">Inserisci la vecchia password:</label>
                    <input type="password" id="reg-pass-old" name="pass-old" class="input-field" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-pass">Inserisci la nuova password:</label>
                    <input type="password" id="reg-pass-new" name="pass-new" class="input-field" required>
                  </div>
                </form>

                <div class="buttons">
                  <input type="submit" value="Conferma" class="button" form="UpPass">
                  <button onclick="cancel(2)" class="button">Annulla</button>
                </div>
              </div>

              <div class="form-modify">

                <form action="UpdateUser" id="UpUser" method="post">
                  <input type="hidden" name="id" value="<%=u.getID()%>">
                  <div class="input-wrap">
                    <label for="reg-nome">Nome</label>
                    <input type="text" id="reg-nome" name="nome" class="input-field"  value="<%=u.getNome()%>" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-cognome">Cognome</label>
                    <input type="text" id="reg-cognome" name="cognome" class="input-field" value="<%=u.getCognome()%>" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-date">Data di nascita</label>
                    <input type="date" id="reg-date" name="date" class="input-field" placeholder="  " value="<%=u.getDataNascita()%>" onfocus="(this.type='date')" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-via">Via</label>
                    <input type="text" name="via" id="reg-via" class="input-field" value="<%=u.getVia()%>" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-civico">Civico</label>
                    <input type="number" name="civico" id="reg-civico" class="input-field" value="<%=u.getCivico()%>" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-cap">CAP</label>
                    <input type="number" name="cap" id="reg-cap" class="input-field" value="<%=u.getCap()%>" required>
                  </div>
                  <div class="input-wrap">
                    <label for="reg-pass">Inserire password per conferma</label>
                    <input type="password" id="reg-pass" name="pass" class="input-field" required>
                  </div>
                </form>

                <div class="buttons">
                    <input type="submit" value="Conferma" class="button" form="UpUser">
                    <button onclick="cancel(1)" class="button">Annulla</button>
                </div>

              </div>

              <div class="order-frame">
                <%
                  if(list!=null){
                    for(Ordine x :list.getList()){%>
                <div class="order">
                  <fieldset class="order-info-table">
                    <legend>Codice ordine <%=x.getCodice()%></legend>
                    <div class="info">
                      <p class="title">Data evasione</p>
                      <p class="data"><%=x.getDataEvasione().getDay()%>/<%=x.getDataEvasione().getMonth()+1%>/<%=x.getDataEvasione().getYear()%></p>
                    </div>
                    <div class="info">
                      <p class="title">Indirizzo di spedizione</p>
                      <p class="data"><%=x.getVia()%> <%=x.getCivico()%> <%=x.getCap()%></p>
                    </div>
                    <table >

                      <tr>
                        <td><h4>Prodotto</h4></td>
                        <td class="al"><h4>Quantità</h4></td>
                        <td class="al"><h4>Prezzo</h4></td>
                        <td class="al"><h4>Subtotale</h4></td>
                      <tr>
                          <%
            DecimalFormat dr=new DecimalFormat("0.00");
              for(Prodotto p: x.getCarrello()){%>
                      <tr>
                        <td><%=p.getArticolo().getTitolo()%></td>
                        <td class="quant"><%=p.getQuantita()%></td>
                        <td class="prezzo"><%=dr.format(p.getPrezzo()/p.getQuantita())%></td>
                        <td class="subtotale"><%=p.getPrezzo()%></td>
                      </tr>
                      <%}%>
                      <tr>
                        <td></td>
                        <td></td>
                        <td class="al"><h4>Totale</h4></td>
                        <td class="al"><h4><%=x.getPrezzo()%></h4></td>
                      </tr>
                    </table>
                  </fieldset>


                </div>
                <%
                  }
                } else {
                %>
                <h1>Ancora nessun ordine effettuato</h1>
                <%
                  }
                %>
              </div>

            </div>
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
