// 表单校验字段
let validatorMessage = {
    name: {
        required: "权限名不能为空",
        minlength: "权限名长度过短",
        remote: "权限名已存在"
    },
    description: {
        required: "描述不能为空",
        maxlength: "描述最长为{0}个字符"
    }
};

function errorAlert(xhr) {
    swal('错误', '服务器代码:' + xhr.status, 'error');
}


function submitAuth() {
    swal({
            title: "确认保存记录?",
            type: "info",
            showCancelButton: true,
            closeOnConfirm: false,
            showLoaderOnConfirm: true
        },
        function () {
            $('.form-group').attr('class', 'form-group');
            $('.help-block').html('');


            let id = $('#input-id').val();
            $.ajax({
                type: 'POST',
                async: true,
                timeout: 100000,
                url: '/admin/auth/' + ((id === undefined) ? "" : id),
                dataType: 'json',
                data: {
                    id: id,
                    name: $('#input-name').val(),
                    description: $('#input-description').val(),
                    enabled: $('input[name="enabled"]:checked').val()
                },
                success: function (data) {
                    const json = data;
                    if (json.result === 0) {
                        swal('失败', json.msg, 'error');
                        const fields = json.obj;
                        for (property in fields) {
                            const errorMsg = fields[property].join("<br/>");
                            $('#error-' + property).css('display', 'block')
                                .html(errorMsg);
                            $('#form-group-' + property).attr('class', 'form-group has-error');
                        }
                        return;
                    }

                    swal('成功', json.msg, 'success');
                    goUrl("/admin/auths");
                },
                error: function (xhr) {
                    swal('错误', '服务器代码:' + xhr.status, 'error');
                    // TODO 错误后的form-group样式异常
                }
            });
        });
}