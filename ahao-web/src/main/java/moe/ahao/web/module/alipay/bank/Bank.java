package moe.ahao.web.module.alipay.bank;

import moe.ahao.commons.http.HttpClientHelper;
import moe.ahao.commons.http.convert.UTF8Convert;
import moe.ahao.commons.http.param.impl.UrlEncodedFormParam;
import moe.ahao.util.commons.io.JSONHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于 https://github.com/digglife/cnbankcard 实现的一个简单银行枚举类
 */
public enum Bank {
    SRCB("SRCB", "深圳农村商业银行"),
    BGB("BGB", "广西北部湾银行"),
    SHRCB("SHRCB", "上海农村商业银行"),
    BJBANK("BJBANK", "北京银行"),
    WHCCB("WHCCB", "威海市商业银行"),
    BOZK("BOZK", "周口银行"),
    KORLABANK("KORLABANK", "库尔勒市商业银行"),
    SPABANK("SPABANK", "平安银行"),
    SDEB("SDEB", "顺德农商银行"),
    HURCB("HURCB", "湖北省农村信用社"),
    WRCB("WRCB", "无锡农村商业银行"),
    BOCY("BOCY", "朝阳银行"),
    CZBANK("CZBANK", "浙商银行"),
    HDBANK("HDBANK", "邯郸银行"),
    BOC("BOC", "中国银行"),
    BOD("BOD", "东莞银行"),
    CCB("CCB", "中国建设银行"),
    ZYCBANK("ZYCBANK", "遵义市商业银行"),
    SXCB("SXCB", "绍兴银行"),
    GZRCU("GZRCU", "贵州省农村信用社"),
    ZJKCCB("ZJKCCB", "张家口市商业银行"),
    BOJZ("BOJZ", "锦州银行"),
    BOP("BOP", "平顶山银行"),
    HKB("HKB", "汉口银行"),
    SPDB("SPDB", "上海浦东发展银行"),
    NXRCU("NXRCU", "宁夏黄河农村商业银行"),
    NYNB("NYNB", "广东南粤银行"),
    GRCB("GRCB", "广州农商银行"),
    BOSZ("BOSZ", "苏州银行"),
    HZCB("HZCB", "杭州银行"),
    HSBK("HSBK", "衡水银行"),
    HBC("HBC", "湖北银行"),
    JXBANK("JXBANK", "嘉兴银行"),
    HRXJB("HRXJB", "华融湘江银行"),
    BODD("BODD", "丹东银行"),
    AYCB("AYCB", "安阳银行"),
    EGBANK("EGBANK", "恒丰银行"),
    CDB("CDB", "国家开发银行"),
    TCRCB("TCRCB", "江苏太仓农村商业银行"),
    NJCB("NJCB", "南京银行"),
    ZZBANK("ZZBANK", "郑州银行"),
    DYCB("DYCB", "德阳商业银行"),
    YBCCB("YBCCB", "宜宾市商业银行"),
    SCRCU("SCRCU", "四川省农村信用"),
    KLB("KLB", "昆仑银行"),
    LSBANK("LSBANK", "莱商银行"),
    YDRCB("YDRCB", "尧都农商行"),
    CCQTGB("CCQTGB", "重庆三峡银行"),
    FDB("FDB", "富滇银行"),
    JSRCU("JSRCU", "江苏省农村信用联合社"),
    JNBANK("JNBANK", "济宁银行"),
    CMB("CMB", "招商银行"),
    JINCHB("JINCHB", "晋城银行JCBANK"),
    FXCB("FXCB", "阜新银行"),
    WHRCB("WHRCB", "武汉农村商业银行"),
    HBYCBANK("HBYCBANK", "湖北银行宜昌分行"),
    TZCB("TZCB", "台州银行"),
    TACCB("TACCB", "泰安市商业银行"),
    XCYH("XCYH", "许昌银行"),
    CEB("CEB", "中国光大银行"),
    NXBANK("NXBANK", "宁夏银行"),
    HSBANK("HSBANK", "徽商银行"),
    JJBANK("JJBANK", "九江银行"),
    NHQS("NHQS", "农信银清算中心"),
    MTBANK("MTBANK", "浙江民泰商业银行"),
    LANGFB("LANGFB", "廊坊银行"),
    ASCB("ASCB", "鞍山银行"),
    KSRB("KSRB", "昆山农村商业银行"),
    YXCCB("YXCCB", "玉溪市商业银行"),
    DLB("DLB", "大连银行"),
    DRCBCL("DRCBCL", "东莞农村商业银行"),
    GCB("GCB", "广州银行"),
    NBBANK("NBBANK", "宁波银行"),
    BOYK("BOYK", "营口银行"),
    SXRCCU("SXRCCU", "陕西信合"),
    GLBANK("GLBANK", "桂林银行"),
    BOQH("BOQH", "青海银行"),
    CDRCB("CDRCB", "成都农商银行"),
    QDCCB("QDCCB", "青岛银行"),
    HKBEA("HKBEA", "东亚银行"),
    HBHSBANK("HBHSBANK", "湖北银行黄石分行"),
    WZCB("WZCB", "温州银行"),
    TRCB("TRCB", "天津农商银行"),
    QLBANK("QLBANK", "齐鲁银行"),
    GDRCC("GDRCC", "广东省农村信用社联合社"),
    ZJTLCB("ZJTLCB", "浙江泰隆商业银行"),
    GZB("GZB", "赣州银行"),
    GYCB("GYCB", "贵阳市商业银行"),
    CQBANK("CQBANK", "重庆银行"),
    DAQINGB("DAQINGB", "龙江银行"),
    CGNB("CGNB", "南充市商业银行"),
    SCCB("SCCB", "三门峡银行"),
    CSRCB("CSRCB", "常熟农村商业银行"),
    SHBANK("SHBANK", "上海银行"),
    JLBANK("JLBANK", "吉林银行"),
    CZRCB("CZRCB", "常州农村信用联社"),
    BANKWF("BANKWF", "潍坊银行"),
    ZRCBANK("ZRCBANK", "张家港农村商业银行"),
    FJHXBC("FJHXBC", "福建海峡银行"),
    ZJNX("ZJNX", "浙江省农村信用社联合社"),
    LZYH("LZYH", "兰州银行"),
    JSB("JSB", "晋商银行"),
    BOHAIB("BOHAIB", "渤海银行"),
    CZCB("CZCB", "浙江稠州商业银行"),
    YQCCB("YQCCB", "阳泉银行"),
    SJBANK("SJBANK", "盛京银行"),
    XABANK("XABANK", "西安银行"),
    BSB("BSB", "包商银行"),
    JSBANK("JSBANK", "江苏银行"),
    FSCB("FSCB", "抚顺银行"),
    HNRCU("HNRCU", "河南省农村信用"),
    COMM("COMM", "交通银行"),
    XTB("XTB", "邢台银行"),
    CITIC("CITIC", "中信银行"),
    HXBANK("HXBANK", "华夏银行"),
    HNRCC("HNRCC", "湖南省农村信用社"),
    DYCCB("DYCCB", "东营市商业银行"),
    ORBANK("ORBANK", "鄂尔多斯银行"),
    BJRCB("BJRCB", "北京农村商业银行"),
    XYBANK("XYBANK", "信阳银行"),
    ZGCCB("ZGCCB", "自贡市商业银行"),
    CDCB("CDCB", "成都银行"),
    HANABANK("HANABANK", "韩亚银行"),
    CMBC("CMBC", "中国民生银行"),
    LYBANK("LYBANK", "洛阳银行"),
    GDB("GDB", "广东发展银行"),
    ZBCB("ZBCB", "齐商银行"),
    CBKF("CBKF", "开封市商业银行"),
    H3CB("H3CB", "内蒙古银行"),
    CIB("CIB", "兴业银行"),
    CRCBANK("CRCBANK", "重庆农村商业银行"),
    SZSBK("SZSBK", "石嘴山银行"),
    DZBANK("DZBANK", "德州银行"),
    SRBANK("SRBANK", "上饶银行"),
    LSCCB("LSCCB", "乐山市商业银行"),
    JXRCU("JXRCU", "江西省农村信用"),
    ICBC("ICBC", "中国工商银行"),
    JZBANK("JZBANK", "晋中市商业银行"),
    HZCCB("HZCCB", "湖州市商业银行"),
    NHB("NHB", "南海农村信用联社"),
    XXBANK("XXBANK", "新乡银行"),
    JRCB("JRCB", "江苏江阴农村商业银行"),
    YNRCC("YNRCC", "云南省农村信用社"),
    ABC("ABC", "中国农业银行"),
    GXRCU("GXRCU", "广西省农村信用"),
    PSBC("PSBC", "中国邮政储蓄银行"),
    BZMD("BZMD", "驻马店银行"),
    ARCU("ARCU", "安徽省农村信用社"),
    GSRCU("GSRCU", "甘肃省农村信用"),
    LYCB("LYCB", "辽阳市商业银行"),
    JLRCU("JLRCU", "吉林农信"),
    URMQCCB("URMQCCB", "乌鲁木齐市商业银行"),
    XLBANK("XLBANK", "中山小榄村镇银行"),
    CSCB("CSCB", "长沙银行"),
    JHBANK("JHBANK", "金华银行"),
    BHB("BHB", "河北银行"),
    NBYZ("NBYZ", "鄞州银行"),
    LSBC("LSBC", "临商银行"),
    BOCD("BOCD", "承德银行"),
    SDRCU("SDRCU", "山东农信"),
    NCB("NCB", "南昌银行"),
    TCCB("TCCB", "天津银行"),
    WJRCB("WJRCB", "吴江农商银行"),
    CBBQS("CBBQS", "城市商业银行资金清算中心"),
    HBRCU("HBRCU", "河北省农村信用社");

    private static final Logger logger = LoggerFactory.getLogger(Bank.class);
    private String code;
    private String name;

    Bank(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 解析银行卡号, 返回 enum
     * @param card 银行卡号
     */
    public static Bank parseCard(String card) {
        // 1. 发送请求
        String json = HttpClientHelper.get("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json")
                .paramType(UrlEncodedFormParam::new)
                .addParam("_input_charset", "utf-8")
                .addParam("cardNo", card)
                .addParam("cardBinCheck", "true")
                .execute(true)
                .convert(UTF8Convert::new);
        String bankCode = JSONHelper.getString(json, "bank");

        // 2. 获取 枚举
        List<Bank> filter = Arrays.stream(values())
                .filter(e -> e.code.equals(bankCode))
                .collect(Collectors.toList());
        int filterSize = CollectionUtils.size(filter);
        if (filterSize > 1) {
            logger.error("匹配到的银行数目大于1! " + JSONHelper.toString(filter));
            throw new IllegalStateException("匹配到的银行数目大于1!" + JSONHelper.toString(filter));
        }
        if (filterSize == 0) {
            logger.error("匹配到的银行数目为0!");
            return null;
        }
        return filter.get(0);

    }

    /**
     * 返回银行Logo图片地址
     */
    public String getBankLogo() {
        return "https://apimg.alipay.com/combo.png?d=cashier&t=" + this.code;
    }


    // ============================= Getter ===================================
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
