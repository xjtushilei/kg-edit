package com.xjtu.spider.service;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

/**
 * 下载图片
 * Created by yuanhao on 2017/3/16.
 */
@Service
public class ImageService {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        //        getImageFromNetByUrl("http://t1.mmonly.cc/uploads/allimg/150415/22691-sF0hoc.jpg");
    }

    /**
     * 得到图片链接对应的输入流，用于数据库Blob图片字段的存储
     *
     * @param imageUrl
     * @return
     */
    public byte[] getImageFromNetByUrl(String imageUrl) {
        byte[] byt = null;
        try {
            byt = IOUtils.toByteArray(new URL(imageUrl));
        } catch (IOException e) {
            logger.error("下载图片<" + imageUrl + ">失败！" + e.toString());
        }
        return byt;
    }
    //    public static byte[] getImageFromNetByUrl(String imageUrl) {
    //        InputStream inStream = null;
    //        byte[] byt = null;
    //        try {
    //            URL url = new URL(imageUrl);
    //            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    //            conn.setRequestMethod("GET");
    //            conn.setDoInput(true); // 设置应用程序要从网络连接读取数据
    //            conn.setConnectTimeout(3000); // 设置网络连接超时时间
    //            int responseCode = 0;
    //            try {
    //                responseCode = conn.getResponseCode();
    //            } catch (Exception e) {
    //                responseCode = 0;
    //            }
    //
    //            if (responseCode == 200) {
    //                // 从服务器返回一个输入流
    //                inStream = conn.getInputStream();
    //                byt = new byte[inStream.available()];
    //                inStream.read(byt);
    //            } else if (responseCode == 301) {
    //                System.out.println("301 error, 请求的网页已永久移动到新位置...");
    //            } else if (responseCode == 503) {
    //                System.out.println("503 error, 服务器不支持当前请求所需要的某个功能。当服务器无法识别请求的方法，并且无法支持其对任何资源的请求。...");
    //            } else if (responseCode == 0) {
    //                System.out.println("0 eroor, connect timed out...");
    //            }
    //
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //        return byt;
    //    }


}
