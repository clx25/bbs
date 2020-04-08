package com.bbs.util.savefile;

import com.google.common.net.MediaType;
import org.apache.shiro.codec.Hex;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 保存文件的策略
 */
public interface SaveStrategy {

    void save(MultipartFile multipartFile, String path, String fileName) throws IOException;

    default String fileHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        //计算SHA1
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] digest = sha1.digest(file.getBytes());

        //返回文件哈希值
        return Hex.encodeToString(digest);
    }

    @SuppressWarnings("UnstableApiUsage")
    default String mediaTypeCheck(MultipartFile file, MediaType... mediaType) throws IOException {
        String actualMediaType = new Tika().detect(file.getBytes());

        for (MediaType type : mediaType) {
            if (!actualMediaType.contains(type.type())) {
                throw new IllegalArgumentException("类型错误");
            }
        }

        //返回真实格式
        return actualMediaType.split("/")[1];
    }

}
