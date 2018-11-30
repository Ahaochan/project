<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>文件上传</title>

</head>
<body>
<p>
    浏览器支持:<br/>
    IE 8<br/>
</p>
<div id="uploadForm">
    <input id="upload" name="file" type="file"/>
</div>
<p id="json"></p>
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script>
    var ctx = $('meta[name="ctx"]').attr('content');

    $.fn.extend({
        ajaxUpload: function (option) {
            var $file = $(this);
            $file.on('change', function () {
                if(!$file.val()) {
                    // console.log("没有选择文件! 请重新上传再试!");
                    return;
                }

                var options = $.extend({
                    frameName: 'frame' + new Date().getTime(), // iframe 的 name 属性
                    method: 'post',
                    url: undefined,
                    ext: [], // 允许的扩展名
                    callback: function (data) {} // 回调函数
                }, option);
                // 1. 创建隐藏的 <iframe> 和 <form>, 加入 body 中
                var $iframe = $('<iframe name="' + options.frameName + '" style="display: none;"/>');
                var $form = $('<form method="' + options.method + '" style="display: none;" target="' + options.frameName + '" action="' + options.url + '" enctype="multipart/form-data" />');
                var $fileParent = $file.parent();
                $form.append($file);
                $(document.body).append($iframe).append($form);

                // 2. 验证扩展名
                var ext = $(this).val().split('.').pop();
                if (!!options.ext || $.inArray(ext, options.ext) > -1) {
                    $form.submit();
                } else {
                    $fileParent.append($file);
                    $iframe.remove();
                    $form.remove();
                    alert('文件格式错误, 请重新选择! 只支持上传' + options.ext + '文件!');
                }

                // 3. 提交后返回的json处理
                $iframe.load(function () {
                    var data = $(this).contents().find('body').text();
                    $fileParent.append($file);
                    $iframe.remove();
                    $form.remove();
                    options.callback(JSON.parse(data));
                });
            });
        }
    });

    $('#upload').ajaxUpload({
        url: ctx+'/upload/ie/file',
        callback: function (json) {
            $('#json').html(JSON.stringify(json));
        }
    });
</script>
</body>
</html>
