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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.coffee.mycoffeeassistant.R
import com.coffee.mycoffeeassistant.ui.navigation.Screen

@Composable
fun CoffeeCardVertical(
    navController: NavController,
    modifier: Modifier,
    index: Int
) {
    Card(
        onClick = {
            navController.navigate(Screen.CoffeeDetails.route + "/$index")
        },
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

@Preview
@Composable
fun HomeCoffeeCardPreview() {
    CoffeeCardVertical(
        navController = rememberNavController(),
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight(),
        index = 0
    )
}
