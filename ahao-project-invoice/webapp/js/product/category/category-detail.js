$(document).ready(function () {

    var id = $('#input-id').val();
    $.submitDetail({
        url: '/product/category/' + ((id === undefined) ? "" : id),
        go: '/product/categorys',
        data: {
            id: '#input-id',
            name: '#input-name',
            description: '#input-description'
        },
        rules: {
            name: {
                required: true,
                minlength: 2,
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
                required: "货物类别名不能为空",
                minlength: "货物类别名长度过短",
                remote: "货物类别名已存在"
            },
            description: {
                required: "货物类别描述不能为空",
                maxlength: "货物类别描述最长为{0}个字符"
            }
        }
    });
});
