<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-default navbar-static-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">电吉他设备选购及知识普及软件平台</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <c:if test="${username != null}">
                    <li><a href="${contextPath}/profile">${username}</a></li>
                </c:if>
                <c:if test="${username == null}">
                    <li><a href="${contextPath}/login">登录</a></li>
                    <li><a href="../navbar/">注册</a></li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>
