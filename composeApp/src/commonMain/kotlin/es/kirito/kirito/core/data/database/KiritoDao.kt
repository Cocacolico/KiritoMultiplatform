package es.kirito.kirito.core.data.database

import es.kirito.kirito.core.data.sqldelight.KiritoDatabase
import es.kirito.kirito.core.data.sqldelight.User

class KiritoDao(private val db: KiritoDatabase) {
    private val userQueries = db.userQueries

    fun getAllUsers(): List<User> {
        return userQueries.selectAllUsers().executeAsList()
    }

    fun insertUser(user: User) {
        userQueries.insertUser(user)
    }

    fun deleteAllUsers() {
        userQueries.deleteAllUsers()
    }




}