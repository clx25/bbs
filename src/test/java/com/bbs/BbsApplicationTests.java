package com.bbs;

import com.bbs.controller.UserController;
import com.bbs.util.savefile.SaveStrategy;
import com.bbs.util.savefile.SavedLocally;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import javax.annotation.Resource;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest

class BbsApplicationTests {




    @Resource
    public SaveStrategy savedLocally;
    @Test
    void test() {

        System.out.println(savedLocally.getClass());

    }


}
