package com.github.stricklerxc.awsvaultenginedemo.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.github.stricklerxc.awsvaultenginedemo.AwsVaultEngineDemoApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.GetCallerIdentityResponse;

@Configuration
@ConfigurationProperties("aws")
public class AwsConfigurationProperties {
    private static final Logger logger = LoggerFactory.getLogger(AwsVaultEngineDemoApplication.class);

    String accessKeyId;
    String secretAccessKey;

    String sessionToken;

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        System.setProperty("aws.accessKeyId", accessKeyId);
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
        System.setProperty("aws.secretAccessKey", secretAccessKey);
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
        System.setProperty("aws.sessionToken", sessionToken);
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Validates that the aws.accessKeyId and aws.secretKey provided are valid and active before starting the application.
     *
     * This is accomplished by making sure we receive a success response from a call to STS's GetCallerIdentity API.
     * @see: https://www.vaultproject.io/docs/secrets/aws#usage
     */
    @PostConstruct
    private void checkCredentials() {
        StsClient stsClient = StsClient.builder().build();

        logger.info("Waiting for AWS credentials to become active");

        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(30L, TimeUnit.SECONDS);
        while (System.nanoTime() < endTime) {
            try {
                GetCallerIdentityResponse response = stsClient.getCallerIdentity();

                logger.info(String.format("AWS Credentials (%s) are active", response.arn()));
                break;
            } catch (AwsServiceException e) {
                logger.debug(e.getMessage());
            } catch (SdkClientException e) {
                logger.error(e.getMessage());
                break;
            }
        }

        if (System.nanoTime() > endTime) {
            logger.error("AWS credentials failed to become active");
        }
    }
}