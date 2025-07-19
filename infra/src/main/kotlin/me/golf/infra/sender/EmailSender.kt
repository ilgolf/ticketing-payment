package me.golf.infra.sender

import me.golf.infra.config.MailProperties
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

interface EmailSender {

    fun send(orderId: String, userId: Long, traceId: String)
}

@Component
internal class EmailSenderImpl(
    private val javaMailSender: JavaMailSender,
    private val mailProperties: MailProperties
) : EmailSender {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Async
    override fun send(orderId: String, userId: Long, traceId: String) {
        val result = kotlin.runCatching {
            val content = EMAIL_TEMPLATE.trimIndent().format(orderId, userId, traceId)
            val message = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")

            helper.setFrom(mailProperties.username)
            helper.setTo(ADMIN_EMAIL)
            helper.setSubject(EMAIL_TITLE)
            helper.setText(content, true)

            javaMailSender.send(message)
        }

        result.onFailure { log.error("이메일 발송 실패 - paymentId: {}, userId: {}, traceId: {}", orderId, userId, traceId, it) }
    }

    companion object {
        const val ADMIN_EMAIL = "junghn6768@gmail.com"
        const val EMAIL_TITLE = "kafka 이벤트 처리 실패 메일입니다."
        const val EMAIL_TEMPLATE = """
            결제 이벤트 처리 실패 Message Queue와 실패한 요청 건을 빨리 확인해주세요.
            paymentId: %d
            userId: %d
            traceId: %s
       """
    }
}