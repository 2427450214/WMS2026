package org.example.wms_backend.common;

import java.util.Random;

/**
 * 字符串工具类
 */
public class StringUtil {
    /**
     * 随机字符串字符集
     */
    private static final String RANDOM_CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 检查字符串是否为空
     * @param str 字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 检查字符串是否不为空
     * @param str 字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 生成随机字符串
     * @param length 字符串长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_CHAR_SET.charAt(random.nextInt(RANDOM_CHAR_SET.length())));
        }
        return sb.toString();
    }

    /**
     * 生成入库单ID（RK+8位随机字符）
     * @return 入库单ID
     */
    public static String generateInboundId() {
        return "RK" + generateRandomString(8);
    }

    /**
     * 生成出库单ID（CK+8位随机字符）
     * @return 出库单ID
     */
    public static String generateOutboundId() {
        return "CK" + generateRandomString(8);
    }

    /**
     * 生成商品编码（SP+8位随机字符）
     * @return 商品编码
     */
    public static String generateGoodsCode() {
        return "SP" + generateRandomString(8);
    }

    /**
     * 生成批次号（LOT+3位随机字符+当前年月）
     * @return 批次号
     */
    public static String generateBatchNo() {
        String yearMonth = DateUtil.getCurrentDateStr().substring(2).replace("-", "");
        return "LOT" + generateRandomString(3) + yearMonth;
    }

    /**
     * 生成仓库编码（WH+8位随机字符）
     * @return 仓库编码
     */
    public static String generateWarehouseCode() {
        return "WH" + generateRandomString(8);
    }
}
