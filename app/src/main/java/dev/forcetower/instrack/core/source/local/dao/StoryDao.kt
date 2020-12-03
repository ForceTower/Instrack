package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.Story
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoryDao : BaseDao<Story>() {
    @Query("SELECT S.pk AS pk, S.previewPicture AS previewPicture, COUNT(SW.userPk) AS `count` FROM Story AS S LEFT JOIN StoryWatch AS SW ON S.pk = SW.storyPk WHERE S.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) GROUP BY S.pk, S.takenAt ORDER BY S.takenAt DESC")
    abstract fun getStoriesWithViewerCount(): Flow<List<StoryViewCount>>

    @Query("SELECT S.pk AS pk, S.previewPicture AS previewPicture, COUNT(SW.userPk) AS `count` FROM Story AS S LEFT JOIN StoryWatch AS SW ON S.pk = SW.storyPk WHERE S.expiringAt > :now AND S.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) GROUP BY S.pk, S.takenAt ORDER BY S.takenAt DESC")
    abstract fun getActiveStoriesWithViewerCount(now: Long = System.currentTimeMillis() / 1000): Flow<List<StoryViewCount>>

    @Query("SELECT S.pk AS pk, S.previewPicture AS previewPicture, COUNT(SW.userPk) AS `count` FROM Story AS S LEFT JOIN StoryWatch AS SW ON S.pk = SW.storyPk WHERE S.expiringAt < :now AND S.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) GROUP BY S.pk, S.takenAt ORDER BY S.takenAt DESC")
    abstract fun getInactiveStoriesWithViewerCount(now: Long = System.currentTimeMillis() / 1000): Flow<List<StoryViewCount>>
}
