<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Powerful Search Engine - Similar Page</title>

<link rel="stylesheet" type="text/css" href="ustyle.css" />
<div id="container">

<div id="header">

<h1>Powerful Search Engine--Similar Page</h1>
<h2>by Our COMP4321 Wonderful Team.</h2>


</div>

<div id="linkbar">
<div id="navcontainer">
<ul id="navlist">

</ul>
</div>
</div>

<div id="content">
<div id="right_menu">
<h4>Menu</h4>

<div class="navcontainer">
<ul id="navcontainer">
<li><a href="http://localhost:8080/Cphase1/index_eric.jsp">Powerful Search Engine</a></li>

<!-- <li><a href="http://localhost:8080/Cphase1/Similar_page.jsp">Similar Page</a></li>
 <li><a href="#">Contact</a></li>
<li><a href="#">Links</a></li>-->
</ul>
</div>
</div>

<div><br></br></div>
<div class="content" style="white-space:initial;">
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>

<%@page import="IRUtilities.*" %>
<%@page import="java.util.*" %>
<%



	String[] voca = (String[])session.getAttribute("vocab");
	
	String string1 = voca[0]+" "+voca[1]+" "+voca[2]+" "+voca[3]+" "+voca[4];

    String[] str1 = string1.split(" ");
	List<String> list = Arrays.asList(str1);
    Vector<String> vector = new Vector<String>(list);

    SearchEngine se = new SearchEngine();
	Vector<Link> result = se.search(vector);
	if(result.size() > 0){
		//out.println("<table>");
		

		for(int i = 0; i < result.size(); i++){
			Link temp = result.elementAt(i);
			out.println("Title Similarity Score: "+"<tr><td valign=\"top\">"+temp.getScore()+"</td>");
			out.println("<td>");
			out.println("<div><br></br></div>");
			out.println("Title: "+"<a href=\""+temp.getUrl()+"\"> "+temp.getTitle()+"</a><br/>");
			out.println("URL: "+"<a href=\""+temp.getUrl()+"\"> "+temp.getUrl()+"</a><br/>");
			out.println("Last Update Date: "+temp.getLastUpdate()+", Page Size: "+temp.getPageSize()+"<br/>");
			
				
			
			out.println("<div><br></br></div>");
			out.println("Parent Links: ");
			out.println("<div></div>");

		Vector<String> p = temp.getParentLink();
		for(int j = 0; j< p.size(); j++){
			out.println(p.elementAt(j)+"<br/>");
		}
		out.println("<div><br></br></div>");
		out.println("Child Links: ");
		out.println("<div></div>");
		Vector<String> c = temp.getChildLink();
		for(int j = 0; j< c.size(); j++){
            out.println(c.elementAt(j)+"<br/>");
        }
		out.println("<br/></td></tr>");
		if(i >= 30)break;
		out.println("---------------------------------------------------------------------------------------------------------------------------------------------");
		out.println("<div></div>");
		}
		//out.println("</table>");
	}else{
		out.println("No match result");
		for(int i=0;i<30;i++){
			out.println("<div><br></br></div>");
		}
	}


%>
</div>
</div>
</div>
</body>

</html>