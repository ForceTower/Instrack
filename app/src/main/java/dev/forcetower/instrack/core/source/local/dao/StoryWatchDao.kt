package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.toolkit.database.dao.BaseDao

@Dao
abstract class StoryWatchDao : BaseDao<StoryWatch>()