package it.sagratime.app.core.feature.cards.search.components

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun LocalDateTime.toLocalDate() = LocalDate(year, month, day)
