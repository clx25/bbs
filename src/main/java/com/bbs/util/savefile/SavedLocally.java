package com.bbs.util.savefile;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 保存到本地
 */
@Component
public class SavedLocally implements SaveStrategy {
    @Override
    public void save(MultipartFile multipartFile, String path, String fileName) throws IOException {
        File file = new File(path + fileName);

        if (file.exists()) {
            return;
        }
        multipartFile.transferTo(file);
    }


}
