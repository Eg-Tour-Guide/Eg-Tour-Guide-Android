package com.egtourguide.home.presentation.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertDate(inputDate: String): String {
    val instant = Instant.parse(inputDate)
    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return localDate.format(formatter)
}