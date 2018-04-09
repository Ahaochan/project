<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>用户管理</title>
</head>
<body>
<%-- 导航条 --%>
<%@include file="/WEB-INF/views/static/nav.jsp" %>

<%-- 内容 --%>
<div class="container">
    <div class="row">
        <div class="col-md-2">
            <ul class="nav nav-pills nav-stacked">
                <li><a href="${contextPath}/manager/profile">个人资料</a></li>
                <li><a href="${contextPath}/manager/password">修改密码</a></li>
                <li><a href="${contextPath}/manager/categories">分区管理</a></li>
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li class="active"><a href="javascript:void(0)">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <div class="panel-heading">用户管理</div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-3">
                                <div class="btn-group">
                                    <a class="btn btn-primary" href="${contextPath}/manager/user">
                                        <span class="glyphicon glyphicon-plus"></span>新增
                                    </a>
                                    <a id="btn_delete_list" class="btn btn-warning">
                                        <span class="glyphicon glyphicon-remove"></span>删除
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-3 col-md-offset-6">
                                <form id="form-search">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="搜索用户名"
                                               name="user-name"/>
                                        <span class="input-group-btn">
                                                    <button class="btn btn-default" type="submit">搜索</button>
                                                </span>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th class="col-md-1"></th>
                            <th class="col-md-1">用户id</th>
                            <th class="col-md-1">用户名</th>
                            <th class="col-md-2">最后一次登录时间</th>
                            <th class="col-md-2">最后一次登录Ip</th>
                            <th class="col-md-1">状态</th>
                            <th class="col-md-1">主题数</th>
                            <th class="col-md-1">回复数</th>
                            <th class="col-md-2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <%-- Ajax加载数据 --%>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="text-right" id="pagination">
                <%-- Ajax加载数据 获取分页器 --%>
            </div>
        </div>
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script src="${contextPath}/js/core.js"></script>
<script src="${contextPath}/js/manager/user/manager-user-list.js"></script>
</html>