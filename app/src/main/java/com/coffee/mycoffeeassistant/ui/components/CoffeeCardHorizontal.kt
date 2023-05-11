package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffee.mycoffeeassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeCardHorizontal(
    modifier: Modifier,
    index: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier.height(IntrinsicSize.Min).fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.img_coffee_bag),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Coffee num $index",
                    maxLines = 1,
                    fontSize = 20.sp,
                )
                Text(
                    "Brand name",
                    maxLines = 1,
                    fontSize = 16.sp
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
            .width(300.dp),
        index = 0
    ) {

    }
}