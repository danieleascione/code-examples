package com.dascione.examples.sqs

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

@Testcontainers
class SqsTestContainer : AutoCloseable {
    val localstack: LocalStackContainer =
        LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3"),
        ).withServices(Service.SQS)
            .also { it.start() }

    val sqsClient: SqsClient =
        SqsClient
            .builder()
            .endpointOverride(localstack.getEndpointOverride(Service.SQS))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(localstack.accessKey, localstack.secretKey),
                ),
            ).region(Region.of(localstack.region))
            .build()

    override fun close() {
        sqsClient.close()
        localstack.close()
    }
}
