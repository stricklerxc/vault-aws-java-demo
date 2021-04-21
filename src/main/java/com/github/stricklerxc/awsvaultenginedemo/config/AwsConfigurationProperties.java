package com.github.stricklerxc.awsvaultenginedemo.config;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.amazonaws.services.securitytoken.model.AWSSecurityTokenServiceException;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityRequest;
import com.amazonaws.services.securitytoken.model.GetCallerIdentityResult;
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
     * Validates that the aws.accessKeyId and aws.secretKey provided are valid and active before starting the application.
     *
     * This is accomplished by making sure we receive a success response from a call to STS's GetCallerIdentity API.
     * @see: https://www.vaultproject.io/docs/secrets/aws#usage
     */
    @PostConstruct
    private void checkCredentials() {
        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder
                                                .standard()
                                                .withRegion(Regions.DEFAULT_REGION)
                                                .build();

        logger.info("Waiting for AWS credentials to become active");

        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(5L, TimeUnit.MINUTES);
        while (System.nanoTime() < endTime) {
            try {
                GetCallerIdentityResult response = stsClient.getCallerIdentity(new GetCallerIdentityRequest());

                logger.info(String.format("AWS Credentials (%s) are active", response.getArn()));
                break;
            } catch (AWSSecurityTokenServiceException e) {
                logger.debug(e.getMessage());
            }
        }

        if (System.nanoTime() > endTime) {
            logger.error("AWS credentials failed to become active");
        }
    }
}