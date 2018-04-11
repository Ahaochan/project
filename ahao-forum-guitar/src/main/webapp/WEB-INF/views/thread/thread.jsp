<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <%--@elvariable id="thread" type="com.ahao.core.entity.IDataSet"--%>
    <meta name="threadId" content="${thread.getInt("thread_id")}"/>
    <title>${thread.getString("thread_title")}-电吉他设备选购及知识普及软件平台</title>
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
                    <h1>${thread.getString("forum_name")}</h1>
                </div>
                <a href="${contextPath}">首页</a> >
                <a href="${contextPath}/forum-${thread.getInt("forum_id")}">${thread.getString("forum_name")}</a> >
                <span>${thread.getString("thread_title")}</span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="list-group" id="list-post">
            <div class="list-group-item" id="item-thread">
                <div class="row">
                    <div class="col-md-2 text-center">
                        <div class="row"><p>用户1</p></div>
                        <div class="row"><img style="width: 80%"
                                              src="${contextPath}/img/93b09a57-93a8-43f3-bf2b-6cc62530dd81%E7%89%88%E5%9B%BE1.jpg"/>
                        </div>
                        <div class="row"><p>主题数量: 100<br/>回帖数量: 100</p></div>
                    </div>
                    <div class="col-md-10">
                        <div class="row">
                            <div class="col-md-10">
                                <small>发布时间:2018年04月11日 11:22:33</small>
                            </div>

                            <div class="col-md-2 text-right">主题</div>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="col-md-5">
                                <small>修改时间:2018年04月11日 11:22:33</small>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">
                                Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                Maecenas sed diam eget risus varius blandit sit amet non magna. Lorem ipsum dolor sit
                                amet,
                                consectetur adipiscing elit. Praesent commodo cursus magna, vel scelerisque nisl
                                consectetur et.
                                Cras mattis consectetur purus sit amet fermentum. Duis mollis, est non commodo luctus,
                                nisi erat porttitor ligula, eget lacinia odio sem nec elit. Aenean lacinia bibendum
                                nulla sed consectetur.
                            </div>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="col-md-12">
                                <a class="btn btn-default" href="#" role="button">回复</a>
                                <a class="btn btn-default" href="#" role="button">删帖</a>
                            </div>
                        </div>
                    </div>
                </div>
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
    var generatePost = function (option) {
        var options = $.extend({
            selector: '',
            username: '用户名',
            avatarUrl: '头像',
            threadNum: 0,
            postNum: 0,
            createTime: 'yyyy年MM月dd日 hh:mm:ss',
            modifyTime: 'yyyy年MM月dd日 hh:mm:ss',
            floor: 0,
            contentHtml: '',
            // TODO 回复、删帖
        }, option);

        var $listItem = $('<div class="list-group-item"><div class="row"></div></div>');
        var $listItemRow = $listItem.find('div.row');

        // 1. 添加用户信息div
        (function ($) {
            var $userDiv = $('<div class="col-md-2 text-center">' +
                '<div class="row"><p>' + options.username + '</p></div>' +
                '<div class="row"><img style="width: 80%" src="' + options.avatarUrl + '"/></div>' +
                '<div class="row"><p>主题数量: ' + options.threadNum + '<br/>回帖数量: ' + options.postNum + '</p></div>' +
                '</div>');
            $listItemRow.append($userDiv);
        })(jQuery);

        // 2. 添加内容div
        (function ($) {
            var $postDiv = $('<div class="col-md-10">' +
                '    <div class="row">' +
                '        <div class="col-md-10"><small>' + options.createTime + '</small></div>' +
                '        <div class="col-md-2 text-right">' + options.floor + '楼</div>' +
                '    </div>' +
                '    <hr/>' +
                (!!options.modifyTime ? '<div class="row"><div class="col-md-5"><small>修改时间:' + options.modifyTime + '</small></div></div>' : '') +
                '    <div class="row"><div class="col-md-12">' + options.contentHtml + '</div></div>' +
                '    <hr/>' +
                '    <div class="row">' +
                '        <div class="col-md-12">' +
                '            <a class="btn btn-default" href="#" role="button">回复</a>' +
                '            <a class="btn btn-default" href="#" role="button">编辑</a>' +
                '            <a class="btn btn-default" href="#" role="button">删帖</a>' +
                '        </div>' +
                '    </div>' +
                '</div>');
            $listItemRow.append($postDiv);
        })(jQuery);

        $(options.selector).append($listItem);
    };
    var jump = function (page) {
        var search = $('input[name="thread-name"]').val();
        getListFun({page: page, search: search});
    };
    var getListFun = function (option) {
        var options = $.extend({
            page: 1,
        }, option);

        var page = options.page;
        var threadId = $('meta[name="threadId"]').attr('content');
        $.ajax({
            type: 'get',
            url: ctx + '/api/thread-' + threadId + '/posts',
            data: {page: page},
            success: function (json) {

                var $list = $('#list-post');
                $list.children().not('#item-thread').remove();

                if (!json.result) {
                    console.log('该主题暂无回复');
                    return;
                }

                $('#item-thread').css('display', page > 1 ? 'none' : 'block');

                var list = json.obj.list;
                for (var i = 0, len = list.length; i < len; i++) {
                    var item = list[i];
                    generatePost({
                        selector: '#list-post',
                        username: item.username,
                        avatarUrl: ctx + '/img/93b09a57-93a8-43f3-bf2b-6cc62530dd81%E7%89%88%E5%9B%BE1.jpg',
                        threadNum: item.thread_num,
                        postNum: item.post_num,
                        createTime: $.format.date(item.create_time || '', 'yyyy-MM-dd'),
                        modifyTime: $.format.date(item.modify_time || '', 'yyyy-MM-dd'),
                        floor: item.floor,
                        contentHtml: item.content,
                    })
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
    });
</script>
</html>
