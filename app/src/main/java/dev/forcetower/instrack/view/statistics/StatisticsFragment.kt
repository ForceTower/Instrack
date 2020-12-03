package dev.forcetower.instrack.view.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.instrack.R
import dev.forcetower.instrack.core.extensions.LOCAL_DATE_TIME_EPOCH
import dev.forcetower.instrack.databinding.FragmentUsefulStatsBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.extensions.resolveColorAttr
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class StatisticsFragment : BaseFragment() {
    private val viewModel by viewModels<StatisticsViewModel>()
    private lateinit var binding: FragmentUsefulStatsBinding
    private lateinit var compromisedAdapter: CompromisedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = FragmentUsefulStatsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        compromisedAdapter = CompromisedAdapter()
        binding.includeMostEngaged.recyclerCompromised.apply {
            adapter = compromisedAdapter
            itemAnimator?.changeDuration = 0L
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mostCompromised().observe(viewLifecycleOwner) {
            compromisedAdapter.submitList(it)
        }

        val accentColor = requireContext().resolveColorAttr(R.attr.colorAccent)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_chart_distribution)

        val formatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val converted = value.toLong()
                if (converted < 0)
                    return ""

                return LOCAL_DATE_TIME_EPOCH.plusDays(converted)
                    .format(DateTimeFormatter.ofPattern("E"))
            }
        }

        viewModel.postDistribution().observe(
            viewLifecycleOwner,
            { dataset ->
                dataset.configureStatsDefault(accentColor, drawable)
                val lineData = LineData(dataset)

                binding.includePostDistribution.chart.apply {
                    data = lineData
                    configureStatsDefault()
                    xAxis.apply {
                        valueFormatter = formatter
                    }
                    invalidate()
                }
            }
        )

        viewModel.likeDistribution().observe(
            viewLifecycleOwner,
            { dataset ->
                dataset.configureStatsDefault(accentColor, drawable)
                val lineData = LineData(dataset)

                binding.includeLikeDistribution.chart.apply {
                    data = lineData
                    configureStatsDefault()
                    xAxis.apply {
                        valueFormatter = formatter
                    }
                    invalidate()
                }
            }
        )

        viewModel.commentDistribution().observe(
            viewLifecycleOwner,
            { dataset ->
                dataset.configureStatsDefault(accentColor, drawable)
                val lineData = LineData(dataset)

                binding.includeCommentDistribution.chart.apply {
                    data = lineData
                    configureStatsDefault()
                    xAxis.apply {
                        valueFormatter = formatter
                    }
                    invalidate()
                }
            }
        )
    }
}
