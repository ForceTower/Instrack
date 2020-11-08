package dev.forcetower.instrack.core.source.repository

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dev.forcetower.instrack.core.extensions.asLocalDateTimeMs
import dev.forcetower.instrack.core.extensions.asLocalDateTimeSec
import dev.forcetower.instrack.core.extensions.daysSinceEpoch
import dev.forcetower.instrack.core.extensions.map
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.local.TrackDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepository @Inject constructor(
    private val database: TrackDB
) {
    fun getMostCompromised(): Flow<List<UserFriendship>> {
        return database.action().getMostCompromised()
    }

    fun getPostDistribution(): Flow<LineDataSet> {
        val now = LocalDateTime.now()
        val before = now.minusWeeks(1).plusDays(1)
        val min = before.toEpochSecond(ZoneOffset.UTC)

        val range = before.daysSinceEpoch..now.daysSinceEpoch

        return database.post().getAllPosts(min).map { posts ->
            val processed = posts.groupBy { it.takenAt.daysSinceEpoch }
                .mapValues { it.value.size.toFloat() }

            val entries = range.map { date ->
                Entry(date.toFloat(), processed[date] ?: 0.0f)
            }

            LineDataSet(entries, "")
        }
    }

    fun getLikeDistribution(): Flow<LineDataSet> {
        val now = LocalDateTime.now()
        val before = now.minusWeeks(1).plusDays(1)
        val min = before.toEpochSecond(ZoneOffset.UTC)

        val range = before.daysSinceEpoch..now.daysSinceEpoch

        return database.like().getAllLikes(min).map { posts ->
            val processed = posts.groupBy { it.timestamp.asLocalDateTimeMs().daysSinceEpoch }
                .mapValues { it.value.size.toFloat() }

            val entries = range.map { date ->
                Entry(date.toFloat(), processed[date] ?: 0.0f)
            }

            LineDataSet(entries, "")
        }
    }

    fun getCommentDistribution(): Flow<LineDataSet> {
        val now = LocalDateTime.now()
        val before = now.minusWeeks(1).plusDays(1)
        val min = before.toEpochSecond(ZoneOffset.UTC)

        val range = before.daysSinceEpoch..now.daysSinceEpoch

        return database.comment().getAllComments(min).map { posts ->
            val processed = posts.groupBy { it.createdAt.asLocalDateTimeSec().daysSinceEpoch }
                .mapValues { it.value.size.toFloat() }

            Timber.d("Processed $processed")

            val entries = range.map { date ->
                Entry(date.toFloat(), processed[date] ?: 0.0f)
            }

            Timber.d("Entries $entries")
            LineDataSet(entries, "")
        }
    }
}