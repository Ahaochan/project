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
                <li><a href="${contextPath}/manager/forums">板块管理</a></li>
                <li class="active"><a href="javascript:void(0)">用户管理</a></li>
                <li><a href="${contextPath}/manager/auths">权限管理</a></li>
                <li><a href="${contextPath}/manager/roles">角色管理</a></li>
            </ul>
        </div>
        <div class="col-md-10">
            <div class="tab-content tab-pane">
                <div class="row">
                    <div class="tab-content tab-pane">
                        <div class="panel panel-default">
                            <div class="panel-heading">分区管理</div>
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
                                                <input type="text" class="form-control" placeholder="搜索分区名"
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
                </div>
                <div class="row">
                    <div class="text-right" id="pagination">
                        <%-- Ajax加载数据 获取分页器 --%>
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
    var jump = function (page) {
        var search = $('input[name="user-name"]').val();
        getListFun({page: page, search: search});
    };
    var getListFun = function (option) {
        var options = $.extend({
            page: 1,
            search: ''
        }, option);

        var page = options.page, search = options.search;

        $.ajax({
            type: 'get',
            url: ctx + '/manager/api/users/list-' + page,
            data: {search: search},
            success: function (json) {
                var $tbody = $('tbody');
                $tbody.empty();

                if (!json.result) {
                    $tbody.append('<tr><td colspan="5" class="text-center">暂无数据</tr>');
                    return;
                }


                var list = json.obj.list;
                for (var i = 0, len = list.length; i < len; i++) {
                    var item = list[i];
                    $tbody.append('<tr ' + (item.enabled ? '' : 'class="danger"') + '>' +
                        '   <td><input type="checkbox" name="user-id" value="' + item.id + '"/></td>' +
                        '   <td>' + item.id + '</td>' +
                        '   <td>' + item.username + '</td>' +
                        '   <td>' + ($.format.date(item.last_login_time || '无', 'yyyy-MM-dd')) + '</td>' +
                        '   <td>' + (item.last_login_ip || '无') + '</td>' +
                        '   <td>' + (!!item.enabled ? '正常' : '禁用') + '</td>' +
                        '   <td>' + (item.thread_num || '0') + '</td>' +
                        '   <td>' + (item.post_num || '0') + '</td>' +
                        '   <td>' +
                        '       <a type="button" class="btn btn-primary btn-circle btn-sm" href="' + ctx + '/manager/user-' + item.id + '">' +
                        '           <i class="glyphicon glyphicon-pencil"></i>' +
                        '       </a> &nbsp;' +
                        '       <a class="btn btn-warning btn-circle btn-sm btn-delete" ahao-user-id="' + item.id + '">' +
                        '           <i class="glyphicon glyphicon-remove"></i>' +
                        '       </a>' +
                        '   </td>' +
                        '</tr>');
                }
                $('#pagination').empty().append(json.obj.pageIndicator);
            }
        });
    };

    $(function () {
        // 1. 初始化表格, 获取第1页数据
        (function ($) {
            getListFun();
        })(jQuery);

        // 2. 搜索功能
        (function ($) {
            $('#form-search').submit(function (event) {
                var search = $('input[name="user-name"]').val();
                getListFun({search: search});
                event.preventDefault();
            });
            $('input[name="user-name"]').on('keyup', function () {
                var $this = $(this);
                clearTimeout(parseInt($this.data('timer')));
                var search = $this.val();
                $this.data('timer', setTimeout(function () {
                    getListFun({search: search});
                }, 500));
            });
        })(jQuery);

        // 3. 删除功能
        (function ($) {
            var deleteFun = function (userIds) {
                var ids = [].concat(userIds);
                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/users/delete',
                    data: {userIds: ids},
                    dataType: 'json',
                    success: function (json) {
                        if (!json.result) {
                            swal({type: 'warning', title: '警告', text: json.msg});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: json.msg});
                        $('#form-search').submit();
                    }
                });
            };

            // 3.1 批量删除
            $('#btn_delete_list').on('click', function () {
                var userIds = $('input[name="user-id"]:checked').map(function () {
                    return this.value;
                }).get();
                deleteFun(userIds);
            });

            // 3.2. 单个删除
            $('tbody').on('click', 'a.btn-delete', function () {
                console.log("测试2");
                var $this = $(this);
                var userId = $this.attr('ahao-user-id');
                if (!!userId) {
                    deleteFun(userId);
                }
            });
        })(jQuery);
    });
</script>
</html>