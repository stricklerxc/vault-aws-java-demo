package com.github.stricklerxc.awsvaultenginedemo.service;

import java.util.List;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

import org.springframework.stereotype.Service;

@Service
public class S3Service {
    AmazonS3 s3;

    S3Service() {
        s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }

    public void printBuckets() {
        List<Bucket> buckets = s3.listBuckets();

        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }
}
