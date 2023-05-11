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
import androidx.compose.ui.unit.sp
import com.coffee.mycoffeeassistant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeCardVertical(
    modifier: Modifier,
    index: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = modifier
    ) {
        // TODO: Add coffee status item in right top corner
        Image(
            painter = painterResource(id = R.drawable.img_coffee_bag),
            contentDescription = "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 5f)
                .clip(RoundedCornerShape(12.dp))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
        ) {
            Text(
                text = "Coffee $index",
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 20.sp,
            )
            Text(
                "Brand name",
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun CoffeeCardVerticalPreview() {
    CoffeeCardVertical(
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight(),
        index = 0
    ) {}
}
