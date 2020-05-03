package dev.banic.korisnicipotpore.util

object ApiUtil {

    fun getYearForIndex(index: Int): Int {
        return STARTING_YEAR + ((STARTING_MONTH + index - 1) / 12)
    }

    fun getMonthForIndex(index: Int): Int {
        return ((STARTING_MONTH + index - 1) % 12) + 1
    }

    const val STARTING_YEAR: Int = 2020
    const val STARTING_MONTH: Int = 3
}