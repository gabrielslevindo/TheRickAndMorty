package com.example.therickandmorty.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun statusFilter(
    statuses: List<String>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit,
) {
    Column {
        Text(
            text = "Character Status",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp, top = 5.dp),
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                statuses.forEach { status ->
                    Row(
                        modifier =
                            Modifier
                                .selectable(
                                    selected = selectedStatus == status,
                                    onClick = {
                                        if (selectedStatus == status) {
                                            onStatusSelected("")
                                        } else {
                                            onStatusSelected(status)
                                        }
                                    },
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = selectedStatus == status,
                            onClick = {
                                if (selectedStatus == status) {
                                    onStatusSelected("")
                                } else {
                                    onStatusSelected(status)
                                }
                            },
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = status,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun statusFilterPreview() {
    MaterialTheme {
        val statuses =
            listOf(
                "Alive",
                "Dead",
                "Unknown",
            )

        statusFilter(
            statuses = statuses,
            selectedStatus = "Alive",
            onStatusSelected = {},
        )
    }
}
