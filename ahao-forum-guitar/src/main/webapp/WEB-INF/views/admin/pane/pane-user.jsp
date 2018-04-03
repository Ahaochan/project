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
                <li><a href="javascript:void(0)">个人资料</a></li>
                <li><a href="${contextPath}/manager/password">修改密码</a></li>
                <li><a href="${contextPath}/manager/categories">分区管理</a></li>
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li class="active"><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="row">
                <div class="tab-content tab-pane">
                    <div class="panel panel-default">
                        <div class="panel-heading">用户管理</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-md-3">
                                    <div class="btn-group">
                                        <a class="btn btn-default">
                                            <span class="glyphicon glyphicon-plus"></span>新增
                                        </a>
                                        <a id="btn_delete_list" class="btn btn-default">
                                            <span class="glyphicon glyphicon-remove"></span>删除
                                        </a>
                                    </div>
                                </div>
                                <div class="col-md-3 col-md-offset-6">
                                    <div class="input-group">
                                        <input type="text" class="form-control" placeholder="Search for..."/>
                                        <span class="input-group-btn">
                                            <button class="btn btn-default" type="button">Go!</button>
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <table class="table table-bordered table-striped">
                            <thead>
                            <tr>
                                <th></th>
                                <th>用户id</th>
                                <th>用户名</th>
                                <th>状态</th>
                                <th>最后一次登录时间</th>
                                <th>最后一次登录Ip</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><input type="checkbox" /></td>
                                <td>列1</td>
                                <td>列2</td>
                                <td>列3</td>
                                <td>列4</td>
                                <td>列5</td>
                                <td>
                                    <a type="button" class="btn btn-primary btn-circle btn-sm" href="">
                                        <i class="glyphicon glyphicon-pencil"></i>
                                    </a> &nbsp;
                                    <a class="btn btn-warning btn-circle btn-sm btn-delete" data-id="">
                                        <i class="glyphicon glyphicon-remove"></i>
                                    </a>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="text-right">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <li>
                                <a href="#" aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                            <li><a href="#">1</a></li>
                            <li><a href="#">2</a></li>
                            <li><a href="#">3</a></li>
                            <li><a href="#">4</a></li>
                            <li><a href="#">5</a></li>
                            <li>
                                <a href="#" aria-label="Next">
                                    <span aria-hidden="true">&raquo;</span>
                                </a>
                            </li>
                        </ul>
                    </nav>
                </div>
            </div>
        </div>
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
</html>
