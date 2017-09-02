var oldName;

$(document).ready(function () {
    oldName = $('#input-name').val();

    var id = $('#input-id').val();
    $.submitDetail({
        url: '/admin/auth/' + ((id === undefined) ? "" : id),
        go: '/admin/auths',
        data: {
            id: '#input-id',
            name: '#input-name',
            description: '#input-description',
            enabled: function () {
                return $('input[name="enabled"]:checked').val();
            }
        },
        rules: {
            name: {
                required: true,
                minlength: 2,
                remote: {
                    param:{
                        url : '/admin/auth/checkName',
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
                required: "权限名不能为空",
                minlength: "权限名长度过短",
                remote: "权限名已存在"
            },
            description: {
                required: "描述不能为空",
                maxlength: "描述最长为{0}个字符"
            }
        }
    });
});
