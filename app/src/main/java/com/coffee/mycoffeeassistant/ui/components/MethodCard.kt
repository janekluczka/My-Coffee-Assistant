package com.coffee.mycoffeeassistant.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.storage.FirebaseStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodCard(
    name: String,
    imageRef: String,
    onClick: () -> Unit
) {
    var imageUri by remember { mutableStateOf("") }

    if (imageUri.isBlank()) {
        val firebaseStorage = FirebaseStorage.getInstance(Firebase.app)
        firebaseStorage.reference.child(imageRef).downloadUrl.addOnSuccessListener { uri ->
            imageUri = uri.toString()
        }
    }

    Card(onClick = onClick) {
        Crossfade(targetState = imageUri, animationSpec = tween(1500)) { uri ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(21f / 9f)
                    .padding(top = 4.dp, start = 4.dp, end = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
            ) {
                if (uri.isNotBlank()) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Text(
                text = name,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun MethodCardPreview() {
    MethodCard(
        name = "Aeropress",
        imageRef = "",
    ) {

    }
}