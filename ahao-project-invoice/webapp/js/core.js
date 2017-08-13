/**
 * AJAX请求的CSRF配置, Form表单的CSRF配置使用data-th-action替代action即可
 */
$(function () {
    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

/**
 * 样式设置
 */
$(".text-vertical").each(function () {
    $(this).after($(this).text().split('').join('<br/><br/>') + '<br/><br/>');
    $(this).remove();
});

/**
 * 根据name获取url参数
 * @param key 参数
 * @returns 返回key对应的值
 */
function getUrlParam(key) {
    const url = window.location.search.substring(1);
    const params = url.split('&');
    for (let i = 0; i < params.length; i++) {
        let keyValue = params[i].split('=');
        if (keyValue[0] == key) {
            return keyValue[1];
        }
    }
}

function goUrl(url) {
    setTimeout(function () {
        window.location.href = url;
    }, 1000);
}

function isTrue(flag) {
    if (flag === null || flag === undefined || flag === 'false' || flag === false) {
        return false;
    }
    return true;
}