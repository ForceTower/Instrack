package dev.forcetower.instrack.core.source.repository

import dev.forcetower.instrack.core.model.database.Profile
import dev.forcetower.instrack.core.model.ui.StoryViewCount
import dev.forcetower.instrack.core.model.ui.StoryWatchProfileSimple
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryRepository @Inject constructor(
    private val database: TrackDB
) {
    fun all(): Flow<List<StoryViewCount>> {
        return database.story().getStoriesWithViewerCount()
    }

    fun active(): Flow<List<StoryViewCount>> {
        return database.story().getActiveStoriesWithViewerCount()
    }

    fun inactive(): Flow<List<StoryViewCount>> {
        return database.story().getInactiveStoriesWithViewerCount()
    }

    fun watchers(pk: Long): Flow<List<StoryWatchProfileSimple>> {
        return database.storyWatch().getWatchersOf(pk)
    }

    fun watchersFollower(pk: Long): Flow<List<StoryWatchProfileSimple>> {
        return database.storyWatch().getFollowerWatcherOf(pk)
    }

    fun watchersNotFollower(pk: Long): Flow<List<StoryWatchProfileSimple>> {
        return database.storyWatch().getNotFollowerWatcherOf(pk)
    }

    fun watchersNotFollow(pk: Long): Flow<List<StoryWatchProfileSimple>> {
        return database.storyWatch().getNotFollowWatcherOf(pk)
    }
}