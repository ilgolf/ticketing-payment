package me.golf.infra.medium.sender

import me.golf.infra.medium.IntegrationTestModule
import me.golf.infra.sender.EmailSender
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class EmailSenderImplTest
@Autowired constructor(
    private val sut: EmailSender,
): IntegrationTestModule() {

    @Test
    @Disabled
    fun `수동 이메일 전송 테스트`() {
        val paymentId = 1001L
        val userId = 2002L
        val traceId = "manual-test-${LocalDateTime.now()}"

        try {
            sut.send(paymentId, userId, traceId)
        } catch (e: Exception) {
            println("❌ 이메일 전송 실패: ${e.message}")
            e.printStackTrace()
        }
    }
}
