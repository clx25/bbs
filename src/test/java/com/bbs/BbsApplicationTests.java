package com.bbs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest
class BbsApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    Environment environment;

    @Test
    void test() {
        String activeProfile = applicationContext.getEnvironment().getActiveProfiles()[0];

        Profiles dev = Profiles.of("dev");
        boolean b = environment.acceptsProfiles(dev);


    }


}
