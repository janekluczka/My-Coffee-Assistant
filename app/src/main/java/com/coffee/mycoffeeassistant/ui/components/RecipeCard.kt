@file:Suppress("SpellCheckingInspection")

package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

/* YouTube Video Thumbnail URLs
   1280×720 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/maxresdefault.jpg
   640×480 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/sddefault.jpg
   480×360 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/hqdefault.jpg
   320×180 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/mqdefault.jpg
   120×90 https://i.ytimg.com/vi/[YOUTUBE VIDEO ID]/default.jpg */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(
    title: String,
    author: String,
    videoId: String,
    onClick: () -> Unit
) {
    Card(onClick = onClick) {
        AsyncImage(
            model = "https://i.ytimg.com/vi/${videoId}/sddefault.jpg",
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = author,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun RecipeCardPreview() {
    RecipeCard(
        title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
        author = "Rick Astley",
        videoId = "dQw4w9WgXcQ"
    ) {

    }
}