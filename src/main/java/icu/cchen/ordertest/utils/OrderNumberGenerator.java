package icu.cchen.ordertest.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 订单号生成器
 *
 * @author 罗汉
 * @date 2024/08/07
 */
public class OrderNumberGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generateOrderNumber() {
        // 获取当前时间的时间戳，格式为 yyyyMMddHHmmss
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);

        // 生成一个 UUID，并取其前 8 位
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        // 结合时间戳和 UUID 生成订单号
        return timestamp + "-" + uuid;
    }
}
