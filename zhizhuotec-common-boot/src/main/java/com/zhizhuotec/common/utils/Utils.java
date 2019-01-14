package com.zhizhuotec.common.utils;

import java.awt.Color;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhizhuotec.common.pojo.Constant;

public class Utils {

	/**
	 * 日志log
	 */
	public static void logger(String level, String msg) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement el = elements[2];
		Logger log = LoggerFactory.getLogger(el.getClassName());
		switch (level) {
		case "ERROR":
			log.error(msg);
			break;
		case "INFO":
			log.info(msg);
			break;
		case "WARN":
			log.warn(msg);
			break;
		case "DEBUG":
			log.debug(msg);
			break;
		default:
			break;
		}
	}

	/**
	 * 日志log
	 */
	public static void logger(String level, String msg, Exception e) {
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		StackTraceElement el = elements[2];
		Logger log = LoggerFactory.getLogger(el.getClassName());
		switch (level) {
		case "ERROR":
			log.error(msg, e);
			break;
		case "INFO":
			log.info(msg, e);
			break;
		case "WARN":
			log.warn(msg, e);
			break;
		case "DEBUG":
			log.debug(msg, e);
			break;
		default:
			break;
		}
	}

	/**
	 * 手机号码正则
	 */
	public static boolean verifyPhone(String identifier) {
		if (StringUtils.isNotBlank(identifier)) {
			Pattern pattern = Pattern.compile("^1(3[0-9]|4[5678]|5[0-35-9]|6[6]|7[0135678]|8[0-9]|9[8-9])\\d{8}$");
			Matcher i = pattern.matcher(identifier);
			if (i.matches()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 密码正则
	 */
	public static boolean verifyPass(String credential) {
		if (StringUtils.isNotBlank(credential)) {
			Pattern pattern = Pattern.compile("[a-zA-Z0-9_.]{6,16}");
			Matcher c = pattern.matcher(credential);
			if (c.matches()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * 返回日期格式[yyyyMMdd]
	 */
	public static int getNioFileName(Object obj) {
		return Integer.valueOf(new DateTime(obj).toString("yyyyMMdd"));
	}

	/**
	 * 返回当前时间的Timestamp
	 */
	public static Timestamp getTimestamp() {
		return Timestamp.valueOf(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 返回日期格式[yyyy-MM-dd]
	 */
	public static String getYMD(Object obj) {
		return new DateTime(obj).toString("yyyy-MM-dd");
	}

	/**
	 * 返回日期格式[yyyy-MM-dd HH:mm:ss]
	 */
	public static String getTimestampStr(Timestamp t) {
		return new DateTime(t).toString("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取年月周日
	 * 
	 * @param date
	 * @param type
	 *            0/安卓 1/后台
	 * @param plusType
	 */
	public static long days(Object date, int type, int plusType) {
		DateTime dt = new DateTime(date);
		if (plusType == 1) {
			if (type == 0) {
				return dt.dayOfWeek().withMinimumValue().plusDays(-1).getMillis();
			}
			return dt.dayOfWeek().withMinimumValue().getMillis();
		} else if (plusType == 2) {
			return dt.dayOfMonth().withMinimumValue().getMillis();
		} else if (plusType == 3) {
			return dt.dayOfYear().withMinimumValue().getMillis();
		} else if (plusType == 11) {
			if (type == 0) {
				return dt.dayOfWeek().withMaximumValue().plusDays(-1).getMillis();
			}
			return dt.dayOfWeek().withMaximumValue().getMillis();
		} else if (plusType == 12) {
			return dt.dayOfMonth().withMaximumValue().getMillis();
		} else if (plusType == 13) {
			return dt.dayOfYear().withMaximumValue().getMillis();
		} else {
			return dt.getMillis();
		}
	}

	/**
	 * 返回当天剩余的毫秒数
	 */
	public static long endOfDay() {
		DateTime dt = new DateTime();
		return dt.millisOfDay().withMaximumValue().getMillis() - dt.getMillis();
	}

	/**
	 * UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 6位数字随机验证码
	 */
	public static String code() {
		String code = "";
		for (int i = 0; i < 6; i++) {
			code += (int) (Math.random() * 10);
		}
		return "666666";
	}

	/**
	 * 创建颜色
	 */
	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 
	 * @param source
	 * @param hashType
	 *            MD5/SHA
	 * @return
	 */
	public static String getHash(String source, String hashType) {
		// 用来将字节转换成 16 进制表示的字符
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		String salting = source + Constant.SALT.getValue(); // 加盐
		StringBuilder sb = new StringBuilder(salting);
		sb.replace(0, 5, sb.substring(sb.length() - 5, sb.length()));
		sb.replace(sb.length() - 5, sb.length(), salting.substring(0, 5)); // 前后5位置换
		try {
			MessageDigest md = MessageDigest.getInstance(hashType);
			md.update(sb.toString().getBytes()); // 通过使用 update 方法处理数据,使指定的
													// byte数组更新摘要
			byte[] encryptStr = md.digest(); // 获得密文完成哈希计算,产生128 位的长整数
			char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符
			int k = 0; // 表示转换结果中对应的字符位置
			for (int i = 0; i < 16; i++) { // 从第一个字节开始，对每一个字节,转换成 16 进制字符的转换
				byte byte0 = encryptStr[i]; // 取第 i 个字节
				str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, >>>
				// // 为逻辑右移，将符号位一起右移
				str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
			}
			return new String(str); // 换后的结果转换为字符串
		} catch (NoSuchAlgorithmException e) {
			logger("ERROR", "Server exception：", e);
		}
		return null;
	}

}
