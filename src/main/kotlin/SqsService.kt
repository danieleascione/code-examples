package com.dascione.examples.sqs

import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.SqsServiceClientConfiguration
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

class SqsServiceConfiguration(val maxNumberOfMessages: Int, val visibilityTimeout: Duration)

class SqsService(
    private val sqsClient: SqsClient,
    private val queueUrl: String,
    private val configuration: SqsServiceConfiguration = SqsServiceConfiguration(1, 2.seconds)
) {

    fun sendMessage(message: String) {
        val sendMsgRequest = SendMessageRequest.builder()
            .queueUrl(queueUrl)
            .messageBody(message)
            .build()
        sqsClient.sendMessage(sendMsgRequest)
    }

    fun receiveAndProcessMessages(processMessage: (String) -> Unit) {
        val receiveMessageRequest = ReceiveMessageRequest.builder()
            .queueUrl(queueUrl)
            .maxNumberOfMessages(configuration.maxNumberOfMessages)
            .visibilityTimeout(configuration.visibilityTimeout.toInt(DurationUnit.SECONDS))
            .build()

        val messages = sqsClient.receiveMessage(receiveMessageRequest).messages()

        messages.forEach { message ->
            try {
                processMessage(message.body())
                val deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build()
                sqsClient.deleteMessage(deleteRequest)
            } catch (e: Exception) {
                println("Failed to process message: ${e.message}")
            }
        }
    }
}
