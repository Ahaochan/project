var oldName;

$(document).ready(function () {
    oldName = $('#input-name').val();

    $.initPanel({
        url: contextPath+'admin/role/auths',
        data: {
            roleId: $('#input-id').val()
        },
        panel: '#panel-auth',
        inputName: 'auth'
    });

    var id = $('#input-id').val();
    $.submitDetail({
        url: contextPath+'admin/role/' + ((id === undefined) ? "" : id),
        go: contextPath+'admin/roles',
        data: {
            id: '#input-id',
            name: '#input-name',
            description: '#input-description',
            enabled: function () {
                return $('input[name="enabled"]:checked').val();
            },
            auth: function () {var ids = [];
                $('input[name="auth"]:checked').each(function () {
                    ids.push($(this).val());
                });
                return ids;
            }
        },
        rules: {
            name: {
                required: true,
                minlength: 2,
                remote: {
                    param:{
                        url : contextPath+'admin/role/checkName',
                        method : 'post',
                        delay: 2000,
                    },
                    depends: function (element) {
                        return oldName !== $(element).val();
                    }
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
        messages: {
            name: {
                required: "角色名不能为空",
                minlength: "角色名长度过短",
                remote: "角色名已存在"
            },
            description: {
                required: "描述不能为空",
                maxlength: "描述最长为{0}个字符"
            }
        }
    });
});