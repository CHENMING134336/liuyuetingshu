package com.cm.lianting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

/**
 * @Author CM
 * @Date 2021/2/28 15:52
 * @Description
 */
@SpringBootApplication
public class LianTingApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        SpringApplication.run(LianTingApplication.class,args);
        /*LianTing lianTing = new LianTing();
        lianTing.getBookList();*/
    }
}
