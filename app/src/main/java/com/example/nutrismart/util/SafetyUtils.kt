package com.example.nutrismart.util

import android.util.Log

/**
 * SAFE NULL HANDLING UTILITIES
 *
 * This object provides safe utilities and extension functions to prevent
 * crashes caused by null pointer exceptions and unsafe operations.
 */
object SafetyUtils {

    internal const val TAG = "SafetyUtils"

    /**
     * Safe list access - returns null instead of crashing
     */
    fun <T> safeFirst(list: List<T>?): T? {
        return try {
            if (list?.isNotEmpty() == true) list.first() else null
        } catch (e: Exception) {
            Log.e(TAG, "Error accessing first element", e)
            null
        }
    }

    /**
     * Safe list access by index
     */
    fun <T> safeGet(list: List<T>?, index: Int): T? {
        return try {
            if (list != null && index >= 0 && index < list.size) list[index] else null
        } catch (e: Exception) {
            Log.e(TAG, "Error accessing element at index $index", e)
            null
        }
    }

    /**
     * Safe string operations - returns empty string if null or blank
     */
    fun orEmpty(str: String?): String {
        return str?.takeIf { it.isNotBlank() } ?: ""
    }

    /**
     * Safe list operations - returns empty list if null
     */
    fun <T> orEmptyList(list: List<T>?): List<T> {
        return list ?: emptyList()
    }

    /**
     * Safe try-catch wrapper for operations
     */
    inline fun <T> safeCall(
        block: () -> T,
        default: T,
        onError: (Exception) -> Unit = {}
    ): T {
        return try {
            block()
        } catch (e: Exception) {
            Log.e("SafetyUtils", "Error in safe call", e)
            onError(e)
            default
        }
    }

    /**
     * Safe null coalescing
     */
    fun <T> coalesce(vararg values: T?): T? {
        return values.firstOrNull { it != null }
    }
}

/**
 * EXTENSION FUNCTIONS FOR SAFER CODE
 */

/**
 * Safe list access - returns null instead of crashing
 */
fun <T> List<T>?.safeFirst(): T? = if (this?.isNotEmpty() == true) this.first() else null

/**
 * Safe list access by index
 */
fun <T> List<T>?.safeGet(index: Int): T? {
    return if (this != null && index >= 0 && index < this.size) this[index] else null
}

/**
 * Safe list operations - returns empty list if null
 */
fun <T> List<T>?.orEmptyList(): List<T> = this ?: emptyList()

/**
 * Safe string operations - handles null and blank strings
 */
fun String?.orEmpty(): String = this?.takeIf { it.isNotBlank() } ?: ""

/**
 * Safe Boolean operations
 */
fun Boolean?.orFalse(): Boolean = this == true
fun Boolean?.orTrue(): Boolean = this != false

/**
 * Safe Integer operations
 */
fun Int?.orZero(): Int = this ?: 0

/**
 * Safe parsing with fallback
 */
fun String?.safeToInt(default: Int = 0): Int {
    return this?.toIntOrNull() ?: default
}

fun String?.toIntSafe(default: Int = 0): Int = this.safeToInt(default)

/**
 * Validate string is not empty
 */
fun String?.isValidString(): Boolean = !this.isNullOrBlank()

/**
 * Validate list has items
 */
fun <T> List<T>?.hasItems(): Boolean = this != null && this.isNotEmpty()

/**
 * Safe empty string handling with custom default
 */
fun String?.orDefault(default: String = ""): String {
    return if (this.isNullOrBlank()) default else this
}

/**
 * Safe number conversions
 */
fun String?.toDoubleSafe(default: Double = 0.0): Double {
    return try {
        this?.toDoubleOrNull() ?: default
    } catch (e: Exception) {
        default
    }
}

/**
 * Log with null safety
 */
fun Any?.toLog(): String = this?.toString() ?: "null"
