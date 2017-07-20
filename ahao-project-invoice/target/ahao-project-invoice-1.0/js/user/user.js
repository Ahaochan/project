function showDeleteModal(userId) {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    var deleteModalId = '#deleteModal';
    console.log("id1:" + userId);
    $(deleteModalId).modal();
    $(deleteModalId + 'Label').text('确认删除id为' + userId + '的用户?');
    $(deleteModalId + 'Body').text('删除是可逆的, 最高管理员仍能恢复删除数据, 确认删除?');
    $(deleteModalId + 'Button').click(function () {
        $(deleteModalId).modal('hide');

        var confirmModalId = '#confirmModal';
        $.ajax({
            type: 'DELETE',
            url: '/admin/user/' + userId,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (result) {
                $(confirmModalId).modal();
                $(confirmModalId + 'Body').text(result.msg);
                setInterval(function () {
                    location.href = '/admin/user';
                }, 3000);
            },
            error: function (xhr) {
                $('notification').addClass('alert-warning')
                    .css('display', 'block')
                    .text('删除失败, 错误代码:' + xhr.status + ', 请联系最高管理员! ');
            }
        });


    })
}

function check_modify() {
    if ($('#usernameInput').text() === '') {
        //TODO 表单验证js
    }
}