package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.model.database.ProfilePreview
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProfileDao : BaseDao<Profile>() {
    @Query("SELECT * FROM Profile WHERE pk = :pk")
    protected abstract suspend fun getProfileByIdDirect(pk: Long): Profile?

    override suspend fun getValueByIDDirect(value: Profile): Profile? {
        return getProfileByIdDirect(value.pk)
    }

    @Update(onConflict = OnConflictStrategy.REPLACE, entity = Profile::class)
    abstract suspend fun update(profile: ProfilePreview)

    @Transaction
    open suspend fun insertOrUpdatePreview(profiles: List<ProfilePreview>) {
        profiles.forEach {
            val item = getProfileByIdDirect(it.pk)
            if (item == null) {
                insert(Profile.adapt(it))
            } else {
                update(it)
            }
        }
    }

    // (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1)
    @Query("SELECT P.* FROM Profile AS P WHERE P.pk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1)")
    abstract fun getSelectedProfile(): Flow<Profile?>

    @Query("SELECT P.* FROM Profile AS P WHERE P.pk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1)")
    abstract suspend fun getSelectedProfileDirect(): Profile?
}