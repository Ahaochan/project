(function ($) {
    $.extend({
        initPanel: function (option) {
            var options = $.extend({
                url: undefined,
                data: {},
                panel: '#panel',
                inputName: ''
            }, option);

            var url = options.url, data = options.data;

            $.ajax({
                type: 'GET',
                contentType: 'application/json',
                timeout: 100000,
                data: data,
                url: url,
                dataType: 'json',
                success: function (data) {
                    var panel = options.panel, name = options.inputName;

                    var $panel = $(panel);
                    $.each(data.obj, function (index, entry) {
                        var $div = $('<div class="checkbox"></div>');

                        if (!entry.enabled) {
                            $div.addClass('has-error');
                        }

                        var $checkbox = $('<input type="checkbox" name="' + name + '" value="' + entry.id + '"/>');
                        if (!entry.enabled) {
                            $checkbox.attr('disabled', 'disabled');
                        }
                        if (!!entry.selected) {
                            $checkbox.attr('checked', 'checked');
                        }

                        $div.append($('<label></label>').append($checkbox).append(entry.name));
                        $panel.append($div);
                    })
                }
            });
        }
    });

})(jQuery);