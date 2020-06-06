package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import dev.forcetower.instrack.core.model.database.Action
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class ActionDao : BaseDao<Action>()