package com.baltajmn.sleeper.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.baltajmn.sleeper.common.isNumericOrBlank

@Composable
fun TimeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    val visualTransformation = remember { TimeVisualTransformation() }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp)),
        value = TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        ),
        onValueChange = { newValue ->
            if (newValue.text.trim().isNumericOrBlank()) {
                if (newValue.text.trim().isNotEmpty()) {
                    if (newValue.text.length == 1 && newValue.text.toInt() >= 3) {
                        onValueChange("0${newValue.text}")
                    } else {
                        if (newValue.text.length == 3 && newValue.text.last().toString()
                                .toInt() > 5
                        ) {
                            onValueChange("${newValue.text.dropLast(1)}5")
                        } else {
                            onValueChange(newValue.text)
                        }
                    }
                } else {
                    onValueChange(newValue.text)
                }
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {}),
        visualTransformation = visualTransformation
    )
}


class TimeVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) {
            text.text.substring(0, 4)
        } else {
            text.text
        }

        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 1) append(':')
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}