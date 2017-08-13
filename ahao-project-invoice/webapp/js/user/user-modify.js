const oldUsername = $('#input-username').val();

$(document).ready(function () {
    initForm();
    initValidator();
});

// 初始化表单界面
function initForm() {
    getRoles();
}

// 初始化角色信息
function getRoles() {
    $.ajax({
        type: 'GET',
        contentType: 'application/json',
        async: true,
        timeout: 100000,
        data: {
            userId: $('#input-id').val()
        },
        url: '/admin/user/roles',
        dataType: 'json',
        success: initRoleList,
        error: errorAlert
    });
}


function initValidator() {
    $('form').validate({
        rules: {
            username: {
                required: true,
                minlength: 2,
                // TODO 传递oldUsername
                // remote: {
                //     url : '/admin/user/checkUsername',
                //     method : 'post',
                //     delay: 2000
                // },
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            password: {
                required: true,
                rangelength: [5, 50]
            },
            passwordConfirm: {
                equalTo: "#input-password"
            },
            email: {
                required: true,
                email: true
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
        submitHandler: submitUser
    });
}

