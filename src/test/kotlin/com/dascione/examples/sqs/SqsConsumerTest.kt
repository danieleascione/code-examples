import com.dascione.examples.sqs.SqsConsumer
import com.dascione.examples.sqs.SqsTestContainer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqsConsumerTest {
    val container = SqsTestContainer()
    val createQueueRequest = CreateQueueRequest.builder().queueName("test-queue").build()
    val sqsClient = container.sqsClient
    val queueUrl = sqsClient.createQueue(createQueueRequest).queueUrl()

    @Test
    fun `happy path - message is received, processed and deleted`() {
        val service = SqsConsumer(sqsClient, queueUrl)
        val messageBody = "Hello, SQS!"
        service.sendMessage(messageBody)

        var receivedMessage: String? = null
        service.receiveAndProcessMessages { message -> receivedMessage = message }
        assert(receivedMessage == messageBody)

        val messages = sqsClient.receiveMessage { it.queueUrl(queueUrl).maxNumberOfMessages(1) }.messages()
        assert(messages.size == 0)
    }

    @Test
    fun `unhappy path - message is received, exception thrown, and message is received again after visibility timeout`() {
        val consumer = SqsConsumer(sqsClient, queueUrl)
        val messageBody = "Error-prone message"
        consumer.sendMessage(messageBody)

        var processCount = 0
        var lastReceivedMessage: String? = null

        fun processMessage(message: String) {
            println("processMessage: $message, at ${LocalDateTime.now()}")
            processCount++
            lastReceivedMessage = message
        }

        // First attempt - throw an exception
        consumer.receiveAndProcessMessages { message ->
            processMessage(message)
            throw RuntimeException("Simulated processing error")
        }

        assert(processCount == 1)
        assert(lastReceivedMessage == messageBody)

        // Wait for the visibility timeout to expire
        eventually(duration = (consumer.configuration.visibilityTimeout * 2)) {
            println(LocalDateTime.now())
            assert(processCount == 1)
        }

        // Second attempt - process successfully
        consumer.receiveAndProcessMessages { message -> processMessage(message) }

        eventually(duration = (consumer.configuration.visibilityTimeout * 20)) {
            println("${LocalDateTime.now()} -- $processCount")
            assert(processCount == 2)
        }
        assert(lastReceivedMessage == messageBody)

        // Verify that the message has been processed and deleted
        val messages = sqsClient.receiveMessage { it.queueUrl(queueUrl).maxNumberOfMessages(1) }.messages()
        assert(messages.isEmpty())
    }

    private fun eventually(
        duration: Duration,
        function: () -> Unit,
    ) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + duration.inWholeMilliseconds

        while (System.currentTimeMillis() < endTime) {
            try {
                function()
                return // If the function succeeds, exit the loop
            } catch (e: AssertionError) {
                // If an assertion fails, wait a bit before retrying
                Thread.sleep(100)
            }
        }

        // If we've reached this point, the function never succeeded within the time limit
        function() // Run one last time to get the actual failure
    }

    @AfterAll
    fun tearDown() {
        container.close()
    }
}
