package es.kirito.kirito.core.data.database

import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import es.kirito.kirito.core.data.sqldelight.KiritoDatabase
import es.kirito.kirito.core.data.sqldelight.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class KiritoDao(private val db: KiritoDatabase) {
    private val userQueries = db.userQueries

    fun getAllUsers(): Flow<List<User>> {
        return userQueries.selectAllUsers().asFlow().mapToList(Dispatchers.IO)
    }

    fun getUserById(id: Long): Flow<User?> {
        return userQueries.selectUserById(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    fun insertUser(user: User) {
        userQueries.insertUser(user)
    }

    fun deleteAllUsers() {
        userQueries.deleteAllUsers()
    }




}