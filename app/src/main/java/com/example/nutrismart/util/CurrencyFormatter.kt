package com.example.nutrismart.util

import java.util.Locale

object CurrencyFormatter {
    fun formatTnd(amount: Double): String {
        return String.format(Locale.US, "%.3f TND", amount)
    }
}
