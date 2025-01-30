package me.golf.app.api.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PaymentController {

  @GetMapping("/success")
  fun successPage(): String {
    return "success"
  }

  @GetMapping("/fail")
  fun failPage(): String {
    return "fail"
  }
}