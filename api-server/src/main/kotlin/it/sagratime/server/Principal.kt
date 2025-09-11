package it.sagratime.server

sealed interface Principal {
    object Admin : Principal
}
