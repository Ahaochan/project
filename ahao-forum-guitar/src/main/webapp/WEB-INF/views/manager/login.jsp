<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>标题</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <form class="form-signin" action="${contextPath}/login" method="post">
        <h2 class="form-signin-heading">请登录</h2>
        <div class="alert alert-danger" style="display: ${message == null ? "none" : "block"}">${message}</div>
        <div class="input-group form-group">
            <label class="input-group-addon" for="form-username">
                <i class="glyphicon glyphicon-user"></i>
            </label>
            <%--@elvariable id="username" type="java.lang.String"--%>
            <input id="form-username" name="username" class="form-control" value="${username_tmp}"/>
        </div>
        <div class="input-group form-group">
            <label class="input-group-addon" for="form-password">
                <i class="glyphicon glyphicon-lock"></i>
            </label>
            <%--@elvariable id="password" type="java.lang.String"--%>
            <input id="form-password" name="password" class="form-control" type="password" value="${password_tmp}" />
        </div>
        <button class="btn btn-lg btn-primary btn-block">登录</button>
    </form>
</div>

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script src="${contextPath}/js/core.js"></script>
</html>
