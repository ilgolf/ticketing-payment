package me.golf.infra.sender

import me.golf.infra.config.MailProperties
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

interface EmailSender {

    fun send(paymentId: Long, userId: Long, traceId: String)
}

@Component
internal class EmailSenderImpl(
    private val javaMailSender: JavaMailSender,
    private val mailProperties: MailProperties
) : EmailSender {

    @Async
    override fun send(paymentId: Long, userId: Long, traceId: String) {
        val content = EMAIL_TEMPLATE.trimIndent().format(paymentId, userId, traceId)

        println("username: ${mailProperties.username}")
        println("password: ${mailProperties.password}")

        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setFrom(mailProperties.username)
        helper.setTo(ADMIN_EMAIL)
        helper.setSubject(EMAIL_TITLE)
        helper.setText(content, true)

        javaMailSender.send(message)
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