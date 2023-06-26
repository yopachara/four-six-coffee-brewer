package com.yopachara.fourtosixmethod.feature.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun HistoryChart(recipeList: List<Recipe>) {

    val frequencies = recipeList.groupingBy { recipe ->
        val localDate = recipe.createAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        localDate.dayOfYear
    }.eachCount()

    val value = frequencies.map { recipe ->
        Entry(
            recipe.key.toFloat(),
            recipe.value.toFloat()
        )
    }

    val chartEntryModelProducer = ChartEntryModelProducer(value)
    val defaultColumns = currentChartStyle.columnChart.columns

    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
        if (x > 0) {
            val localDate = LocalDate.ofYearDay(
                LocalDate.now().year,
                x.toInt()
            )
            if (localDate != null) {
                localDate.format(DateTimeFormatter.ofPattern("dd/MM"))
            } else {
                ""
            }
        } else ""
    }

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

        chartModelProducer = chartEntryModelProducer,
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
}

