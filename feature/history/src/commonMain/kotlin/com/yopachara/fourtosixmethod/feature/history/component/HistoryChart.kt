package com.yopachara.fourtosixmethod.feature.history.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.yopachara.fourtosixmethod.core.data.model.Recipe
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

private val chartDateFormat = LocalDate.Format {
    dayOfMonth()
    char('/')
    monthNumber()
    char('/')
    yearTwoDigits(baseYear = 2000)
}

@Composable
fun HistoryChart(recipeList: List<Recipe>) {
    // x = epoch day of the brew, y = number of brews recorded that day
    val points = remember(recipeList) {
        recipeList.groupingBy { it.createAt.toEpochDays() }
            .eachCount()
            .entries
            .sortedBy { it.key }
    }
    val xValues = remember(points) { points.map { it.key } }
    val yValues = remember(points) { points.map { it.value } }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(xValues, yValues) {
        if (xValues.isNotEmpty()) {
            modelProducer.runTransaction {
                columnSeries { series(xValues, yValues) }
            }
        }
    }

    val dateFormatter = CartesianValueFormatter { _, value, _ ->
        chartDateFormat.format(LocalDate.fromEpochDays(value.toInt()))
    }

    if (xValues.isNotEmpty()) {
        CartesianChartHost(
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(valueFormatter = dateFormatter),
            ),
            modelProducer,
            modifier = Modifier.fillMaxWidth().height(220.dp),
        )
    }
}
