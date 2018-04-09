<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>权限管理</title>
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
                <li><a href="${contextPath}/manager/users">用户管理</a></li>
                <li class="active"><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="panel panel-default">
                    <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                    <%--@elvariable id="auth" type="com.ahao.core.entity.IDataSet"--%>
                    <div class="panel-heading">${isExist?'编辑权限':'增加权限'}</div>
                    <div class="panel-body">
                        <form class="form-horizontal" id="form-auth">
                            <c:if test="${isExist}">
                                <div class="form-group">
                                    <label for="input-id" class="col-md-2 control-label">权限id</label>
                                    <div class="col-md-10">
                                        <input class="form-control" placeholder="权限id" disabled
                                               id="input-id" name="auth-id"
                                               value="${auth.getInt('id')}"/>
                                    </div>
                                </div>
                            </c:if>
                            <div class="form-group">
                                <label for="input-name" class="col-md-2 control-label">权限名称</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="权限名称(只支持英文及点, 如 auth.root)"
                                           id="input-name" name="auth-name"
                                           value="${auth.getString('name')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="input-description" class="col-md-2 control-label">权限描述</label>
                                <div class="col-md-10">
                                    <input class="form-control" placeholder="权限描述"
                                           id="input-description" name="auth-description"
                                           value="${auth.getString('description')}">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-2 control-label">权限状态</label>
                                <c:set var="enabled" value="${auth.getInt('enabled') == 0}"/>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio" ${enabled?'':'checked'}
                                               id="input-enabled-1" name="auth-enabled"
                                               value="1" />启用
                                    </label>
                                </div>
                                <div class="col-md-3 radio">
                                    <label>
                                        <input type="radio"  ${enabled?'checked':''}
                                               id="input-status-0" name="auth-enabled"
                                               value="0"/>禁用
                                    </label>
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
            $('#form-auth').submit(function (e) {
                e.preventDefault();

                var authId = $('input[name="auth-id"]').val();
                var name = $('input[name="auth-name"]').val();
                var description = $('input[name="auth-description"]').val();
                var enabled = $('input[name="auth-enabled"]:checked').val();

                if(!/^[a-zA-Z.]+$/.test(name)){
                    swal({type: 'warning', title: '警告', text: '权限名格式填写错误!'});
                    return;
                }

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/auth/save',
                    data: {
                        authId: authId,
                        name: name,
                        description: description,
                        enabled: enabled
                    },
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            window.location.href = ctx + '/manager/auths';
                        })
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>