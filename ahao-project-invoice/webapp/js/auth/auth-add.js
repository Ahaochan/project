$(document).ready(function () {
    initValidator();
});


function initValidator() {
    $('form').validate({
        rules: {
            name: {
                required: true,
                minlength: 2,
                remote: {
                    url: '/admin/auth/checkName',
                    method: 'post',
                    delay: 2000
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
        submitHandler: submitAuth
    });
}

