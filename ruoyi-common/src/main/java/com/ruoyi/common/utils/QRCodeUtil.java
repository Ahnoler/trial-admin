package com.ruoyi.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

/**
 * 二维码工具类
 * 
 * @author ruoyi
 */
public class QRCodeUtil {
    
    /**
     * 生成二维码Base64字符串
     * 
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return Base64编码的二维码图片
     */
    public static String generateQRCodeBase64(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                content, 
                BarcodeFormat.QR_CODE, 
                width, 
                height, 
                hints
            );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }
    
    /**
     * 生成二维码字节数组
     * 
     * @param content 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return 二维码图片字节数组
     */
    public static byte[] generateQRCodeBytes(String content, int width, int height) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 1);
            
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                content, 
                BarcodeFormat.QR_CODE, 
                width, 
                height, 
                hints
            );
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成二维码失败", e);
        }
    }
}
