function parseCode(code) {
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        async: true,
        url: '/parseCode',
        dataType: 'json',
        data: {'code': code},
        success: function (data) {
            var json = eval(data);
            if (json.result === 0) {
                showFailAjaxTip('错误的发票代码, 请检查发票');
                return;
            }
            console.log(json);
            $('#invoiceTitle').text(json.msg.city + json.msg.type);
        },
        error: function (xhr) {
            showFailAjaxTip('服务器错误, 代码:' + xhr.status);
        }
    })
}
