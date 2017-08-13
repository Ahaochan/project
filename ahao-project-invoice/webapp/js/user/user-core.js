// 表单校验字段
let validatorMessage = {
    username: {
        required: "用户名不能为空",
        minlength: "用户名长度过短",
        remote: "用户名已存在"
    },
    password: {
        required: "密码不能为空",
        rangelength: "密码长度在{0}和{1}之间"
    },
    confirm_password: {
        equalTo: "两次密码不一致"
    },
    email: "请输入正确的邮箱地址"
};

function initRoleList(data) {
    let json = data;

    let $panel = $('#panel-role');
    $.each(json.obj, function (index, entry) {
        let $div = $('<div class="checkbox"></div>');

        if (!isTrue(entry.enabled)) {
            $div.addClass('has-error');
        }

        let $checkbox = $('<input type="checkbox" name="role" value="' + entry.id + '"/>');
        if (!isTrue(entry.enabled)) {
            $checkbox.attr('disabled', 'disabled');
        }
        if (isTrue(entry.selected)) {
            $checkbox.attr('checked', 'checked');
        }

        $div.append($('<label></label>').append($checkbox).append(entry.name));
        $panel.append($div);
    })
}


function errorAlert(xhr) {
    swal('错误', '服务器代码:' + xhr.status, 'error');
}


function submitUser() {
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

            let roleValue = [];
            $('input[name="role"]:checked').each(function () {
                roleValue.push($(this).val());
            });


            let id = $('#input-id').val();
            $.ajax({
                type: 'POST',
                async: true,
                timeout: 100000,
                url: '/admin/user/' + ((id === undefined) ? "" : id),
                dataType: 'json',
                data: {
                    id: id,
                    username: $('#input-username').val(),
                    password: $('#input-password').val(),
                    email: $('#input-email').val(),
                    enabled: $('input[name="enabled"]:checked').val(),
                    accountExpired: $('input[name="accountExpired"]:checked').val(),
                    credentialsExpired: $('input[name="credentialsExpired"]:checked').val(),
                    accountLocked: $('input[name="accountLocked"]:checked').val(),
                    role: roleValue
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
                    goUrl("/admin/users");
                },
                error: function (xhr) {
                    swal('错误', '服务器代码:' + xhr.status, 'error');
                    // TODO 错误后的form-group样式异常
                }
            });
        });
}