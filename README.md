# Vault AWS Java Demo

Demo application for using Vault's AWS Secrets Engine.

## Usage

1. Enable the AWS Secrets Engine in Vault ([setup](https://www.vaultproject.io/docs/secrets/aws#setup))

    ```bash
    $ vault secrets enable aws
    Success! Enabled the aws secrets engine at: aws/
    ```

2. Setup IAM User for Vault

    These are the IAM credentials that Vault will use to dynamically provision IAM users & roles. See below for the minimum set of IAM permissions this user will need.

    [Minimal IAM Policy](https://www.vaultproject.io/docs/secrets/aws#example-iam-policy-for-vault)

    ```bash
    $ vault write aws/config/root \
        access_key=<access-key-id> \
        secret_key=<secret-key> \
        region=us-east-1
    Success! Data written to: aws/config/root
    ```

3. Create role in AWS Secrets Engine

    Below command creates a role "s3" in Vault with the Amazon provided AmazonS3ReadOnlyAccess policy attached.

    Using this role will cause Vault to provision IAM users in your AWS account with the AmazonS3ReadOnlyAccess policy attached. Users will be deleted at the end of the Vault lease. See the [official docs](https://www.vaultproject.io/docs/secrets/aws#aws-secrets-engine) for more information.

    ```bash
    $ vault write aws/roles/s3 \
        credential_type=iam_user \
        policy_arns=arn:aws:iam::aws:policy/AmazonS3ReadOnlyAccess
    Success! Data written to: aws/roles/s3
    ```

4. Set the VAULT_ADDR and VAULT_TOKEN environment variables

    ```bash
    export VAULT_TOKEN=s.XXXXXXXXX
    export VAULT_ADDR=https://your-vault.example.com
    ```

5. Start the demo application

    ```bash
    $ ./gradlew clean bootRun

    > Task :bootRun

    .   ____          _            __ _ _
    /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
    ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
    \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
    '  |____| .__|_| |_|_| |_\__, | / / / /
    =========|_|==============|___/=/_/_/_/
    :: Spring Boot ::                (v2.4.5)

    2021-04-21 07:55:44.368  INFO 43670 --- [           main] c.g.s.a.AwsVaultEngineDemoApplication    : Starting AwsVaultEngineDemoApplication using Java 11.0.10
    2021-04-21 07:55:44.369  INFO 43670 --- [           main] c.g.s.a.AwsVaultEngineDemoApplication    : No active profile set, falling back to default profiles: default
    2021-04-21 07:55:44.653  INFO 43670 --- [           main] o.s.cloud.context.scope.GenericScope     : BeanFactory id=e63f7c45-2f96-3ebe-aa98-98cc811115d0
    2021-04-21 07:55:44.811  INFO 43670 --- [           main] c.g.s.a.AwsVaultEngineDemoApplication    : Waiting for AWS credentials to become active
    2021-04-21 07:55:55.272  INFO 43670 --- [           main] c.g.s.a.AwsVaultEngineDemoApplication    : AWS Credentials (arn:aws:iam::<aws-account-id>:user/vault-<auth_method>-<role>-<timestamp>) are active
    2021-04-21 07:55:55.370  INFO 43670 --- [           main] c.g.s.a.AwsVaultEngineDemoApplication    : Started AwsVaultEngineDemoApplication in 12.745 seconds (JVM running for 13.038)
    ...
    2021-04-21 07:55:57.731  INFO 43670 --- [extShutdownHook] o.s.s.c.ThreadPoolTaskScheduler          : Shutting down ExecutorService

    BUILD SUCCESSFUL in 16s
    5 actionable tasks: 5 executed

    ```
