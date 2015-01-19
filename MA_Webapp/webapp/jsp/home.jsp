<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/common.css'></c:url>"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/home.css'></c:url>"></link>
	<script type="text/javascript" src="<c:url value='/resources/js/jquery-1.9.0.min.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/preference.js'></c:url>"></script>
	<script type="text/javascript" src="<c:url value='/resources/js/home.js'></c:url>"></script>
</head>
<body>
	<div class="container">
		<div class="sidebar"></div>
		<div class="container_header">Simplest Nash equilibrium !</div>
		<div class="main_content">
			<div id="div_input">
			Input number of agents:<input type="text" id="txt_agent_number"> <input type="button" id="btn_agent_number" value="OK">
			</div>
			<div id="div_util">
				<p> Input actions of each agent and its utility value in each preference</p>
				<table id="tbl_util">
				</table>
			</div>
			<div id="div_submit">
				<input type="button" value="Submit" id="btn_submit">
			</div>
		</div>
	</div>
</body>
</html>
