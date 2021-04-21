package com.github.stricklerxc.awsvaultenginedemo;

import com.github.stricklerxc.awsvaultenginedemo.service.S3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsVaultEngineDemoApplication implements CommandLineRunner {
    @Autowired
    S3Service s3;

    public static void main(String[] args) {
        SpringApplication.run(AwsVaultEngineDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int failures = 0;
        while (failures < 10) {
            try {
                s3.printBuckets();

                break;
            } catch (Exception e) {
                System.out.println("Failed to print buckets");

                failures++;
            }
        }
    }
}
