<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>View Alarms</title>
<style>
table, th, td {
  border: 1px solid;
}
</style>
</head>
<body>
<p>number of alarms = ${alarmsSize}</p>
	<table>
		<thead>
			<tr>
				<th>alarm</th>

			</tr>
		</thead>
		<tbody>
			<c:forEach var="entry" items="${alarms}">
				<tr>
					<td>${entry.key}</td>
					<td>$${entry.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>

