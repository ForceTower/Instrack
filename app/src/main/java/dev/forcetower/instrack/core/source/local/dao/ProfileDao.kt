package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class ProfileDao : BaseDao<Profile>() {
    @Query("SELECT * FROM Profile WHERE pk = :pk")
    protected abstract suspend fun getProfileByIdDirect(pk: Long): Profile?

    override suspend fun getValueByIDDirect(value: Profile): Profile? {
        return getProfileByIdDirect(value.pk)
    }
}