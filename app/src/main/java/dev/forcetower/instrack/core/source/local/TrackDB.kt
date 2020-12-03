package dev.forcetower.instrack.core.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.forcetower.instrack.core.model.database.Action
import dev.forcetower.instrack.core.model.database.LinkedProfile
import dev.forcetower.instrack.core.model.database.Post
import dev.forcetower.instrack.core.model.database.PostComment
import dev.forcetower.instrack.core.model.database.PostLike
import dev.forcetower.instrack.core.model.database.PostMedia
import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.model.database.ProfileBond
import dev.forcetower.instrack.core.model.database.ProfileFriendship
import dev.forcetower.instrack.core.model.database.ProfileHistory
import dev.forcetower.instrack.core.model.database.Story
import dev.forcetower.instrack.core.model.database.StoryWatch
import dev.forcetower.instrack.core.model.database.SyncRegistry
import dev.forcetower.instrack.core.source.local.converters.DateConverters
import dev.forcetower.instrack.core.source.local.dao.ActionDao
import dev.forcetower.instrack.core.source.local.dao.BondDao
import dev.forcetower.instrack.core.source.local.dao.FriendshipDao
import dev.forcetower.instrack.core.source.local.dao.HistoryDao
import dev.forcetower.instrack.core.source.local.dao.LinkedProfileDao
import dev.forcetower.instrack.core.source.local.dao.PostCommentDao
import dev.forcetower.instrack.core.source.local.dao.PostDao
import dev.forcetower.instrack.core.source.local.dao.PostLikeDao
import dev.forcetower.instrack.core.source.local.dao.PostMediaDao
import dev.forcetower.instrack.core.source.local.dao.ProfileDao
import dev.forcetower.instrack.core.source.local.dao.StoryDao
import dev.forcetower.instrack.core.source.local.dao.StoryWatchDao
import dev.forcetower.instrack.core.source.local.dao.SyncRegistryDao

@Database(
    entities = [
        SyncRegistry::class,
        LinkedProfile::class,
        Profile::class,
        ProfileHistory::class,
        ProfileBond::class,
        ProfileFriendship::class,
        Action::class,
        Story::class,
        StoryWatch::class,
        Post::class,
        PostMedia::class,
        PostLike::class,
        PostComment::class
    ],
    version = 1
)
@TypeConverters(value = [DateConverters::class])
abstract class TrackDB : RoomDatabase() {
    abstract fun linked(): LinkedProfileDao
    abstract fun profile(): ProfileDao
    abstract fun history(): HistoryDao
    abstract fun bond(): BondDao
    abstract fun friendship(): FriendshipDao
    abstract fun action(): ActionDao
    abstract fun story(): StoryDao
    abstract fun storyWatch(): StoryWatchDao
    abstract fun post(): PostDao
    abstract fun postMedia(): PostMediaDao
    abstract fun like(): PostLikeDao
    abstract fun comment(): PostCommentDao
    abstract fun sync(): SyncRegistryDao
}
