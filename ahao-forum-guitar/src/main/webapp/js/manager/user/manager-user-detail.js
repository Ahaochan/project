$(function () {
    // 1. 显示超级版主和版主的模块选择
    var superModeratorId = $('meta[name="superModeratorId"]').attr('content');
    var moderatorId = $('meta[name="moderatorId"]').attr('content');
    (function ($) {
        $('select[name="select-role"]').on('change', function() {
            $('#panel-category').css('display', parseInt(this.value) === parseInt(superModeratorId) ? 'block' : 'none');
            $('#panel-forum').css('display', parseInt(this.value) === parseInt(moderatorId) ? 'block' : 'none');
        });
    })(jQuery);

    // 2. 保存用户信息
    (function ($) {
        $('#form-user').submit(function (e) {
            e.preventDefault();

            var userId = $('input[name="user-id"]').val();
            var username = $('input[name="user-username"]').val();
            var password = $('input[name="user-password"]').val();
            var enabled = $('input[name="user-enabled"]:checked').val();

            var avatarUrl = $('input[name="profile-avatar-url"]').val();
            var email = $('input[name="profile-email"]').val();
            var sex = $('input[name="profile-sex"]:checked').val();
            var qq = $('input[name="profile-qq"]').val();
            var city = $('input[name="profile-city"]').val();

            var role = $('select[name="select-role"]').val();
            var data = {
                userId: userId, username: username, password: password, enabled: enabled,
                email: email, sex: sex, qq: qq, city: city, avatarUrl: avatarUrl,
                roleId: role
            };

            if(parseInt(role) === parseInt(superModeratorId)){
                data.categoryIds = $('input[name="category-id"]:checked').map(function () { return this.value }).get();
            } else if(parseInt(role) === parseInt(moderatorId)){
                data.forumIds = $('input[name="forum-id"]:checked').map(function () { return this.value }).get();
            }

            $.ajax({
                type: 'post',
                url: ctx + '/manager/api/user/save',
                data: data,
                success: function (json) {
                    if (!json.result) {
                        swal({type: 'warning', title: '警告', text: '保存失败'});
                        return;
                    }
                    swal({type: 'success', title: '成功', text: '保存成功'});

                    setTimeout(function () {
                        window.location.href = ctx + '/manager/users';
                    })
                }
            });
        });
    })(jQuery);

    // 3. 文件上传插件
    (function ($) {
        var url = $('meta[name="avatar-url"]').attr('content');
        url = (!!url) ? ctx + url : undefined;

        var fileInput = new FileInput();
        fileInput.initImg({
            selector: '#input_avatar',
            uploadUrl: ctx + '/upload/img',
            placeholderImg: url,
            filePath: 'user_avatar'
        }).on('fileuploaded', function (event, data, previewId, index) {
            var json = data.response;
            if (!json.result) {
                swal({type: 'warning', title: '警告', text: '上传失败'});
                return;
            }
            $('input[name="profile-avatar-url"]').val(json.obj.url || '');
        });
    })(jQuery);
});