// 表单校验字段
let validatorMessage = {
    name: {
        required: "角色名不能为空",
        minlength: "角色名长度过短",
        remote: "角色名已存在"
    },
    description: {
        required: "描述不能为空",
        maxlength: "描述最长为{0}个字符"
    }
};

function initAuthList(data) {
    let json = data;

    let $panel = $('#panel-auth');
    $.each(json.obj, function (index, entry) {
        let $div = $('<div class="checkbox"></div>');

        if (!isTrue(entry.enabled)) {
            $div.addClass('has-error');
        }

        let $checkbox = $('<input type="checkbox" name="auth" value="' + entry.id + '"/>');
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


function submitRole() {
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

            let authValue = [];
            $('input[name="auth"]:checked').each(function () {
                authValue.push($(this).val());
            });


            let id = $('#input-id').val();
            $.ajax({
                type: 'POST',
                async: true,
                timeout: 100000,
                url: '/admin/role/' + ((id === undefined) ? "" : id),
                dataType: 'json',
                data: {
                    id: id,
                    name: $('#input-name').val(),
                    description: $('#input-description').val(),
                    enabled: $('input[name="enabled"]:checked').val(),
                    auth: authValue
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
                    goUrl("/admin/roles");
                },
                error: function (xhr) {
                    swal('错误', '服务器代码:' + xhr.status, 'error');
                    // TODO 错误后的form-group样式异常
                }
            });
        });
}