package dev.forcetower.instrack.core.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.model.ui.StoryWatchProfileSimple
import dev.forcetower.instrack.core.model.ui.StoryWatcherSimple
import dev.forcetower.instrack.core.model.ui.UserFriendship
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

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(SW.userPk) AS insight, SW.timestamp AS timestamp, P.* FROM StoryWatch SW INNER JOIN ProfileBond PB ON SW.userPk = PB.userPk INNER JOIN Profile P ON SW.userPk = P.pk INNER JOIN Story S ON SW.storyPk = S.pk WHERE S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY SW.userPk ORDER BY COUNT(SW.userPk) DESC")
    abstract fun getGreaterWatchersAndCount(): PagingSource<Int, UserFriendship>

    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(SW.userPk) AS insight, SW.timestamp AS timestamp, P.* FROM StoryWatch SW INNER JOIN ProfileBond PB ON SW.userPk = PB.userPk INNER JOIN Profile P ON SW.userPk = P.pk INNER JOIN Story S ON SW.storyPk = S.pk WHERE S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY SW.userPk ORDER BY COUNT(SW.userPk) ASC")
    abstract fun getLeastWatchersAndCount(): PagingSource<Int, UserFriendship>

    // @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(SW.userPk) AS insight, SW.timestamp AS timestamp, P.* FROM StoryWatch SW INNER JOIN ProfileBond PB ON SW.userPk = PB.userPk INNER JOIN Profile P ON SW.userPk = P.pk INNER JOIN Story S ON SW.storyPk = S.pk WHERE S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND (PB.followsMe = 0 OR PB.followsMe IS NULL) GROUP BY SW.userPk ORDER BY COUNT(SW.userPk) ASC")
    @Query("SELECT PB.iFollow AS iFollow, PB.followsMe as followsMe, count(SW.userPk) AS insight, SW.timestamp AS timestamp, P.* FROM StoryWatch AS SW INNER JOIN Story AS S ON SW.storyPk = S.pk INNER JOIN Profile P on SW.userPk = P.pk INNER JOIN ProfileBond PB ON P.pk = PB.userPk WHERE (PB.followsMe = 0 OR PB.followsMe IS NULL) AND S.userPk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) AND PB.referencePk = (SELECT LP.userPk FROM LinkedProfile AS LP WHERE LP.selected = 1 LIMIT 1) GROUP BY SW.userPk ORDER BY COUNT(SW.userPk) ASC")
    abstract fun getNotFollowerWatchersAndCount(): PagingSource<Int, UserFriendship>

    @Query("SELECT S.pk AS pk, S.previewPicture AS previewPicture, COUNT(SW.userPk) AS `count` FROM Story AS S LEFT JOIN StoryWatch AS SW ON S.pk = SW.storyPk WHERE S.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) GROUP BY S.pk ORDER BY COUNT(SW.userPk) DESC")
    abstract fun getMostWatched(): PagingSource<Int, StoryViewCount>

    @Query("SELECT S.pk AS pk, S.previewPicture AS previewPicture, COUNT(SW.userPk) AS `count` FROM Story AS S LEFT JOIN StoryWatch AS SW ON S.pk = SW.storyPk WHERE S.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) GROUP BY S.pk ORDER BY COUNT(SW.userPk) ASC")
    abstract fun getLeastWatched(): PagingSource<Int, StoryViewCount>
}