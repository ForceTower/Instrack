package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import dev.forcetower.instrack.core.model.database.ProfileHistory
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class HistoryDao : BaseDao<ProfileHistory>()
