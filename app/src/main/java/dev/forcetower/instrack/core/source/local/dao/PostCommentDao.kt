package dev.forcetower.instrack.core.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import dev.forcetower.instrack.core.model.database.PostComment
import dev.forcetower.instrack.core.model.ui.PostCommenterSimple
import dev.forcetower.toolkit.database.dao.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
abstract class PostCommentDao : BaseDao<PostComment>() {
    @Query("SELECT * FROM PostComment WHERE pk = :pk")
    protected abstract suspend fun getCommentByIDDirect(pk: Long): PostComment?

    override suspend fun getValueByIDDirect(value: PostComment): PostComment? {
        return getCommentByIDDirect(value.pk)
    }

    @Query("SELECT COUNT(PC.pk) FROM PostComment AS PC INNER JOIN Post AS P ON PC.postPk = P.pk WHERE P.userPk = (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getCommentCount(): Flow<Int>

    @Query("SELECT PC.*, PR.picture FROM PostComment AS PC INNER JOIN Post AS P ON PC.postPk = P.pk INNER JOIN Profile PR on PC.userPk = PR.pk WHERE P.userPk = (SELECT userPK FROM LinkedProfile WHERE selected = 1 LIMIT 1)")
    abstract fun getCommenters(): Flow<List<PostCommenterSimple>>
}