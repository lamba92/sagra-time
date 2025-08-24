package it.sagrabot.server

sealed interface Principal {
    object Admin : Principal
}