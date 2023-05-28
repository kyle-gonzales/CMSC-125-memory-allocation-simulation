package com.example.memoryallocation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuScreen(
    
) {
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(modifier = Modifier.padding(12.dp), onClick = { /*TODO*/ }) {
            Text("First Fit")
        }
        Button(modifier = Modifier.padding(12.dp), onClick = { /*TODO*/ }) {
            Text("Best Fit")
        }
        Button(modifier = Modifier.padding(12.dp), onClick = { /*TODO*/ }) {
            Text("Worst Fit")
        }
    }
}

@Preview
@Composable
fun PreviewMenuScreen() {
    MenuScreen()
}