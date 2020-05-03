package dev.banic.korisnicipotpore.data


typealias AllCompanyData = Map<Int, Map<Int, Api.CompanyPaymentData>>

fun AllCompanyData.toMutable(): MutableAllCompanyData {
    return this.mapValues {
        it.value.toMutableMap()
    }.toMutableMap()
}