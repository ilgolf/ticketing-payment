package me.golf.app.api.view.checkout

import me.golf.app.api.checkout.request.CheckoutRequest
import me.golf.core.usecase.domain.order.usecase.CheckoutUseCase
import me.golf.core.usecase.domain.order.usecase.message.CheckoutCompleteResponseMessage
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CheckoutController (
  private val checkoutUseCase: CheckoutUseCase
){

  @GetMapping("/checkout")
  fun checkoutPage(request: CheckoutRequest, model: Model): String {
    val result: CheckoutCompleteResponseMessage = checkoutUseCase.checkout(request.toMessage())

    model.addAttribute("orderId", result.orderId)
    model.addAttribute("amount", result.amount.toDouble())

    return "checkout"
  }
}