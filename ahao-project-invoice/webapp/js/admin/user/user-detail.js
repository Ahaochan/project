var oldUsername;

$(document).ready(function () {
    oldUsername = $('#input-username').val();

    $.initPanel({
        url: contextPath+'admin/user/roles',
        data: {
            userId: $('#input-id').val()
        },
        panel: '#panel-role',
        inputName: 'role'
    });

    var id = $('#input-id').val();
    $.submitDetail({
        url: contextPath+'admin/user/' + ((id === undefined) ? "" : id),
        go: contextPath+'admin/users',
        data: {
            id: '#input-id',
            username: '#input-username',
            password: '#input-password',
            email: '#input-email',
            enabled: function () {
                return $('input[name="enabled"]:checked').val();
            },
            accountExpired: function () {
                return $('input[name="accountExpired"]:checked').val();
            },
            credentialsExpired: function () {
                return $('input[name="credentialsExpired"]:checked').val();
            },
            accountLocked: function () {
                return $('input[name="accountLocked"]:checked').val();
            },
            role: function () {
                var ids = [];
                $('input[name="role"]:checked').each(function () {
                    ids.push($(this).val());
                });
                return ids;
            }
        },
        rules: {
            username: {
                required: true,
                minlength: 2,
                remote: {
                    param:{
                        url : contextPath+'admin/user/checkUsername',
                        method : 'post',
                        delay: 2000,
                    },
                    depends: function (element) {
                        return oldUsername !== $(element).val();
                    }
                },
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
        messages: {
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
        }
    });
});