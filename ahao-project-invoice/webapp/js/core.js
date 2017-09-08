(function ($) {

    // AJAX请求的CSRF配置, Form表单的CSRF配置使用data-th-action替代action即可
    (function () {
        var token = $('meta[name="_csrf"]').attr('content');
        var header = $('meta[name="_csrf_header"]').attr('content');
        $(document).ajaxSend(function (e, xhr) {
            xhr.setRequestHeader(header, token);
        });
    })();

    // 垂直字体
    (function () {
        $('.text-vertical').each(function () {
            $(this).after($(this).text().split('').join('<br/><br/>') + '<br/><br/>');
            $(this).remove();
        });
    })();

    // 扩展验证信息
    (function () {
        $.validator.setDefaults({
            ignore: []
            // any other default options and/or rules
        });

        // 验证统一社会信用代码
        $.validator.addMethod("unifiedSocialCreditCode", function (value, element) {
            $(element).val(value.toUpperCase());

            if (!$.isString(value) || value.length !== 18) {
                return false;
            }
            value = value.toUpperCase();

            var valid = false;
            var array = [];
            var dist = {
                '0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9,
                'A': 10, 'B': 11, 'C': 12, 'D': 13, 'E': 14, 'F': 15, 'G': 16, 'H': 17, 'J': 18,
                'K': 19, 'L': 20, 'M': 21, 'N': 22, 'P': 23, 'Q': 24, 'R': 25, 'T': 26, 'U': 27,
                'W': 28, 'X': 29, 'Y': 30,
                '10': 'A', '11': 'B', '12': 'C', '13': 'D', '14': 'E', '15': 'F',
                '16': 'G', '17': 'H', '18': 'J', '19': 'K', '20': 'L', '21': 'M',
                '22': 'N', '23': 'P', '24': 'Q', '25': 'R', '26': 'T', '27': 'U',
                '28': 'W', '29': 'X', '30': 'Y'
            };
            var power = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];

            for (var i = 0, len = value.length - 1; i < len; i++) {
                var num = dist[value[i]];
                if (num === undefined) {
                    return false;
                }
                array.push(num);
            }
            // 91350100M000100Y43
            // 12440200455904030C
            var sum = 0;
            for (var i = 0, len = array.length; i < len; i++) {
                sum += array[i] * power[i];
            }
            if (dist[31 - sum % 31] + '' === value.charAt(value.length - 1)) {
                valid = true;
            }
            return this.optional(element) || valid;
        }, "请输入正确的代码, 18位的数字+大写字母");

        // 验证银行卡号
        $.validator.addMethod("bankAccount", function (value, element, param, method) {
            if (!$.isString(value) || value.length < 10) {
                return false;
            }

            if (this.optional(element)) {
                return "dependency-mismatch";
            }

            method = typeof method === "string" && method || "remote";

            var previous = this.previousValue(element, method),
                validator, data, optionDataString;

            if (!this.settings.messages[element.name]) {
                this.settings.messages[element.name] = {};
            }
            previous.originalMessage = previous.originalMessage || this.settings.messages[element.name][method];
            this.settings.messages[element.name][method] = previous.message;

            param = typeof param === "string" && {url: param} || param;
            optionDataString = $.param($.extend({data: value}, param.data));
            if (previous.old === optionDataString) {
                return previous.valid;
            }

            previous.old = optionDataString;
            validator = this;
            this.startRequest(element);
            data = {};
            data[element.name] = value;
            $.ajax($.extend(true, {
                type: 'get',
                url: "/invoice/bank/" + value,
                mode: "abort",
                port: "validate" + element.name,
                dataType: "json",
                data: data,
                context: validator.currentForm,
                success: function (response) {
                    var valid = response.result === 1,
                        errors, message, submitted;

                    if (response && response.obj && response.obj.imgUrl) {
                        $('#img-account').attr('src', response.obj.imgUrl);
                    } else {
                        $('#img-account').attr('src', '');
                    }

                    validator.settings.messages[element.name][method] = previous.originalMessage;
                    if (valid) {
                        submitted = validator.formSubmitted;
                        validator.resetInternals();
                        validator.toHide = validator.errorsFor(element);
                        validator.formSubmitted = submitted;
                        validator.successList.push(element);
                        validator.invalid[element.name] = false;
                        validator.showErrors();
                    } else {
                        errors = {};
                        message = response || validator.defaultMessage(element, {
                            method: method,
                            parameters: value
                        });
                        errors[element.name] = previous.message = message;
                        validator.invalid[element.name] = true;
                        validator.showErrors(errors);
                    }
                    previous.valid = valid;
                    validator.stopRequest(element, valid);
                }
            }, param));
            return "pending";
        }, "请输入正确的银行卡号");

    })();

    var province = {
        '110000': '北京市',
        '120000': '天津市',
        '130000': '河北省',
        '130100': '河北省石家庄市',
        '130200': '河北省唐山市',
        '130300': '河北省秦皇岛市',
        '130400': '河北省邯郸市',
        '130500': '河北省邢台市',
        '130600': '河北省保定市',
        '130700': '河北省张家口市',
        '130800': '河北省承德市',
        '130900': '河北省沧州市',
        '131000': '河北省廊坊市',
        '131100': '河北省衡水市',
        '140000': '山西省',
        '140100': '山西省太原市',
        '140200': '山西省大同市',
        '140300': '山西省阳泉市',
        '140400': '山西省长治市',
        '140500': '山西省晋城市',
        '140600': '山西省朔州市',
        '140700': '山西省晋中市',
        '140800': '山西省运城市',
        '140900': '山西省忻州市',
        '141000': '山西省临汾市',
        '141100': '山西省吕梁市',
        '150000': '内蒙古自治区',
        '150100': '内蒙古自治区呼和浩特市',
        '150200': '内蒙古自治区包头市',
        '150300': '内蒙古自治区乌海市',
        '150400': '内蒙古自治区赤峰市',
        '150500': '内蒙古自治区通辽市',
        '150600': '内蒙古自治区鄂尔多斯市',
        '150700': '内蒙古自治区呼伦贝尔市',
        '150800': '内蒙古自治区巴彦淖尔市',
        '150900': '内蒙古自治区乌兰察布市',
        '152200': '内蒙古自治区兴安盟',
        '152500': '内蒙古自治区锡林郭勒盟',
        '152900': '内蒙古自治区阿拉善盟',
        '210000': '辽宁省',
        '210100': '辽宁省沈阳市',
        '210200': '辽宁省大连市',
        '210300': '辽宁省鞍山市',
        '210400': '辽宁省抚顺市',
        '210500': '辽宁省本溪市',
        '210600': '辽宁省丹东市',
        '210700': '辽宁省锦州市',
        '210800': '辽宁省营口市',
        '210900': '辽宁省阜新市',
        '211000': '辽宁省辽阳市',
        '211100': '辽宁省盘锦市',
        '211200': '辽宁省铁岭市',
        '211300': '辽宁省朝阳市',
        '211400': '辽宁省葫芦岛市',
        '220000': '吉林省',
        '220100': '吉林省长春市',
        '220200': '吉林省吉林市',
        '220300': '吉林省四平市',
        '220400': '吉林省辽源市',
        '220500': '吉林省通化市',
        '220600': '吉林省白山市',
        '220700': '吉林省松原市',
        '220800': '吉林省白城市',
        '222400': '吉林省延边朝鲜族自治州',
        '230000': '黑龙江省',
        '230100': '黑龙江省哈尔滨市',
        '230200': '黑龙江省齐齐哈尔市',
        '230300': '黑龙江省鸡西市',
        '230400': '黑龙江省鹤岗市',
        '230500': '黑龙江省双鸭山市',
        '230600': '黑龙江省大庆市',
        '230700': '黑龙江省伊春市',
        '230800': '黑龙江省佳木斯市',
        '230900': '黑龙江省七台河市',
        '231000': '黑龙江省牡丹江市',
        '231100': '黑龙江省黑河市',
        '231200': '黑龙江省绥化市',
        '232700': '黑龙江省大兴安岭地区',
        '310000': '上海市',
        '320000': '江苏省',
        '320100': '江苏省南京市',
        '320200': '江苏省无锡市',
        '320300': '江苏省徐州市',
        '320400': '江苏省常州市',
        '320500': '江苏省苏州市',
        '320600': '江苏省南通市',
        '320700': '江苏省连云港市',
        '320800': '江苏省淮安市',
        '320900': '江苏省盐城市',
        '321000': '江苏省扬州市',
        '321100': '江苏省镇江市',
        '321200': '江苏省泰州市',
        '321300': '江苏省宿迁市',
        '330000': '浙江省',
        '330100': '浙江省杭州市',
        '330200': '浙江省宁波市',
        '330300': '浙江省温州市',
        '330400': '浙江省嘉兴市',
        '330500': '浙江省湖州市',
        '330600': '浙江省绍兴市',
        '330700': '浙江省金华市',
        '330800': '浙江省衢州市',
        '330900': '浙江省舟山市',
        '331000': '浙江省台州市',
        '331100': '浙江省丽水市',
        '340000': '安徽省',
        '340100': '安徽省合肥市',
        '340200': '安徽省芜湖市',
        '340300': '安徽省蚌埠市',
        '340400': '安徽省淮南市',
        '340500': '安徽省马鞍山市',
        '340600': '安徽省淮北市',
        '340700': '安徽省铜陵市',
        '340800': '安徽省安庆市',
        '341000': '安徽省黄山市',
        '341100': '安徽省滁州市',
        '341200': '安徽省阜阳市',
        '341300': '安徽省宿州市',
        '341500': '安徽省六安市',
        '341600': '安徽省亳州市',
        '341700': '安徽省池州市',
        '341800': '安徽省宣城市',
        '350000': '福建省',
        '350100': '福建省福州市',
        '350200': '福建省厦门市',
        '350300': '福建省莆田市',
        '350400': '福建省三明市',
        '350500': '福建省泉州市',
        '350600': '福建省漳州市',
        '350700': '福建省南平市',
        '350800': '福建省龙岩市',
        '350900': '福建省宁德市',
        '360000': '江西省',
        '360100': '江西省南昌市',
        '360200': '江西省景德镇市',
        '360300': '江西省萍乡市',
        '360400': '江西省九江市',
        '360500': '江西省新余市',
        '360600': '江西省鹰潭市',
        '360700': '江西省赣州市',
        '360800': '江西省吉安市',
        '360900': '江西省宜春市',
        '361000': '江西省抚州市',
        '361100': '江西省上饶市',
        '370000': '山东省',
        '370100': '山东省济南市',
        '370200': '山东省青岛市',
        '370300': '山东省淄博市',
        '370400': '山东省枣庄市',
        '370500': '山东省东营市',
        '370600': '山东省烟台市',
        '370700': '山东省潍坊市',
        '370800': '山东省济宁市',
        '370900': '山东省泰安市',
        '371000': '山东省威海市',
        '371100': '山东省日照市',
        '371200': '山东省莱芜市',
        '371300': '山东省临沂市',
        '371400': '山东省德州市',
        '371500': '山东省聊城市',
        '371600': '山东省滨州市',
        '371700': '山东省菏泽市',
        '410000': '河南省',
        '410100': '河南省郑州市',
        '410200': '河南省开封市',
        '410300': '河南省洛阳市',
        '410400': '河南省平顶山市',
        '410500': '河南省安阳市',
        '410600': '河南省鹤壁市',
        '410700': '河南省新乡市',
        '410800': '河南省焦作市',
        '410900': '河南省濮阳市',
        '411000': '河南省许昌市',
        '411100': '河南省漯河市',
        '411200': '河南省三门峡市',
        '411300': '河南省南阳市',
        '411400': '河南省商丘市',
        '411500': '河南省信阳市',
        '411600': '河南省周口市',
        '411700': '河南省驻马店市',
        '420000': '湖北省',
        '420100': '湖北省武汉市',
        '420200': '湖北省黄石市',
        '420300': '湖北省十堰市',
        '420500': '湖北省宜昌市',
        '420600': '湖北省襄阳市',
        '420700': '湖北省鄂州市',
        '420800': '湖北省荆门市',
        '420900': '湖北省孝感市',
        '421000': '湖北省荆州市',
        '421100': '湖北省黄冈市',
        '421200': '湖北省咸宁市',
        '421300': '湖北省随州市',
        '422800': '湖北省恩施土家族苗族自治州',
        '430000': '湖南省',
        '430100': '湖南省长沙市',
        '430200': '湖南省株洲市',
        '430300': '湖南省湘潭市',
        '430400': '湖南省衡阳市',
        '430500': '湖南省邵阳市',
        '430600': '湖南省岳阳市',
        '430700': '湖南省常德市',
        '430800': '湖南省张家界市',
        '430900': '湖南省益阳市',
        '431000': '湖南省郴州市',
        '431100': '湖南省永州市',
        '431200': '湖南省怀化市',
        '431300': '湖南省娄底市',
        '433100': '湖南省湘西土家族苗族自治州',
        '440000': '广东省',
        '440100': '广东省广州市',
        '440200': '广东省韶关市',
        '440300': '广东省深圳市',
        '440400': '广东省珠海市',
        '440500': '广东省汕头市',
        '440600': '广东省佛山市',
        '440700': '广东省江门市',
        '440800': '广东省湛江市',
        '440900': '广东省茂名市',
        '441200': '广东省肇庆市',
        '441300': '广东省惠州市',
        '441400': '广东省梅州市',
        '441500': '广东省汕尾市',
        '441600': '广东省河源市',
        '441700': '广东省阳江市',
        '441800': '广东省清远市',
        '441900': '广东省东莞市',
        '442000': '广东省中山市',
        '445100': '广东省潮州市',
        '445200': '广东省揭阳市',
        '445300': '广东省云浮市',
        '450000': '广西壮族自治区',
        '450100': '广西壮族自治区南宁市',
        '450200': '广西壮族自治区柳州市',
        '450300': '广西壮族自治区桂林市',
        '450400': '广西壮族自治区梧州市',
        '450500': '广西壮族自治区北海市',
        '450600': '广西壮族自治区防城港市',
        '450700': '广西壮族自治区钦州市',
        '450800': '广西壮族自治区贵港市',
        '450900': '广西壮族自治区玉林市',
        '451000': '广西壮族自治区百色市',
        '451100': '广西壮族自治区贺州市',
        '451200': '广西壮族自治区河池市',
        '451300': '广西壮族自治区来宾市',
        '451400': '广西壮族自治区崇左市',
        '460000': '海南省',
        '460100': '海南省海口市',
        '460200': '海南省三亚市',
        '460300': '海南省三沙市',
        '460400': '海南省儋州市',
        '500000': '重庆市',
        '510000': '四川省',
        '510100': '四川省成都市',
        '510300': '四川省自贡市',
        '510400': '四川省攀枝花市',
        '510500': '四川省泸州市',
        '510600': '四川省德阳市',
        '510700': '四川省绵阳市',
        '510800': '四川省广元市',
        '510900': '四川省遂宁市',
        '511000': '四川省内江市',
        '511100': '四川省乐山市',
        '511300': '四川省南充市',
        '511400': '四川省眉山市',
        '511500': '四川省宜宾市',
        '511600': '四川省广安市',
        '511700': '四川省达州市',
        '511800': '四川省雅安市',
        '511900': '四川省巴中市',
        '512000': '四川省资阳市',
        '513200': '四川省阿坝藏族羌族自治州',
        '513300': '四川省甘孜藏族自治州',
        '513400': '四川省凉山彝族自治州',
        '520000': '贵州省',
        '520100': '贵州省贵阳市',
        '520200': '贵州省六盘水市',
        '520300': '贵州省遵义市',
        '520400': '贵州省安顺市',
        '520500': '贵州省毕节市',
        '520600': '贵州省铜仁市',
        '522300': '贵州省黔西南布依族苗族自治州',
        '522600': '贵州省黔东南苗族侗族自治州',
        '522700': '贵州省黔南布依族苗族自治州',
        '530000': '云南省',
        '530100': '云南省昆明市',
        '530300': '云南省曲靖市',
        '530400': '云南省玉溪市',
        '530500': '云南省保山市',
        '530600': '云南省昭通市',
        '530700': '云南省丽江市',
        '530800': '云南省普洱市',
        '530900': '云南省临沧市',
        '532300': '云南省楚雄彝族自治州',
        '532500': '云南省红河哈尼族彝族自治州',
        '532600': '云南省文山壮族苗族自治州',
        '532800': '云南省西双版纳傣族自治州',
        '532900': '云南省大理白族自治州',
        '533100': '云南省德宏傣族景颇族自治州',
        '533300': '云南省怒江傈僳族自治州',
        '533400': '云南省迪庆藏族自治州',
        '540000': '西藏自治区',
        '540100': '西藏自治区拉萨市',
        '540200': '西藏自治区日喀则市',
        '540300': '西藏自治区昌都市',
        '540400': '西藏自治区林芝市',
        '540500': '西藏自治区山南市',
        '542400': '西藏自治区那曲地区',
        '542500': '西藏自治区阿里地区',
        '610000': '陕西省',
        '610100': '陕西省西安市',
        '610200': '陕西省铜川市',
        '610300': '陕西省宝鸡市',
        '610400': '陕西省咸阳市',
        '610500': '陕西省渭南市',
        '610600': '陕西省延安市',
        '610700': '陕西省汉中市',
        '610800': '陕西省榆林市',
        '610900': '陕西省安康市',
        '611000': '陕西省商洛市',
        '620000': '甘肃省',
        '620100': '甘肃省兰州市',
        '620200': '甘肃省嘉峪关市',
        '620300': '甘肃省金昌市',
        '620400': '甘肃省白银市',
        '620500': '甘肃省天水市',
        '620600': '甘肃省武威市',
        '620700': '甘肃省张掖市',
        '620800': '甘肃省平凉市',
        '620900': '甘肃省酒泉市',
        '621000': '甘肃省庆阳市',
        '621100': '甘肃省定西市',
        '621200': '甘肃省陇南市',
        '622900': '甘肃省临夏回族自治州',
        '623000': '甘肃省甘南藏族自治州',
        '630000': '青海省',
        '630100': '青海省西宁市',
        '630200': '青海省海东市',
        '632200': '青海省海北藏族自治州',
        '632300': '青海省黄南藏族自治州',
        '632500': '青海省海南藏族自治州',
        '632600': '青海省果洛藏族自治州',
        '632700': '青海省玉树藏族自治州',
        '632800': '青海省海西蒙古族藏族自治州',
        '640000': '宁夏回族自治区',
        '640100': '宁夏回族自治区银川市',
        '640200': '宁夏回族自治区石嘴山市',
        '640300': '宁夏回族自治区吴忠市',
        '640400': '宁夏回族自治区固原市',
        '640500': '宁夏回族自治区中卫市',
        '650000': '新疆维吾尔自治区',
        '650100': '新疆维吾尔自治区乌鲁木齐市',
        '650200': '新疆维吾尔自治区克拉玛依市',
        '650400': '新疆维吾尔自治区吐鲁番市',
        '650500': '新疆维吾尔自治区哈密市',
        '652300': '新疆维吾尔自治区昌吉回族自治州',
        '652700': '新疆维吾尔自治区博尔塔拉蒙古自治州',
        '652800': '新疆维吾尔自治区巴音郭楞蒙古自治州',
        '652900': '新疆维吾尔自治区阿克苏地区',
        '653000': '新疆维吾尔自治区克孜勒苏柯尔克孜自治州',
        '653100': '新疆维吾尔自治区喀什地区',
        '653200': '新疆维吾尔自治区和田地区',
        '654000': '新疆维吾尔自治区伊犁哈萨克自治州',
        '654200': '新疆维吾尔自治区塔城地区',
        '654300': '新疆维吾尔自治区阿勒泰地区',
        '659000': '新疆维吾尔自治区自治区直辖县级行政区划',
        '710000': '台湾省',
        '810000': '香港特别行政区',
        '820000': '澳门特别行政区'
    };
    $.extend({
        // 根据社会统一信用代码或发票代码获取行政区划名称
        province: function (option) {
            var options = $.extend({
                taxId: undefined,
                invoiceCode: undefined
            }, option);

            var taxId = options.taxId, invoiceCode = options.invoiceCode;

            if (!!taxId) {
                return province[taxId.substring(2, 8)] || '';
            }
            if (!!invoiceCode) {
                return province[invoiceCode.substring(0, 4) + '00'] || '';
            }
            return '';
        },
        // 如果有分母, 则获取 分子/分母 的百分数形式, 如果没有分母, 则获取 分子的百分数形式
        percentage: function (portion, total) {
            if (!!total) {
                return ((portion / total) * 100).toFixed(2) + '%';
            } else {
                return (portion * 100) + '%'
            }
        },
        // 获取url中参数
        getUrlParam: function (key) {
            var url = window.location.search.substring(1);
            var params = url.split('&');
            for (var i = 0; i < params.length; i++) {
                var keyValue = params[i].split('=');
                if (keyValue[0] === String(key)) {
                    return keyValue[1];
                }
            }
        },
        // 跳转url
        goUrl: function (url) {
            setTimeout(function () {
                window.location.href = url;
            }, 1000);
        },
        // 判断是否为string
        isString: function (val) {
            return (typeof val === 'string') && val.constructor === String;
        },
        // 打印输出obj
        logObj: function (obj) {
            for (var i in obj) {
                if (obj.hasOwnProperty(i)) {
                    var value = obj[i];
                    if (typeof value === 'object') {
                        $.logObj(value);
                    } else {
                        console.log(value);
                    }
                }
            }
        },
        // 提交验证表单数据
        submitDetail: function (option) {
            var options = $.extend({
                form: 'form',
                rules: {},
                messages: undefined,
                title: '确认保存记录?',
                url: undefined,
                go: undefined,
                data: {}
            }, option);

            var form = options.form, rules = options.rules, messages = options.messages;

            $(form).validate({
                rules: rules,
                messages: messages,
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
                submitHandler: function () {
                    var title = options.title, url = options.url, go = options.go;
                    var data = {};
                    for (var i in options.data) {
                        if (options.data.hasOwnProperty(i)) {
                            var value = options.data[i];

                            if (typeof value === 'function') {
                                data[i] = value();
                            } else if (typeof value === 'string') {
                                var $element = $(value);
                                if (value.indexOf('#') > -1) {
                                    data[i] = $element.val();
                                } else {
                                    var values = [];
                                    $element.each(function () {
                                        values.push($(this).val());
                                    });
                                    data[i] = values;
                                }
                            }
                        }
                    }

                    swal({
                            title: title,
                            type: "info",
                            showCancelButton: true,
                            closeOnConfirm: false,
                            showLoaderOnConfirm: true
                        },
                        function () {
                            $('.form-group').attr('class', 'form-group');
                            $('.help-block').html('');

                            $.ajax({
                                type: 'POST',
                                timeout: 100000,
                                url: url,
                                dataType: 'json',
                                data: data,
                                success: function (data) {
                                    var json = data;
                                    if (json.result === 0) {
                                        swal('失败', json.msg, 'error');
                                        var fields = json.obj;
                                        for (var property in fields) {
                                            if (fields.hasOwnProperty(property)) {
                                                var errorMsg = fields[property].join("<br/>");
                                                $('#error-' + property).css('display', 'block').html(errorMsg);
                                                $('#form-group-' + property).attr('class', 'form-group has-error');
                                            }
                                        }
                                        return;
                                    }

                                    swal('成功', json.msg, 'success');
                                    $.goUrl(go);
                                }
                            });
                        });
                }
            });
        }
    });

    // 通用ajax错误提示
    $(document).ajaxError(function (event, xhr, options, exc) {
        swal('错误', '服务器代码:' + xhr.status, 'error');
    })
})(jQuery);

