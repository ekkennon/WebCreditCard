<%-- 
    Document   : History
    Created on : Mar 10, 2017, 8:02:35 AM
    Author     : ekk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Account Log</title>
    </head>
    <body>
        <h1>Credit Card Account Log For: ${card.accountId}</h1>
        <br>
        <table>
            <c:forEach var="s" items="${card.creditHistory}">
                <tr>
                    <td>
                        ${s}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
