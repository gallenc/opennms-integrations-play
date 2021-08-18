<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
// request set in controller
//    request.setAttribute("selectedPage","page1");
%>
<jsp:include page="header.jsp" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- Begin page content -->
<!--<META HTTP-EQUIV="Refresh" CONTENT="20"> doesnt work with pagination -->
<main role="main" class="container">
    <H1>Log (Total Entries ${totalElements})</H1>
    <div class="col-md-auto">
        <form class="form-inline" action="./log" method="post">
            <div class="form-group-sm">
                <input type="hidden" name="action" value="resetLog">
                <button  class="btn btn-sm" type="submit">Reset Log</button>
            </div>
        </form>
        <nav aria-label="Result Page Navigation">

            <ul class="pagination pagination-sm">
                <c:forEach begin="0" end="${totalPages}" varStatus="loop">
                    <c:if test = "${loop.index == pageNumber}"><li class="page-item active"></c:if>
                    <c:if test = "${loop.index != pageNumber}"><li class="page-item"></c:if>
                        <a class="page-link" href="./log?currentPage=${loop.index}&itemsPerPage=${itemsPerPage}">Page ${loop.count}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
    <table class="table">
        <tr>
            <td>id</th>
            <th>httpStatus</th>
            <th>date</th>
            <th>messageContent</th>
            <th>reply</th>
            <th>errorMessage</th>
            <th>authError</th>
            <th>exceptionMessage</th>
        </tr>

        <c:forEach var="logItem" items="${requestLog}">
            <tr>
                <td>${logItem.id}</td>
                <td>${logItem.httpStatus}</td>
                <td>${logItem.logTimestamp}</td>
                <td>${logItem.messageContent}</td>
                <td>REPLY</td>
                <td>${logItem.errorMessage}</td>
                <td>${logItem.authError}</td>
                <td>${logItem.exceptionMessage}</td>
            </tr>
        </c:forEach>
    </table>
</div>

</main>




<jsp:include page="footer.jsp" />
