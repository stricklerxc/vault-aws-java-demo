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
        boolean success = false;
        while (!success) {
            try {
                s3.printBuckets();

                success = true;
            } catch (Exception e) {
                System.out.println("Failed to print buckets");
            }
        }
    }
}
