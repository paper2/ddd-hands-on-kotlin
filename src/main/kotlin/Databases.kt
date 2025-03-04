package com.example

import org.jetbrains.exposed.sql.*

fun configureDatabases() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/postgres",
        user = "postgres",
        password = "password"
    )
}
