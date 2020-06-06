package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.instrack.core.model.ui.StoryWatcherSimple
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoryWatchDao : BaseDao<StoryWatch>() {
    @Query("SELECT SW.*, P.picture FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk WHERE S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1)")
    abstract fun getWatchers(): Flow<List<StoryWatcherSimple>>
}