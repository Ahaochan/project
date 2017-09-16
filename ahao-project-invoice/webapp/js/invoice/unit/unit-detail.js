$(document).ready(function () {

    $('#input-tax').blur(function () {
        var taxId = $(this).val();
        var province = $.province({taxId: taxId});
        $('#input-address-auto').val(province);
    }).blur();

    var id = $('#input-id').val();
    $.submitDetail({
        url: contextPath+'invoice/unit/' + ((id === undefined) ? "" : id),
        go: contextPath+'invoice/units',
        data: {
            id: '#input-id',
            name: '#input-name',
            taxId: '#input-tax',
            address: '#input-address',
            tel: '#input-tel',
            account: '#input-account',
        },
        rules: {
            name: {
                required: true,
                minlength: 2,
                normalizer: function (value) {
                    return $.trim(value);
                }
            },
            tax: {
                unifiedSocialCreditCode: true,
                required: true,
                maxlength: 100
            },
            tel:{
                number: true,
            },
            account:{
                number: true,
                bankAccount: true
            }
        },
        messages: {
            name: {
                required: "单位名称不能为空",
                minlength: "单位名称长度过短"
            },
            tax: {
                unifiedSocialCreditCode: "统一社会信用代码错误",
                required: "统一社会信用代码不能为空",
                maxlength: "统一社会信用代码最长为{0}个字符"
            },
            tel:{
                phone: "请输入正确的手机号"
            },
            account:{
                number: "请输入纯数字",
                bankAccount: "请输入正确的银行卡号"
            }
        }
    });
});
