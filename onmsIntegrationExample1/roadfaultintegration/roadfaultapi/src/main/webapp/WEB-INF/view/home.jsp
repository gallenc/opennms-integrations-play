<%-- 
    Document   : content
    Created on : Jan 4, 2020, 11:19:47 AM
    Author     : cgallen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
// request set in controller
//request.setAttribute("selectedPage", "home");
%>
<jsp:include page="header.jsp" />
<!-- Begin page content -->
<main role="main" class="container">
    <noscript><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websocket relies on Javascript being
        enabled. Please enable Javascript and reload this page!</h2></noscript>
    <H1>Home</H1>
    <p id="messageholder">${message}<p>
    <div class="row">
        <div  class="col-md-2">
            <form class="form-inline" id="form1" >
                <div class="form-group-sm">
                    <label for="connect">Live Message Display</label>
                    <button id="connect" class="btn btn-default" type="submit">Start</button>
                    <button id="disconnect" class="btn btn-default" type="submit" disabled="disabled">Stop
                    </button>
                </div>
            </form>
        </div>
        <div class="col-md-3">
            <form class="form-inline" action="./home" method="post">
                <div class="form-group-sm">
                    <label for="name">Username</label>
                    <input type="text" name="username" value="${username}" > <!-- placeholder="undefined "-->
                    <label for="password">Password</label>
                    <input type="text" name="password" value="${password}" >
                    <input type="hidden" name="action" value="changeCredentials">
                    <button class="btn btn-default" type="submit">Change</button>
                </div>
            </form>
        </div>
        <div class="col-md-3">
            <form class="form-inline" action="./home" method="post">
                <div class="form-group-sm">
                    <label for="error_message">error_message</label>
                    <input type="text" name="error_message" value="${error_message}" > <!-- placeholder="undefined "-->
                    <label for="error_type">error_type</label>
                    <input type="text" name="error_type" value="${error_type}" >
                    <input type="hidden" name="action" value="replyWithErrorMessage">
                    <button  class="btn btn-default" type="submit">Set Error Message</button>
                </div>
            </form>
            <form class="form-inline" action="./home" method="post">
                <div class="form-group-sm">
                    <input type="hidden" name="action" value="clearErrorMessage">
                    <button  class="btn btn-default" type="submit">Clear Error Message</button>
                </div>
            </form>
        </div>
        <div class="col-md-4">
            <button id="sendtestdata" class="btn btn-default" >Send Test Message</button>
            <textarea style="width: 100%; height: 100px; font-size: 10px;" id="send"></textarea>
            <textarea style="width: 100%; height: 100px; font-size: 10px;" id="receive"></textarea>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12" style="max-height: 1000px; width: 100%; margin: 0; overflow-y: auto;">
            <p>Messages Received</p>
            <table id="conversation" class="table table-striped" 
                   >
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Status</th>
                        <th>Timestamp</th>
                        <th>Message Content</th>
                        <th>Reply</th>
                    </tr>
                </thead>
                <tbody id="greetings">
                </tbody>
            </table>
        </div>
    </div>
</main>
<jsp:include page="footer.jsp" />
