package com.yopachara.fourtosixmethod.feature.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.dimensions.MutableDimensions
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import com.yopachara.fourtosixmethod.core.designsystem.theme.Typography
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
internal fun HistoryRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val historyState = viewModel.historyStateFlow.collectAsState()

    HistoryScreen(historyState)
}

class Entry(
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = Entry(x, y)
}

data class FloatEntry(
    override val x: Float,
    override val y: Float,
) : ChartEntry {

    override fun withY(y: Float): ChartEntry = FloatEntry(
        x = x,
        y = y,
    )
}

private val bottomAxisValueFormatter =
    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
        LocalDate.ofYearDay(
            2023,
            x.toInt()
        ).format(DateTimeFormatter.ofPattern("dd/MM"))
    }

@Composable
fun HistoryScreen(historyState: State<List<Recipe>>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        val frequencies = historyState.value.groupingBy { recipe ->
            val localDate = recipe.createAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            localDate.dayOfYear
        }.eachCount()

        val value = frequencies.map { recipe ->
            Entry(
                recipe.key.toFloat(),
                recipe.value.toFloat()
            )
        }

        val chartEntryModelProducer1 = ChartEntryModelProducer(value)
        val composedChartEntryModelProducer = chartEntryModelProducer1
        val columnChart = columnChart()
        val lineChart = lineChart()
        val defaultColumns = currentChartStyle.columnChart.columns
        val COLUMN_CORNER_CUT_SIZE_PERCENT = 50

        Chart(
            chart = columnChart(
                columns = remember(defaultColumns) {
                    defaultColumns.map { defaultColumn ->
                        LineComponent(
                            defaultColumn.color,
                            6f,
                        )
                    }
                },
                spacing = 48.dp
            ),

            chartModelProducer = composedChartEntryModelProducer,
            startAxis = startAxis(
                maxLabelCount = 24,
                labelRotationDegrees = 45f,
            ),
            bottomAxis = bottomAxis(
                titleComponent = textComponent(
                    color = Color.Black,
                    textSize = 12.sp,
                ),
                valueFormatter = bottomAxisValueFormatter, labelRotationDegrees = 45f,
            ),
        )

        LazyColumn(modifier = Modifier.padding(top = 24.dp, start = 8.dp, end = 8.dp)) {
            itemsIndexed(historyState.value) { index, recipe ->
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),

                    ) {

                    Text(
                        text = """${recipe.coffeeWeight} g""",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """1:${recipe.ratio}""",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """${recipe.balance}""",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                    Text(
                        text = """${recipe.level}""",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )

                    val firstApiFormat = SimpleDateFormat("MM/dd HH:mm", Locale.getDefault())
                    firstApiFormat.format(recipe.createAt)
                    Text(

                        text = """${firstApiFormat.format(recipe.createAt)}""",
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        )
                    )
                }
            }
        }
    }
}