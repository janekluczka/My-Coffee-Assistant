package com.coffee.mycoffeeassistant.ui.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coffee.mycoffeeassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeCardHorizontal(
    name: String,
    brand: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = brand,
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Image(
                painter = painterResource(id = R.drawable.img_coffee_bag),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .padding(top = 4.dp, end = 4.dp, bottom = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxHeight()
            )
        }
    }
}

@Suppress("SpellCheckingInspection")
@Preview
@Composable
fun CoffeeCardHorizontalPreview() {
    CoffeeCardHorizontal(
        name = "brazylia de ferro",
        brand = "monko.",
        modifier = Modifier.width(250.dp)
    ) {

    }
}