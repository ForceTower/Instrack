package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.forcetower.instrack.core.model.database.PostMedia
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostMediaDao : BaseDao<PostMedia>() {
    //(SELECT userPk FROM LinkedProfile WHERE selected = 1 LIMIT 1)
    @Query("SELECT * FROM PostMedia WHERE postPk = (SELECT pk FROM Post WHERE userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) ORDER BY takenAt DESC LIMIT 1) AND previewPicture IS NOT NULL LIMIT 1")
    abstract fun getLatestMedia(): Flow<PostMedia?>

    @Query("SELECT COUNT(M.pk) FROM PostMedia M INNER JOIN Post P on M.postPk = P.pk WHERE P.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) AND P.mediaType = 2")
    abstract fun getVideosCount(): Flow<Int>

    @Query("SELECT COUNT(M.pk) FROM PostMedia M INNER JOIN Post P on M.postPk = P.pk WHERE P.userPk = (SELECT L.userPK FROM LinkedProfile AS L WHERE L.selected = 1 LIMIT 1) AND P.mediaType = 1")
    abstract fun getImageCount(): Flow<Int>
}