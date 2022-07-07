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
    <script src="./lib/jquery-3.6.0.js"></script>
    <script>
        $(document).ready(function(){
            $("#search-box").keyup(function(){
                $.ajax({
                    type: "POST",
                    url: "SuggestName",
                    data:'keyword='+$(this).val(),
                    beforeSend: function(){
                        $("#search-box").css("background","#FFF no-repeat 165px");
                    },
                    success: function(data){
                        $("#suggesstion-box").show();
                        $("#suggesstion-box").html(data);
                        $("#search-box").css("background","#FFF");
                    }
                });
            });
        });
        //To select country name
        function selectSuggest(val) {
            $("#search-box").val(val);
            $("#suggesstion-box").hide();
            var id=document.getElementById(val).getAttribute("value");
            window.location.href = "./item.jsp?id="+id;
        }
        function serch(){
            var val=document.getElementById("search-box").value;
            if (event.keyCode == 13)
                window.location.href = "./Search?String="+val;
        }
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
    <li><a href="Search?String=" class="spec">Ricerca</a></li>
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
    <li> <a href='AreaPersonale'>Area Personale</a></li>
    <li>
        <div class="Search">
            <input type="text" id="search-box" placeholder="Nome vinile" onkeypress="serch();" />
            <div id="suggesstion-box"></div>
        </div>
    </li>
</ul>
<h1><%= "Hello World!" %>
</h1>
<div >
    <ul class="hs full">
        <%
            listaVinili list1= ((listaVinili) snn.getAttribute("libreria"));
            ArrayList<tag> tags= (ArrayList<tag>) snn.getAttribute("tags");
            for(int j=0;j<tags.size();j++) {
                listaVinili list = list1.getAvableVinil().getFromTag(tags.get(j));
                if(list.size()>0) {
                    out.print("vinili tipo tag: "+tags.get(j).getNome());
                    for (int i = 0; i < list.size(); i++) {
                        prodotto temp = carrello.getItem(list.get(i));
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
                out.print("<br>");
            }
        %>

    </ul>
</div>
<br/>
</body>
</html>