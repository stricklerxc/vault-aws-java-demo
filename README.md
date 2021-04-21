# Vault AWS Java Demo

Demo application for using Vault's AWS Secrets Engine.

## Usage

1. Export your Vault Token to the VAULT_TOKEN environment variable

    ```bash
    export VAULT_TOKEN=s.XXXXXXXXX
    ```

2. Start the application

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
