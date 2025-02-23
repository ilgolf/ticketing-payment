package me.golf.app.service.domain.stock.listener.dto

data class OrderCompleteEvent(val orderId: String, val ticketIds: List<Long>)