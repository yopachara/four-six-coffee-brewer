package com.yopachara.flowsixmethod.feature.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yopachara.fourtosixmethod.core.designsystem.icon.FlowSixIcons


@Composable
internal fun AboutRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AboutContent()
}

fun navigateToUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
    }
    context.startActivity(intent)

}

@Composable
fun AboutContent() {
    val context = LocalContext.current
    val githubProfileUrl = "https://github.com/yopachara/"
    val githubRepoUrl = "https://github.com/yopachara/four-six-coffee-brewer/"

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            Icon(
                painter = painterResource(id = FlowSixIcons.GithubIcon),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navigateToUrl(context, githubProfileUrl)
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "yopachara.", modifier = Modifier.clickable {
                navigateToUrl(context, githubProfileUrl)
            }, style = TextStyle(textDecoration = TextDecoration.Underline))

        }

        Spacer(modifier = Modifier.size(8.dp))

        Row {
            Icon(
                painter = painterResource(id = FlowSixIcons.RepoIcon),
                contentDescription = null,
                modifier = Modifier.clickable {
                    navigateToUrl(context, githubRepoUrl)
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "yopachara/four-six-coffee-brewer", modifier = Modifier.clickable {
                navigateToUrl(context, githubRepoUrl)
            }, style = TextStyle(textDecoration = TextDecoration.Underline))

        }
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "Hi, my name is Pachara S.")
        Spacer(modifier = Modifier.size(8.dp))

        Text(text = "An Android developer who loves coffee")
    }
}

@Preview
@Composable
fun AboutPreview() {
    AboutContent()
}