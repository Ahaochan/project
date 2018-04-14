<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <%--@elvariable id="forum" type="com.ahao.core.entity.IDataSet"--%>
    <meta name="forumId" content="${forum.getInt("id")}"/>
    <title>电吉他设备选购及知识普及软件平台-${forum.getString("name")}</title>
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
                    <h1>${forum.getString("name")}<span class="badge">${forum.getInt("post_count")}</span></h1>
                </div>
                ${forum.getString("description")}
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-1">
            <a class="btn btn-primary" href="${contextPath}/forum-${forum.getInt("id")}/new-thread">发帖</a>
        </div>
        <div class="col-md-4 col-md-offset-7">
            <form id="form-search">
                <div class="input-group">
                    <input type="text" class="form-control" placeholder="搜索主题名"
                           name="thread-name"/>
                    <span class="input-group-btn">
                        <button class="btn btn-default" type="submit">搜索</button>
                    </span>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th class="col-md-8">帖子标题</th>
                        <th class="col-md-1">作者</th>
                        <th class="col-md-1">回复数</th>
                        <th class="col-md-2">最后回复时间</th>
                    </tr>
                    </thead>
                    <tbody id="tbody-thread">
                    <%-- AJAX 生成数据 --%>
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

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<script src="${contextPath}/js/core.js"></script>
<script>
    var jump = function (page) {
        var search = $('input[name="thread-name"]').val();
        getListFun({page: page, search: search});
    };
    var getListFun = function (option) {
        var options = $.extend({
            page: 1,
            search: ''
        }, option);

        var page = options.page, search = options.search;
        var forumId = $('meta[name="forumId"]').attr('content');
        $.ajax({
            type: 'get',
            url: ctx + '/api/forum-' + forumId + '/thread/list-' + page,
            data: {search: search},
            success: function (json) {
                var $tbody = $('#tbody-thread');
                $tbody.empty();

                if (!json.result) {
                    $tbody.append('<tr><td colspan="4" class="text-center">暂无数据</tr>');
                    return;
                }

                var list = json.obj.list;
                for (var i = 0, len = list.length; i < len; i++) {
                    var item = list[i];
                    $tbody.append('<tr>' +
                        '   <td><a href="' + ctx + '/thread-' + item.id + '">' + item.title + '</a></td>' +
                        '   <td>' + item.username + '</td>' +
                        '   <td>' + (item.reply_num || '0') + '</td>' +
                        '   <td>' + ($.format.date(item.last_reply_time || '无', 'yyyy-MM-dd hh:mm:ss')) + '</td>' +
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
                event.preventDefault();

                var search = $('input[name="thread-name"]').val();
                getListFun({search: search});
            });
            $('input[name="thread-name"]').on('keyup', function () {
                var $this = $(this);
                clearTimeout(parseInt($this.data('timer')));
                var search = $this.val();
                $this.data('timer', setTimeout(function () {
                    getListFun({search: search});
                }, 500));
            });
        })(jQuery);
    });
</script>
</html>
