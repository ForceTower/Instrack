package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.instrack.core.model.ui.StoryWatchProfileSimple
import dev.forcetower.instrack.core.model.ui.StoryWatcherSimple
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class StoryWatchDao : BaseDao<StoryWatch>() {
    @Query("SELECT SW.*, P.picture FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk WHERE S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) ORDER BY SW.timestamp DESC")
    abstract fun getWatchers(): Flow<List<StoryWatcherSimple>>

    @Query("SELECT SW.userPk AS pk, SW.timestamp AS timestamp, P.username AS username, P.picture AS picture, P.name AS name FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk WHERE SW.storyPk = :pk AND S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) ORDER BY SW.timestamp DESC")
    abstract fun getWatchersOf(pk: Long): Flow<List<StoryWatchProfileSimple>>

    @Query("SELECT SW.userPk AS pk, SW.timestamp AS timestamp, P.username AS username, P.picture AS picture, P.name AS name FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk INNER JOIN ProfileBond PB ON P.pk = PB.userPk WHERE SW.storyPk = :pk AND PB.followsMe = 1 AND S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) ORDER BY SW.timestamp DESC")
    abstract fun getFollowerWatcherOf(pk: Long): Flow<List<StoryWatchProfileSimple>>

    @Query("SELECT SW.userPk AS pk, SW.timestamp AS timestamp, P.username AS username, P.picture AS picture, P.name AS name FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk INNER JOIN ProfileBond PB ON P.pk = PB.userPk WHERE SW.storyPk = :pk AND (PB.followsMe = 0 OR PB.followsMe IS NULL) AND S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) ORDER BY SW.timestamp DESC")
    abstract fun getNotFollowerWatcherOf(pk: Long): Flow<List<StoryWatchProfileSimple>>

    @Query("SELECT SW.userPk AS pk, SW.timestamp AS timestamp, P.username AS username, P.picture AS picture, P.name AS name FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk INNER JOIN ProfileBond PB ON P.pk = PB.userPk WHERE SW.storyPk = :pk AND (PB.iFollow = 0 OR PB.iFollow IS NULL) AND S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) ORDER BY SW.timestamp DESC")
    abstract fun getNotFollowWatcherOf(pk: Long): Flow<List<StoryWatchProfileSimple>>
}