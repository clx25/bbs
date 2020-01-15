package com.bbs.util;

import com.google.common.net.MediaType;
import org.apache.shiro.codec.Hex;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUploadUtil {


    /**
     * 保存文件到本地
     *
     * @param multipartFile 上传的文件
     * @param path          保存文件的路径
     * @param fileName      文件名
     * @throws IOException I/O异常
     */
    public static void saveFileInLocal(MultipartFile multipartFile, String path, String fileName) throws IOException {

        File file = new File(path + fileName);

        if (file.exists()) {
            return;
        }
        multipartFile.transferTo(file);
    }

    /**
     * 检查是否符合类型
     * 计算文件哈希，并把【哈希值.真实文件类型】作为文件名返回
     *
     * @param file      文件
     * @param mediaType 上传的文件
     * @return 文件名
     * @throws IOException I/O异常
     * @throws NoSuchAlgorithmException 没有该算法
     */
    public static String mediaTypeCheck(MultipartFile file, MediaType... mediaType) throws IOException, NoSuchAlgorithmException {
        //校验文件类型
        String actualMediaType = new Tika().detect(file.getBytes());

        for (MediaType type : mediaType) {
            if (!actualMediaType.contains(type.type())) {
                throw new IllegalArgumentException("类型错误");
            }
        }
        //获取真实格式
        int index = actualMediaType.indexOf('/');
        String suffix = actualMediaType.substring(index + 1);

        //计算SHA1
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] digest = sha1.digest(file.getBytes());
        String hex = Hex.encodeToString(digest);

        //返回拼接文件名
        return hex + "." + suffix;


    }
}
