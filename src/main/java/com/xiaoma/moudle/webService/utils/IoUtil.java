package com.xiaoma.moudle.webService.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.xiaoma.moudle.webService.exception.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.util.Charsets;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;

@Slf4j
public class IoUtil extends StreamUtils {
    public IoUtil() {
    }

    public static void closeQuietly(@Nullable Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
        }

    }

    public static String toString(InputStream input) {
        return toString(input, Charsets.UTF_8);
    }

    public static String toString(@Nullable InputStream input, Charset charset) {
        String var2;
        try {
            var2 = copyToString(input, charset);
        } catch (IOException var6) {
            throw Exceptions.unchecked(var6);
        } finally {
            closeQuietly(input);
        }

        return var2;
    }

    public static byte[] toByteArray(@Nullable InputStream input) {
        byte[] var1;
        try {
            var1 = copyToByteArray(input);
        } catch (IOException var5) {
            throw Exceptions.unchecked(var5);
        } finally {
            closeQuietly(input);
        }

        return var1;
    }

    public static void write(@Nullable final String data, final OutputStream output, final Charset encoding) throws IOException {
        if (data != null) {
            output.write(data.getBytes(encoding));
        }
    }

    /**
     * @Author:湘北智造-框架开发组
     * @Date:2020/11/16
     * @Param:[inStream, filePath:图片地址]
     * @return:boolean
     * @Description:
     */
    public static String saveBit(InputStream inStream, String filePath) throws IOException {
        // 判断目录是否存在，不存在则创建目录
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = IdWorker.get32UUID() + StringPool.DOT + "jpg";
        filePath = filePath + StringPool.SLASH + fileName;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // 创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        // 每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        // 使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            // 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        // 关闭输入流
        inStream.close();
        // 把outStream里的数据写入内存

        // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
        byte[] data = outStream.toByteArray();
        // new一个文件对象用来保存图片，默认保存当前工程根目录
        File imageFile = new File(filePath);
        // 创建输出流
        FileOutputStream fileOutStream = new FileOutputStream(imageFile);
        // 写入数据
        fileOutStream.write(data);
        // 方便测试
//        String resultPath = "/static/images/flowableFile/" + fileName;
        return filePath;
    }

    /**
     * @Author:湘北智造-框架开发组
     * @Date:2020/10/27
     * @Param:[file]
     * @return:java.io.File
     * @Description:MultipartFile转File
     */
    public static File toFile(MultipartFile file) {
        int n;
        String dirPath = getProjectPath() + File.separator + "temp";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File newFile = new File(dirPath + File.separator + file.getOriginalFilename());
        InputStream in = null;
        OutputStream os = null;
        try {
            in = file.getInputStream();
            os = new FileOutputStream(newFile);
            byte[] buffer = new byte[4096];
            while ((n = in.read(buffer, 0, 4096)) != -1) {
                os.write(buffer, 0, n);
            }
            log.info("获取文件成功，暂存目录" + newFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("获取文件失败");
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                log.error("关闭流失败！");
            }
        }
        return newFile;
    }

    public static String getProjectPath() {
        try {
            return ResourceUtils.getURL(StringPool.EMPTY).getPath();
        } catch (FileNotFoundException e) {
            log.error("classpath不存在！", e);
        }
        return StringPool.EMPTY;
    }

    public static String getResourcesPath() {
        try {
            return ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            log.error("classpath不存在！", e);
        }
        return StringPool.EMPTY;
    }
}
