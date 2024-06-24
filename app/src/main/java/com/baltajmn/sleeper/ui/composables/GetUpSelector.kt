package com.baltajmn.sleeper.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baltajmn.sleeper.MainState
import com.baltajmn.sleeper.MainViewModel
import com.baltajmn.sleeper.R
import com.baltajmn.sleeper.ui.theme.SmallTitle

@Composable
fun GetUpSelector(
    state: MainState,
    viewModel: MainViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        GetUpOption(
            positionStart = true,
            isGetUp = state.isGetUp
        ) { viewModel.onGetUpClicked(true) }
        GetUpOption(
            positionStart = false,
            isGetUp = state.isGetUp.not()
        ) { viewModel.onGetUpClicked(false) }
    }
}


context (RowScope)
@Composable
private fun GetUpOption(positionStart: Boolean, isGetUp: Boolean, onOptionClicked: () -> Unit) {

    val cornerShape = if (positionStart) {
        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
    } else {
        RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
    }

    Column(
        modifier = Modifier
            .weight(1f)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = cornerShape
            )
            .clip(cornerShape)
            .background(color = if (isGetUp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
            .padding(4.dp)
            .clickable { onOptionClicked.invoke() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (positionStart) {
                LocalContext.current.getString(R.string.i_want_to_get_up)
            } else {
                LocalContext.current.getString(R.string.i_want_to_sleep)
            },
            color = if (isGetUp) Color.White else Color.Black,
            style = SmallTitle.copy(
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}