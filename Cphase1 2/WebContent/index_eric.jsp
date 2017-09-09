<html>
<%@page contentType="text/html" pageEncoding="UTF-8" errorPage="error.jsp"%>
<%@page import="IRUtilities.*" %>
<%@page import="java.util.*" %>
<head>
<meta charset="UTF-8">
<title>Powerful Search Engine</title>


<link rel='stylesheet prefetch'
	href='http://fonts.googleapis.com/css?family=Open+Sans:600'>

<link rel="stylesheet" href="css/style.css">


</head>
<body>
	<div class="login-wrap">
		<div class="login-html">
			<input id="tab-1" type="radio" name="tab" class="sign-in" checked><label
				for="tab-1" class="tab">Powerful Search Engine</label> <input
				id="tab-2" type="radio" name="tab" class="sign-up"><label
				for="tab-2" class="tab">I am feeling lucky</label>

			<div class="login-form">
				<form method="post" action="winson.jsp">
					<div class="sign-in-htm">
						<div class="group">
							<label for="user" class="label">How can I help you?</label> <input
								id="user" type="text" name="txtname" class="input" size="100"/>
								
						</div>
						<div>
							<h5></h5>
						</div>
					<div class="group">
						<input type="submit" class="button" value="Go">
					</div>
				</form>
				
					<div class="foot-lnk">
						You can search what you want here.
						<div class="hr"></div>
						<div class="foot-lnk">
							<label for="tab-1"><a>Deigned by our COMP4321 wonderful team.</a> </label>
						</div>
					</div>
			</div>
			
				<div class="sign-up-htm">
					<div class="group">
						<label for="user" class="label">Lucky Keyword:</label> 
							<form method="post" action="wasin.jsp">
					<%
					   ArrayList<String> keywords = new ArrayList<String>();
					   Filtering filter = new Filtering();
					   keywords = filter.getkeywords();
					%>
					<div class="group">
						<select name="txtname"  class="input">
						<% for(int i = 0; i < keywords.size(); i++) {
					           String option = (String)keywords.get(i);
					  	%>
					  	<option style="color: gray" value="" disabled selected
								hidden="true">Please choose a lucky keyword...</option>
					  	<option style="color: gray" value="<%= option %>"><%= option %></option>
					   	<% } %>
						</select>
					</div>
					<div class="group">
						<input type="submit" class="button" value="Lucky Search"/>	
					</div>
					</div>

					<div>
						<h5></h5>
					</div>

					</form>
					<!--  <form action="/action_page.php">-->

				
					<div class="foot-lnk">
						You can search what you want here.
						<div class="hr"></div>
						<div class="foot-lnk">
							<label for="tab-1"><a>Designed by our COMP4321
									wonderful team.</a> </label>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>