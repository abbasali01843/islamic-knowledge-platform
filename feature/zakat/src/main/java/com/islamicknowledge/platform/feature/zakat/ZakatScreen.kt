package com.islamicknowledge.platform.feature.zakat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

private const val RATE = 0.025
private const val SILVER_NISAB_GRAMS = 612.36

@Composable
fun ZakatScreen(modifier: Modifier = Modifier) {
    var assets by remember { mutableStateOf("") }
    var debts by remember { mutableStateOf("") }
    var silverPrice by remember { mutableStateOf("") }

    val net = (assets.toDoubleOrNull() ?: 0.0) - (debts.toDoubleOrNull() ?: 0.0)
    val silverNisab = (silverPrice.toDoubleOrNull() ?: 0.0) * SILVER_NISAB_GRAMS
    val zakat = if (net > 0) net * RATE else 0.0

    Column(modifier.fillMaxSize()) {
        SectionHeader(
            title = "যাকাত হিসাব",
            modifier = Modifier.padding(16.dp)
        )

        Text(
            text = "নিসাব ও প্রযোজ্য সম্পদের ধরন মাজহাব/আলেমের ফিকহি নির্দেশনা অনুযায়ী যাচাই করুন।",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall
        )

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = assets,
                onValueChange = { assets = it },
                label = { Text("যাকাতযোগ্য মোট সম্পদ (৳)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = debts,
                onValueChange = { debts = it },
                label = { Text("বাদযোগ্য দায়/ঋণ (৳)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = silverPrice,
                onValueChange = { silverPrice = it },
                label = { Text("রূপার প্রতি গ্রাম মূল্য (ঐচ্ছিক)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "নিট সম্পদ: ৳${"%.2f".format(net)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "২.৫% হারে সম্ভাব্য যাকাত: ৳${"%.2f".format(zakat)}")
                    if (silverNisab > 0) {
                        Text(
                            text = "রূপার ৬১২.৩৬ গ্রাম নিসাবের আনুমানিক মূল্য: ৳${"%.2f".format(silverNisab)}"
                        )
                    }
                }
            }
        }

        Text(
            text = "সাধারণ হিসাবের সূত্র: যোগ্য নিট সম্পদ × ২.৫%। চূড়ান্ত ফিকহি হিসাবের জন্য বিশ্বস্ত আলেমের পরামর্শ নিন।",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}
