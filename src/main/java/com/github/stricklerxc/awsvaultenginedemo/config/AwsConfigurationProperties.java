package com.github.stricklerxc.awsvaultenginedemo.config;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AWSSecurityTokenServiceException;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.github.stricklerxc.awsvaultenginedemo.AwsVaultEngineDemoApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("aws")
public class AwsConfigurationProperties {
    private static final Logger logger = LoggerFactory.getLogger(AwsVaultEngineDemoApplication.class);

    String accessKeyId;
    String secretKey;

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        System.setProperty("aws.accessKeyId", accessKeyId);
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        System.setProperty("aws.secretKey", secretKey);
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    /**
     * Validates that the aws.accessKeyId and aws.secretKey provided have propagated across AWS endpoints.
     *
     * @see: https://www.vaultproject.io/docs/secrets/aws#usage
     * @see: https://docs.aws.amazon.com/IAM/latest/UserGuide/troubleshoot_general.html#troubleshoot_general_eventual-consistency
     */
    @PostConstruct
    private void checkCredentials() throws TimeoutException, InterruptedException {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder
                                                .standard()
                                                .withRegion(Regions.DEFAULT_REGION)
                                                .build();

        logger.info("Waiting for AWS credentials to become active");

        // Give credentials a maximum time of 1 minute to propogate across AWS endpoints
        long endTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1L, TimeUnit.MINUTES);

        // Credentials are considered propagated when we receive 5 consecutive success signals from STS (5s between STS requests)
        int sequentialSuccesses = 0;
        final int sequentialSuccessesRequired = 5;
        final long waitPeriodMillis = 5000;

        String arn = null;

        while (sequentialSuccesses < sequentialSuccessesRequired) {
            if (System.currentTimeMillis() > endTime) {
                throw new TimeoutException("AWS credentials failed to become active");
            }
            try {
                arn = stsClient.getCallerIdentity(new GetCallerIdentityRequest()).getArn();
                sequentialSuccesses++;
            } catch (AWSSecurityTokenServiceException e) {
                logger.debug(e.getMessage());
                sequentialSuccesses = 0;
            }

            Thread.sleep(waitPeriodMillis);
        }

        logger.info(String.format("AWS Credentials (%s) are active", arn));
    }
}