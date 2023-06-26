package com.yopachara.fourtosixmethod.feature.history

import com.patrykandpatrick.vico.core.entry.ChartEntry

class Entry(
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = Entry(x, y)
}
