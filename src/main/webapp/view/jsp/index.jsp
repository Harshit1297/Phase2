<%@page import="javax.swing.text.html.ListView"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import = "java.util.*" %>
<html>
<body>
	<h2>Spring MVC</h2>
	
	<c:if test="${not empty lists}">
	
		<ul>
			<c:forEach var="listValue" items="${lists}">
			<c:url value = "cs/" var="url">
				
				<c:param name="path" value="${listValue}"></c:param>
				
			</c:url>
			
			<li><a href="${url}" onclick="return myFunction();">${listValue}</li>
			</c:forEach>
		</ul>
	</c:if>	
	<script type="text/javascript">
	function myFunction() {
	    document.getElementById(".myDiv").style.flexGrow = "5"; 
	}
	</script>
</body>
</html>