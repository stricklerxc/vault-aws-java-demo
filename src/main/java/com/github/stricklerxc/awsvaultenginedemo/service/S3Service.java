package com.github.stricklerxc.awsvaultenginedemo.service;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

@Service
public class S3Service {
    S3Client s3;

    S3Service() {
        s3 = S3Client.builder().build();
    }

    public void printBuckets() {
        ListBucketsResponse buckets = s3.listBuckets();

        for (Bucket bucket : buckets.buckets()) {
            System.out.println(bucket.name());
        }
    }
}
