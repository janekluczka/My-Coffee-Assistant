package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BrewingStepCard(number: String, description: String, time: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp, start = 4.dp, bottom = 4.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    fontSize = 16.sp,
                )
            }
            Text(
                text = description,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .fillMaxWidth()
                    .weight(1f),
                fontSize = 16.sp
            )
            Text(
                text = time,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp, end = 16.dp),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun BrewingStepCardPreview() {
    BrewingStepCard(number = "1", description = "Grind coffee beans", time = "2,5 min")
}