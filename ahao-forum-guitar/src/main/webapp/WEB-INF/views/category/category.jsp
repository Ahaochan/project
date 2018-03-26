<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>电吉他设备选购及知识普及软件平台-${category.getString("name")}</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <div class="page-header">
                    <h1>${category.getString("name")}<span class="badge">${category.getInt("post_count")}</span></h1>
                </div>
                Basic panel example
            </div>
        </div>
    </div>
    <div class="row">
        <a class="btn btn-primary">发帖</a>
    </div>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th class="col-md-8">帖子标题</th>
                        <th class="col-md-1">作者</th>
                        <th class="col-md-1">回复</th>
                        <th class="col-md-2">最后回复时间</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${posts.get('list')}" var="item">
                        <tr>
                            <td>${item.getString("title")}</td>
                            <td>${item.getString("username")}</td>
                            <td>回复</td>
                            <td>最后回复时间</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="text-right">
            ${posts.getString("pageIndicator")}
        </div>
    </div>

</div>

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
</html>
