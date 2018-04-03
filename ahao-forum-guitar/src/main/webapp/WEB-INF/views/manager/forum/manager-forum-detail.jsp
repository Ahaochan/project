<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>分区管理</title>
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
                <li class="active"><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li><a href="${contextPath}/manager/users">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="row">
                    <div class="tab-content tab-pane">
                        <div class="panel panel-default">
                            <%--@elvariable id="isExist" type="java.lang.Boolean"--%>
                            <%--@elvariable id="category" type="com.ahao.core.entity.IDataSet"--%>
                            <div class="panel-heading">${isExist?'编辑分区':'增加分区'}</div>
                            <div class="panel-body">
                                <form class="form-horizontal" id="forum-category">
                                    <c:if test="${isExist}">
                                        <div class="form-group">
                                            <label for="input-id" class="col-md-2 control-label">分区id</label>
                                            <div class="col-md-10">
                                                <input class="form-control" id="input-id" placeholder="分区id" name="category-id"
                                                       readonly disabled value="${category.getInt('id')}"/>
                                            </div>
                                        </div>
                                    </c:if>
                                    <div class="form-group">
                                        <label for="input-name" class="col-md-2 control-label">分区名称</label>
                                        <div class="col-md-10">
                                            <input class="form-control" id="input-name" placeholder="分区名称" name="category-name"
                                                   value="${category.getString('name')}">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="input-description" class="col-md-2 control-label">分区描述</label>
                                        <div class="col-md-10">
                                            <input class="form-control" id="input-description" placeholder="分区描述" name="category-description"
                                                   value="${category.getString('description')}">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">分区状态</label>
                                        <c:set var="status" value="${category.getInt('status') == 0}"/>
                                        <div class="col-md-3 radio">
                                            <label>
                                                <input type="radio" id="input-status-1" name="category-status"
                                                       value="1" ${status?'':'checked'}>启用
                                            </label>
                                        </div>
                                        <div class="col-md-3 radio">
                                            <label>
                                                <input type="radio" id="input-status-0" name="category-status"
                                                       value="0" ${status?'checked':''}>禁用
                                            </label>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">下属板块</label>
                                        <div class="col-md-10">
                                            <div class="panel panel-default">
                                                <div class="panel-heading">
                                                    <h4 class="panel-title">
                                                        下属板块
                                                        <span class="glyphicon glyphicon-triangle-bottom"
                                                              data-toggle="collapse" href="#pane-sub-forum"></span>
                                                    </h4>
                                                </div>
                                                <div id="pane-sub-forum" class="panel-collapse collapse in">
                                                    <div class="panel-body">
                                                        <c:forEach items="${forums}" var="item">
                                                            <div class="checkbox ${item.getBoolean("status") ? '' : 'disabled'}">
                                                                <label>
                                                                    <input type="checkbox" name="forums"
                                                                           value="${item.getInt("id")}"
                                                                        ${item.getBoolean("status") ? '' : 'disabled'}
                                                                        ${"".equals(item.getString("selected")) ? "":"checked"}/>
                                                                        ${item.getString("name")}</label>
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
    </div>
</div>
<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script>
    $(function () {
        (function ($) {
            $('#forum-category').submit(function (e) {
                e.preventDefault();

                var categoryId = $('input[name="category-id"]').val();
                var name = $('input[name="category-name"]').val();
                var description = $('input[name="category-description"]').val();
                var status = $('input[name="category-status"]:checked').val();
                var forumIds = $('input[name="forums"]:checked').map(function () {
                    return this.value;
                }).get();

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/category/save',
                    data: {
                        categoryId: categoryId,
                        name: name,
                        description: description,
                        status: status,
                        forumIds: forumIds
                    },
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: '保存失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '保存成功'});

                        setTimeout(function () {
                            window.location.href = ctx + '/manager/categories';
                        })
                    }
                });
            });
        })(jQuery);
    });
</script>
</html>