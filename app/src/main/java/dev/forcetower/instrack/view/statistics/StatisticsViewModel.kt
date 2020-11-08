package dev.forcetower.instrack.view.statistics

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dev.forcetower.instrack.core.model.ui.UserFriendship
import dev.forcetower.instrack.core.source.repository.StatisticsRepository

class StatisticsViewModel @ViewModelInject constructor(
    private val repository: StatisticsRepository
): ViewModel() {
    fun mostCompromised(): LiveData<List<UserFriendship>> {
        return repository.getMostCompromised().asLiveData()
    }

    fun postDistribution(): LiveData<LineDataSet> {
        return repository.getPostDistribution().asLiveData()
    }

    fun likeDistribution(): LiveData<LineDataSet> {
        return repository.getLikeDistribution().asLiveData()
    }

    fun commentDistribution(): LiveData<LineDataSet> {
        return repository.getCommentDistribution().asLiveData()
    }

}