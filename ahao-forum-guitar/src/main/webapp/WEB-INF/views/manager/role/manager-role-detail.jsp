<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>角色管理</title>
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
                <li><a href="${contextPath}/manager/categories">角色管理</a></li>
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li class="active"><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                    <%--@elvariable id="role" type="com.ahao.core.entity.IDataSet"--%>
                    <div class="panel-heading">${isExist?'编辑角色':'增加角色'}</div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="form-role">
                            <c:if test="${isExist}">
                                <div class="form-group">
                                    <label for="input-id" class="col-md-2 control-label">角色id</label>
                                    <div class="col-md-10">
                                        <input class="form-control" placeholder="角色id" readonly
                                               id="input-id" name="role-id"
                                               value="${role.getInt('id')}"/>
                                    </div>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <label for="input-name" class="col-md-2 control-label">角色名称</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="角色名称(只支持英文及点, 如 role.root)"
                                           id="input-name" name="role-name"
                                           value="${role.getString('name')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-description" class="col-md-2 control-label">角色描述</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="角色描述"
                                           id="input-description" name="role-description"
                                           value="${role.getString('description')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">角色状态</label>
                                <c:set var="enabled" value="${role.getInt('enabled') == 0}"/>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio" ${enabled?'':'checked'}
                                               id="input-enabled-1" name="role-enabled"
                                               value="1" />启用
                                    </label>
                                </div>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio" ${enabled?'checked':''}
                                               id="input-enabled-0" name="role-enabled"
                                               value="0" />禁用
                                    </label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-weight" class="col-md-2 control-label">角色权值</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="角色权值" type="number"
                                           id="input-weight" name="role-weight"
                                           value="${role.getInt('weight')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">拥有权限</label>
                                <div class="col-md-10">
                                    <div class="panel panel-default">
                                        <div class="panel-heading">
                                            <h4 class="panel-title">
                                                拥有权限
                                                <span class="glyphicon glyphicon-triangle-bottom"
                                                      data-toggle="collapse" href="#pane-sub-forum"></span>
                                            </h4>
                                        </div>
                                        <div id="pane-sub-forum" class="panel-collapse collapse in">
                                            <div class="panel-body">
                                                <c:forEach items="${auths}" var="item">
                                                    <div class="checkbox ${item.getBoolean("enabled") ? '' : 'disabled'}">
                                                        <label>
                                                            <input type="checkbox"
                                                                   name="auths"
                                                                   value="${item.getInt("id")}"
                                                                ${item.getBoolean("enabled") ? '' : 'disabled'}
                                                                ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                ${item.getString("description")}</label>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-2 col-md-10">
                                    <button type="submit" class="btn btn-default">保存</button>
                                </div>
                            </div>
                        </form>
                    </div>
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
<script src="${contextPath}/js/core.js"></script>
<script>
    $(function () {
        (function ($) {
            $('#form-role').submit(function (e) {
                e.preventDefault();

                var roleId = $('input[name="role-id"]').val();
                var name = $('input[name="role-name"]').val();
                var description = $('input[name="role-description"]').val();
                var enabled = $('input[name="role-enabled"]:checked').val();
                var weight = $('input[name="role-weight"]').val();
                var authIds = $('input[name="auths"]:checked').map(function () { return this.value; }).get();

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/role/save',
                    data: {
                        roleId: roleId,
                        name: name,
                        description: description,
                        enabled: enabled,
                        weight: weight,
                        authIds: authIds
                    },
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            window.location.href = ctx + '/manager/roles';
                        })
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>