<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/views/static/head.jsp" %>
    <title>文件上传</title>

</head>
<body>
<p>
    浏览器支持:<br/>
    IE 10+, Firefox 4.0+, Chrome 7+, Safari 5+, Opera 12+<br/>
</p>
<div id="uploadForm">
    <input id="file" type="file"/>
    <button id="upload" type="button">upload</button>
</div>
<p id="json"></p>
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script>
    jQuery(function ($) {
        var ctx = $('meta[name="ctx"]').attr('content');

        if(window.FormData === undefined) {
            $('#json').html('该浏览器不支持FormData');
            return;
        }

        $('#upload').on('click', function () {
            var formData = new FormData();
            formData.append('file', $('#file')[0].files[0]);
            $.ajax({
                type: 'POST',
                url: ctx+'/upload/file',
                cache: false,       // 上传文件不需要缓存
                data: formData,
                processData: false, // 因为data值是formData对象，不需要对数据进行处理
                contentType: false, // 因为是由<form>表单构造的FormData对象，且已经声明了属性enctype="multipart/form-data"，所以这里设置为false
                success: function (json) {
                    $('#json').html('返回结果:'+JSON.stringify(json));
                }
            });
        });
    });
</script>
</body>
</html>
