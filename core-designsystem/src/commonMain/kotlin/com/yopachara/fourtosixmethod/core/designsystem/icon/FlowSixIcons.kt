package com.yopachara.fourtosixmethod.core.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings as OutlinedSettings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material.icons.rounded.Tag
import androidx.compose.material.icons.rounded.ViewDay
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.Res
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_about_24
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_about_filled_24
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_coffee_24
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_coffee_filled_24
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_git_repo
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_github
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_timer_24
import com.yopachara.fourtosixmethod.core.designsystem.generated.resources.ic_timer_filled_24
import org.jetbrains.compose.resources.DrawableResource

object FlowSixIcons {
    val AccountCircle = Icons.Outlined.AccountCircle
    val Add = Icons.Rounded.Add
    val ArrowBack = Icons.Rounded.ArrowBack
    val ArrowDropDown = Icons.Default.ArrowDropDown
    val ArrowDropUp = Icons.Default.ArrowDropUp
    val Check = Icons.Rounded.Check
    val Close = Icons.Rounded.Close
    val ExpandLess = Icons.Rounded.ExpandLess
    val Fullscreen = Icons.Rounded.Fullscreen
    val Grid3x3 = Icons.Rounded.Grid3x3
    val MoreVert = Icons.Default.MoreVert
    val Person = Icons.Rounded.Person
    val PlayArrow = Icons.Rounded.PlayArrow
    val Search = Icons.Rounded.Search
    val Settings = Icons.Rounded.Settings
    val SettingsOutlined = Icons.Outlined.OutlinedSettings
    val ShortText = Icons.Rounded.ShortText
    val Tag = Icons.Rounded.Tag
    val ViewDay = Icons.Rounded.ViewDay
    val VolumeOff = Icons.Rounded.VolumeOff
    val VolumeUp = Icons.Rounded.VolumeUp

    val TimerUnfilled = Res.drawable.ic_timer_24
    val TimerFilled = Res.drawable.ic_timer_filled_24
    val HistoryUnfilled = Res.drawable.ic_coffee_24
    val HistoryFilled = Res.drawable.ic_coffee_filled_24
    val AboutUnfilled = Res.drawable.ic_about_24
    val AboutFilled = Res.drawable.ic_about_filled_24
    val GithubIcon = Res.drawable.ic_github
    val RepoIcon = Res.drawable.ic_git_repo
}


/**
 * A sealed class to make dealing with [ImageVector] and [DrawableResource] icons easier.
 */
sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(val id: DrawableResource) : Icon()
}
