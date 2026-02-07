package com.rutchem.app

import com.rutchem.db.DriverFactory
import com.rutchem.db.RutChemDb
import com.rutchem.db.ScoreEntity
import com.rutchem.db.UserEntity
import kotlinx.datetime.Clock

class GameRepository(driverFactory: DriverFactory) {
    private val db = RutChemDb(driverFactory.createDriver())
    private val queries = db.rutChemDbQueries

    fun getActiveUser(): UserEntity? {
        return queries.selectActiveUser().executeAsOneOrNull()
    }

    fun getUsers(): List<UserEntity> {
        return queries.selectAllUsers().executeAsList()
    }

    fun addUser(name: String) {
        queries.insertUser(name)
    }

    fun setActiveUser(id: Long) {
        queries.resetActiveUsers()
        queries.setActiveUser(id)
    }

    fun addScore(score: Int, total: Int) {
        val user = getActiveUser() ?: return
        queries.insertScore(
            userId = user.id,
            score = score.toLong(),
            totalQuestions = total.toLong(),
            timestamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    fun getHistoryForActiveUser(): List<ScoreEntity> {
        val user = getActiveUser() ?: return emptyList()
        return queries.selectScoresByUser(user.id).executeAsList()
    }
}