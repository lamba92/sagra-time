package it.sagratime.server

val DB_PATH
    get() = System.getenv("DB_PATH") ?: "./sagra-db"

val ADMIN_USERNAME
    get() = System.getenv("ADMIN_USERNAME") ?: "admin"

val ADMIN_PASSWORD
    get() = System.getenv("ADMIN_PASSWORD") ?: "password"
