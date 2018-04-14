<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%-- 样式 --%>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <%--@elvariable id="thread" type="com.ahao.core.entity.IDataSet"--%>
    <meta name="threadId" content="${thread.getInt("thread_id")}"/>
    <meta name="forumId" content="${thread.getInt("forum_id")}"/>
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
                        <div class="row"><p>${thread.getString("username")}</p></div>
                        <div class="row">
                            <img style="width: 80%; max-height: 400px"
                                 src="${contextPath}/img/93b09a57-93a8-43f3-bf2b-6cc62530dd81%E7%89%88%E5%9B%BE1.jpg"/>
                        </div>
                        <div class="row"><p>主题数量: ${thread.getInt("thread_num")}<br/>回帖数量: ${thread.getInt("post_num")}
                        </p></div>
                    </div>
                    <div class="col-md-10">
                        <div class="row">
                            <div class="col-md-10">
                                <small>发布时间:${thread.getString('thread_create_time')}</small>
                            </div>
                            <div class="col-md-2 text-right">主题</div>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="col-md-5">
                                <small>修改时间:${thread.getString('thread_modify_time')}</small>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-12">${thread.getString("thread_content")}</div>
                        </div>
                        <hr/>
                        <div class="row">
                            <div class="col-md-12">
                                <a class="btn btn-default"
                                   href="${contextPath}/thread-${thread.getInt("thread_id")}/new-post">回复</a>
                                <a class="btn btn-default"
                                   href="${contextPath}/thread-${thread.getInt("thread_id")}/modify">编辑</a>
                                <a class="btn btn-default" href="javascript:void(0)" thread-id="${thread.getInt("thread_id")}">删帖</a>
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
    <div class="row">
        <div class="panel panel-default">
            <div class="panel-body">
                <form class="form" id="form-post">
                    <div class="form-group">
                        <div class="col-md-12">
                            <div id="editor"></div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <button type="submit" class="btn btn-primary btn-lg btn-block">发表回复</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<%-- 通用注脚 --%>
<%@include file="/WEB-INF/views/static/footer.jsp" %>
</body>
<%-- 通用脚本 --%>
<%@include file="/WEB-INF/views/static/script.jsp" %>
<%-- 预防 XSS 攻击 --%>
<script src="https://cdn.bootcss.com/js-xss/0.3.3/xss.min.js"></script>
<%-- wangEditor 富文本编辑器 --%>
<script src="https://unpkg.com/wangeditor@3.1.0/release/wangEditor.min.js"></script>
<script src="${contextPath}/js/core.js"></script>
<script>
    var generatePost = function (option) {
        var options = $.extend({
            selector: '',
            threadId: undefined,
            username: '用户名',
            avatarUrl: '头像',
            threadNum: 0,
            postNum: 0,
            postId: 0,
            createTime: '',
            modifyTime: '',
            floor: 0,
            contentHtml: '',
            canEdit: false,
            canDelete: false
        }, option);

        if (!options.threadId) {
            console.error('生成失败, 没有指定主题id');
            return;
        }

        var $listItem = $('<div class="list-group-item"><div class="row"></div></div>');
        var $listItemRow = $listItem.find('div.row');

        // 1. 添加用户信息div
        (function ($) {
            var $userDiv = $('<div class="col-md-2 text-center">' +
                '<div class="row"><p>' + options.username + '</p></div>' +
                '<div class="row"><img style="width: 80%; max-height: 400px" src="' + options.avatarUrl + '"/></div>' +
                '<div class="row"><p>主题数量: ' + options.threadNum + '<br/>回帖数量: ' + options.postNum + '</p></div>' +
                '</div>');
            $listItemRow.append($userDiv);
        })(jQuery);

        // 2. 添加内容div
        (function ($) {
            var $postDiv = $('<div class="col-md-10">' +
                '    <div class="row">' +
                '        <div class="col-md-10"><small>发布时间:' + options.createTime + '</small></div>' +
                '        <div class="col-md-2 text-right">' + options.floor + '楼</div>' +
                '    </div>' +
                '    <hr/>' +
                (!!options.modifyTime ? '<div class="row"><div class="col-md-5"><small>修改时间:' + options.modifyTime + '</small></div></div>' : '') +
                '    <div class="row"><div class="col-md-12">' + options.contentHtml + '</div></div>' +
                '    <hr/>' +
                '    <div class="row">' +
                '        <div class="col-md-12">' +
                '            <a class="btn btn-default" href="' + ctx + '/thread-' + options.threadId + '/new-post?prePostId=' + options.postId + '">回复</a>' +
                (options.canEdit ? '<a class="btn btn-default" href="' + ctx + '/post-' + options.postId + '/modify">编辑</a>' : '') +
                (options.canDelete ? '<a class="btn btn-default" href="javascript:void(0)" post-id="'+options.postId+'">删帖</a>' : '') +
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
                        threadId: threadId,
                        username: item.username,
                        avatarUrl: ctx + '/img/93b09a57-93a8-43f3-bf2b-6cc62530dd81%E7%89%88%E5%9B%BE1.jpg',
                        threadNum: item.thread_num,
                        postNum: item.post_num,
                        postId: item.post_id,
                        createTime: !!item.create_time ? $.format.date(item.create_time, 'yyyy-MM-dd') : '',
                        modifyTime: !!item.modify_time ? $.format.date(item.modify_time, 'yyyy-MM-dd') : '',
                        floor: item.floor,
                        contentHtml: item.content,
                        canEdit: item.can_edit,
                        canDelete: item.can_delete
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

        // 2. 删除回复
        (function ($) {
            $('#list-post').on('click', 'a[post-id]', function () {
                var $this = $(this);
                var postId = $this.attr('post-id');

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/post/delete',
                    data: { postIds: [].concat(postId) },
                    success: function (json) {
                        if (!json.result || !json.obj) {
                            swal({type: 'warning', title: '警告', text: '删除失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '删除成功'});

                        setTimeout(function () {
                            location.reload();
                        }, 3000)
                    }
                });
            });
        })(jQuery);

        // 3. 删除主题帖
        (function ($) {
            $('#list-post').on('click', 'a[thread-id]', function () {
                var $this = $(this);
                var threadId = $this.attr('thread-id');

                $.ajax({
                    type: 'post',
                    url: ctx + '/manager/api/thread/delete',
                    data: { threadIds: [].concat(threadId) },
                    success: function (json) {
                        if (!json.result || !json.obj) {
                            swal({type: 'warning', title: '警告', text: '删除失败'});
                            return;
                        }
                        swal({type: 'success', title: '成功', text: '删除成功'});

                        var forumId = $('meta[name="forumId"]').attr('content');
                        setTimeout(function () {
                            window.location.href = ctx + '/forum-'+forumId;
                        }, 3000)
                    }
                });
            });
        })(jQuery);

        // 2. 初始化回复表单
        (function () {
            // 2.1. 富文本编辑插件
            var editor;
            (function ($) {
                editor = new RichEditor();
                editor.init({
                    uploadImgUrl: ctx + '/upload/img',
                    selector: '#editor',
                    filePath: 'post-img',
                });
            })(jQuery);

            // 2.2. ajax提交表单
            (function ($) {
                $('#form-post').submit(function (e) {
                    e.preventDefault();

                    var postId = 0;
                    var threadId = $('meta[name="threadId"]').attr('content');
                    var content = filterXSS(editor.html());

                    if (!content.trim()) {
                        swal({type: 'warning', title: '警告', text: '内容不能为空!'});
                        return;
                    }

                    $.ajax({
                        type: 'post',
                        url: ctx + '/api/post/save',
                        data: {
                            postId: postId,
                            threadId: threadId,
                            content: content
                        },
                        success: function (json) {
                            if (!json.result || !json.obj) {
                                swal({type: 'warning', title: '警告', text: '保存失败'});
                                return;
                            }
                            swal({type: 'success', title: '成功', text: '保存成功'});

                            setTimeout(function () {
                                window.location.href = ctx + '/thread-' + threadId;
                            }, 3000)
                        }
                    });
                });
            })(jQuery);
        })(jQuery);
    });
</script>
</html>
