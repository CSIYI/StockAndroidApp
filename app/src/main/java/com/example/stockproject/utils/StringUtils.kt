package com.example.stockproject.utils

fun Long.toBigNumberString(): String {
    return when {
        this >= 1000000000 -> String.format("%1$.2fB", this / 100000000.0)
        this >= 1000000 -> String.format("%1$.2fM", this / 1000000.0)
        this >= 1000 -> String.format("%1$.2fK", this / 1000.0)
        else -> this.toString()
    }
}