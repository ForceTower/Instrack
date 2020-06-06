package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.instrack.core.model.LinkedProfile
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class LinkedProfileDao : BaseDao<LinkedProfile>() {
    @Query("SELECT * FROM LinkedProfile WHERE selected = 1")
    abstract fun getSelected(): Flow<LinkedProfile?>

    @Query("UPDATE LinkedProfile SET selected = 1 WHERE userPk = :pk")
    abstract suspend fun selectProfile(pk: Long)

    @Transaction
    open suspend fun insertAndSelect(profile: LinkedProfile) {
        insertIgnore(profile)
        selectProfile(profile.userPk)
    }
}