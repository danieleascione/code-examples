package com.dascione.examples.sqs

import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import org.junit.jupiter.api.Test


class FutureTest {

    @Test
    fun `waiting for multiple futures`() {
        val f1 = waitAndReturn(100, "Harry")
        val f2 = waitAndThrow(100)

        try {
            listOf(f1, f2).forEach { it.get() }
        } catch (e: Exception) {
            assert(e.message!!.contains( "Throwing..."))
        }

    }
}

private fun waitAndReturn(millis: Long, value: String): Future<String> {
    return CompletableFuture.supplyAsync {
        try {
            Thread.sleep(millis)
            return@supplyAsync value
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}

private fun waitAndThrow(millis: Long): Future<*> {
    return CompletableFuture.supplyAsync {
        try {
            Thread.sleep(millis)
        } finally {
            error("Throwing...")
        }
    }
}