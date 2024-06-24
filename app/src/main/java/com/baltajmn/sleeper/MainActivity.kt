package com.baltajmn.sleeper

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.AlarmClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.baltajmn.sleeper.common.LockScreenOrientation
import com.baltajmn.sleeper.domain.Hour
import com.baltajmn.sleeper.ui.composables.GetUpSelector
import com.baltajmn.sleeper.ui.composables.LoadingView
import com.baltajmn.sleeper.ui.composables.TimeInputField
import com.baltajmn.sleeper.ui.theme.LargeTitle
import com.baltajmn.sleeper.ui.theme.SleeperTheme
import com.baltajmn.sleeper.ui.theme.SmallTitle
import com.baltajmn.sleeper.ui.theme.SubBody
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

            SleeperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AnimatedSleeperContent()
                }
            }
        }
    }
}

@Composable
fun AnimatedSleeperContent(
    viewModel: MainViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = state.isLoading,
        label = "",
    ) {
        when (it) {
            true -> LoadingView()
            false -> MainContent(viewModel = viewModel, state = state)
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel, state: MainState) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Sleeper \uD83D\uDCA4",
            style = LargeTitle.copy(color = MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(32.dp))

        GetUpSelector(state, viewModel)

        Spacer(modifier = Modifier.height(24.dp))

        TimeInputField(
            label = LocalContext.current.getString(R.string.choose_hour),
            value = state.hourSelected
        ) { newValue ->
            viewModel.onHourSelected(newValue)
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.hoursList.isNotEmpty()) {
            TimeSentence(isGetUp = state.isGetUp, hour = state.hourSelected)
            HoursOptions(
                list = state.hoursList,
                onHourSelected = {
                    sendIntentToAlarm(
                        context = context,
                        hour = it.substring(0, 2).toInt(),
                        minute = it.substring(2).toInt()
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = LocalContext.current.getString(R.string.click_to_add_alarm),
            style = SubBody.copy(
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(64.dp))

        Text(
            text = "Made with ❤️ by @BaltaJmn",
            style = SubBody.copy(
                textAlign = TextAlign.Center,
                fontSize = 10.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun TimeSentence(isGetUp: Boolean, hour: String) {
    Text(
        text = if (isGetUp) {
            LocalContext.current.getString(R.string.if_i_get_up, hour.toHour())
        } else {
            LocalContext.current.getString(R.string.if_i_sleep, hour.toHour())
        },
        style = SubBody.copy(
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color.Black
        )
    )
}

@Composable
fun HoursOptions(list: List<Hour>, onHourSelected: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        items(list) { hour ->
            HourRecommendation(hour = hour, onHourSelected = onHourSelected)
        }
    }
}

@Composable
fun HourRecommendation(hour: Hour, onHourSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (hour.isRecommended) 2.dp else 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .background(color = MaterialTheme.colorScheme.background)
            .padding(8.dp)
            .clickable { onHourSelected.invoke(hour.hour) },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hour.hour.toHour(),
            style = SmallTitle.copy(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = LocalContext.current.getString(R.string.sleeping, hour.hourSleeping),
            style = SubBody.copy(
                color = Color.Black
            )
        )
        Text(
            text = LocalContext.current.getString(R.string.sleep_cycles, hour.cycles),
            style = SubBody.copy(
                color = Color.Black
            )
        )
        if (hour.isRecommended) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LocalContext.current.getString(R.string.recommended),
                style = SmallTitle.copy(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

private fun String.toHour(): String {
    return this.substring(0, 2) + ":" + this.substring(2)
}

fun sendIntentToAlarm(context: Context, hour: Int, minute: Int) {
    context.startActivity(
        Intent(AlarmClock.ACTION_SET_ALARM)
            .putExtra(
                AlarmClock.EXTRA_MESSAGE,
                "Sleeper alarm, good night!"
            )
            .putExtra(AlarmClock.EXTRA_HOUR, hour)
            .putExtra(AlarmClock.EXTRA_MINUTES, minute)
    )
}
