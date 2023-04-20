@file:OptIn(ExperimentalMaterial3Api::class)

package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.coffee.mycoffeeassistant.R

@Composable
fun CoffeeCardHorizontal(
    modifier: Modifier,
    index: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.img_coffee_bag),
                contentDescription = "",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(1f / 1f)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp)
            ) {
                Text(
                    text = "Coffee num $index",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    "Brand name",
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun CoffeeCardHorizontalPreview() {
    CoffeeCardHorizontal(
        modifier = Modifier
            .width(300.dp)
            .height(100.dp),
        index = 0
    ) {

    }
}