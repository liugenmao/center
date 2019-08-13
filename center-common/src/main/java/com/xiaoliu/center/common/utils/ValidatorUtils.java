package com.xiaoliu.center.common.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据校验工具类, 暂时只想到这几个需要验证, 如有需要请补充在下面
 *
 * @author xiaoliu
 */
public class ValidatorUtils {

    public static final String LICENSE_PLATE_REGEX = "(^[\u4E00-\u9FA5]{1}[A-Z0-9]{6}$)|(^[A-Z]{2}[A-Z0-9]{2}[A-Z0-9\u4E00-\u9FA5]{1}[A-Z0-9]{4}$)|(^[\u4E00-\u9FA5]{1}[A-Z0-9]{5}[挂学警军港澳]{1}$)|(^[A-Z]{2}[0-9]{5}$)|(^(08|38){1}[A-Z0-9]{4}[A-Z0-9挂学警军港澳]{1}$)";
    public static final String MOBILE_REGEX = "^(13|14|15|18|17)\\d{9}$";
    public static final String ANY_CHAR_EXPRESSION = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
    public static final String DOMAIN_EXPRESSION = ANY_CHAR_EXPRESSION + "+(\\." + ANY_CHAR_EXPRESSION + "+)*";
    public static final String IP_DOMAIN_REGEX = "^(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})$";
    public static final String SPECIAL_CHARACTER_REGEX = "^[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$"; // 特殊字符表达式
    public static final String CHINESE_REGEX = "^[\u4e00-\u9fa5]+$"; // 中文表达式

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof CharSequence && (obj.equals("")) || obj.equals("null") || "".equals(obj.toString().trim())) {
            return true;
        } else if (obj instanceof Number && ((Number) obj).doubleValue() == 0) {
            return true;
        } else if (obj instanceof Boolean && !((Boolean) obj)) {
            return true;
        } else if (obj instanceof Collection && ((Collection<?>) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Map && ((Map<?, ?>) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
            return true;
        }
        return false;
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isMobile(CharSequence val) {
        if (isEmpty(val)) {
            return false;
        }
        return Pattern.matches(MOBILE_REGEX, val);
    }

    /**
     * 是否合法的车牌号
     *
     * @param val
     *
     * @return
     */
    public static boolean isLicensePlate(CharSequence val) {
        if (isEmpty(val)) {
            return false;
        }
        return Pattern.matches(LICENSE_PLATE_REGEX, val);
    }

    public static boolean isEmail(CharSequence val) {
        Pattern pattern = Pattern.compile("^" + ANY_CHAR_EXPRESSION + "+(\\." + ANY_CHAR_EXPRESSION + "+)*@(" + DOMAIN_EXPRESSION + ")$", Pattern.CASE_INSENSITIVE);
        if (isEmpty(val)) {
            return false;
        }
        Matcher matcher = pattern.matcher(val);
        return matcher.matches();
    }

    public static boolean length(int min, int max, CharSequence val) {
        if (min < 0) {
            throw new IllegalArgumentException("min不能小于0");
        }
        if (max < 0) {
            throw new IllegalArgumentException("max不能小于0");
        }
        if (max < min) {
            throw new IllegalArgumentException("max不能小于min");
        }
        if (isEmpty(val)) {
            return false;
        }
        int length = val.length();
        return length >= min && length <= max;
    }

    public static boolean minSize(int min, CharSequence val) {
        if (min < 0) {
            throw new IllegalArgumentException("min不能小于0");
        }
        return val.length() < min;
    }

    public static boolean maxSize(int max, CharSequence val) {
        if (max < 0) {
            throw new IllegalArgumentException("max不能小于0");
        }
        return val.length() > max;
    }

    public static boolean isDomain(String address) {
        if (isEmpty(address)) {
            return false;
        }
        String regex = "^" + DOMAIN_EXPRESSION + "$";
        return Pattern.matches(regex, address);
    }

    public static boolean isUrl(String address) {
        String strRegex = "^((https|http|ftp|rtsp|mms)?://)" + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" // ftp的user@
                + "(([0-9]{1,3}.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-zA-Z_!~*'()-]+.)*" // 域名-
                + "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-z]." // 二级域名
                + "[a-zA-Z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        Pattern p = Pattern.compile(strRegex);
        return p.matcher(address).matches();
    }

    public static boolean possibleConnection(String address) {
        if (address.indexOf("http") != 0) {
            address = "http://" + address;
        }
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /**
             * public int getResponseCode()throws IOException 从 HTTP 响应消息获取状态码。
             * 例如，就以下状态行来说： HTTP/1.0 200 OK HTTP/1.0 401 Unauthorized 将分别返回 200
             * 和 401。 如果无法从响应中识别任何代码（即响应不是有效的 HTTP），则返回 -1。
             *
             * 返回 HTTP 状态码或 -1
             */
            int state = conn.getResponseCode();
            System.out.println(state);
            if (state == 200)
                return true;
            else
                return false;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean isIp(String ip) {
        if (isEmpty(ip)) {
            return false;
        }
        return Pattern.matches(IP_DOMAIN_REGEX, ip);
    }

    /**
     * 判断是否包含特殊字符
     *
     * @param str
     *
     * @return
     */
    public static boolean isIncludeSpecChars(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Pattern.matches(SPECIAL_CHARACTER_REGEX, str.substring(i, i + 1))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIncludeChinese(String str) {
        if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (Pattern.matches(CHINESE_REGEX, str.substring(i, i + 1))) {
                return true;
            }
        }
        return false;
    }
}