package com.example.starwarsgarage.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.starwarsgarage.R
import com.example.starwarsgarage.ui.theme.starJediFontFamily

@Composable
fun StarshipShowstopperErrorView(
    errorMessage: String?,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ) {

        Text(
            text = stringResource(id = R.string.showstopper_error),
            fontSize = 20.sp,
            fontFamily = starJediFontFamily,
            color = Color.White,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 1f),
                    offset = Offset(4f, 4f),
                    blurRadius = 1f
                )
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = TextStyle(
                    shadow = Shadow(
                        color =  Color.Black.copy(alpha = 0.8f),
                        offset = Offset(2f, 2f),
                        blurRadius = 1f
                    )
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.retry_button_text))
        }
        TextButton(onClick = onDismiss) {
            Text(
                text = stringResource(id = R.string.dismiss_button_text),
                color = Color.White
            )
        }
    }
}
