$(document).ready(function () {
    initForm();
    initValidator();
});

// 初始化表单界面
function initForm() {
    getAuths();
}

// 初始化角色信息
function getAuths() {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        async: true,
        timeout: 100000,
        url: '/admin/role/auths',
        dataType: 'json',
        success: initAuthList,
        error: errorAlert
    });
}


function initValidator() {
    $('form').validate({
        rules: {
            name: {
                required: true,
                minlength: 2,
                remote: {
                    url: '/admin/role/checkName',
                    method: 'post',
                    delay: 2000
                },
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            description: {
                required: true,
                maxlength: 100
            }
        },
        messages: validatorMessage,
        errorElement: "span",
        errorPlacement: function (error, element) {
            error.addClass('col-md-6 help-block');
            element.parent().parent().find('span').remove();
            error.insertAfter(element.parent());
        },
        highlight: function (element, errorClass, validClass) {
            $(element).parent().parent().attr('class', 'form-group has-error');
        },
        unhighlight: function (element, errorClass, validClass) {
            $(element).parent().parent().attr('class', 'form-group has-success');
        },
        submitHandler: submitRole
    });
}

