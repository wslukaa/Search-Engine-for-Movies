<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Powerful Search Engine</title>

<link rel="stylesheet" type="text/css" href="ustyle.css" />
</head>
<body>
<div id="container">

<div id="header">
<h1>Powerful Search Engine</h1>
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

if(request.getParameter("txtname")!=null&& !request.getParameter("txtname"). isEmpty())
{
	out.println("The results you are looking for ... <hr/>");
	String string1 = request.getParameter("txtname");

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
			%>
			<form method="post" action="Katrina.jsp">

			<input type="submit" class="button" value="Similar Search"/>
			</form>

			<%
				out.println("Top 5 frequent words: ");
				out.println("<div></div>");
					String[] str = new String[5];	
					Vector<Vocab> v1 = temp.getKeywords();
					for(int j = 0; j < v1.size(); j++){
						out.print(v1.elementAt(j).getText()+" "+v1.elementAt(j).getFreq()+"; ");
						str[j] = v1.elementAt(j).getText();
						if(j==4){
							out.println("<br/>");
							break;
						}
					}
				
				out.println("<div><br></br></div>");
				out.println("Parent Links: ");
				out.println("<div></div>");
				session.setAttribute("vocab", str);
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
			out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
			out.println("<div></div>");
		}
		
		//out.println("</table>");
	}else{
		out.println("No match result");
		for(int i=0;i<30;i++){
			out.println("<div><br></br></div>");
		}
	}
}
else
{
	out.println("No input value.");
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