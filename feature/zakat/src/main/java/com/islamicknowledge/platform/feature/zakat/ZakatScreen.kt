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
import java.util.Locale

private const val RATE = 0.025
private const val SILVER_NISAB_GRAMS = 612.36

@Composable
fun ZakatScreen(modifier: Modifier = Modifier) {
    var assets by remember { mutableStateOf("") }
    var debts by remember { mutableStateOf("") }
    var silverPrice by remember { mutableStateOf("") }

    val grossAssets = assets.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val debtsValue = debts.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val net = (grossAssets - debtsValue).coerceAtLeast(0.0)
    val silverPriceValue = silverPrice.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val silverNisab = silverPriceValue * SILVER_NISAB_GRAMS
    val nisabKnown = silverPriceValue > 0.0
    val nisabReached = nisabKnown && net >= silverNisab
    val zakat = if (nisabReached) net * RATE else 0.0

    Column(modifier.fillMaxSize()) {
        SectionHeader(title = "যাকাত হিসাব", modifier = Modifier.padding(16.dp))
        Text(
            text = "এটি শিক্ষামূলক আনুমানিক হিসাব। সম্পদের ধরন, নিসাব, ঋণ এবং এক হাওল পূর্ণ হওয়ার নিয়ম মাজহাব/ফিকহ অনুযায়ী যাচাই করুন।",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodySmall,
        )
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedTextField(value = assets, onValueChange = { assets = it }, label = { Text("যাকাতযোগ্য মোট সম্পদ (৳)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = debts, onValueChange = { debts = it }, label = { Text("বাদযোগ্য দায়/ঋণ (৳)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = silverPrice, onValueChange = { silverPrice = it }, label = { Text("রূপার প্রতি গ্রাম মূল্য (৳)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), modifier = Modifier.fillMaxWidth())
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("নিট যাকাতযোগ্য সম্পদ: ৳" + formatMoney(net), style = MaterialTheme.typography.titleMedium)
                    if (!nisabKnown) {
                        Text("রূপার প্রতি গ্রাম মূল্য দিলে নিসাবের আনুমানিক সীমা যাচাই করা যাবে।")
                    } else {
                        Text("রূপার ৬১২.৩৬ গ্রাম নিসাবের আনুমানিক মূল্য: ৳" + formatMoney(silverNisab))
                        Text(if (nisabReached) "দেওয়া তথ্য অনুযায়ী নিসাব পূর্ণ হয়েছে।" else "দেওয়া তথ্য অনুযায়ী রূপার নিসাব পূর্ণ হয়নি।")
                    }
                    if (nisabReached) Text("২.৫% হারে আনুমানিক যাকাত: ৳" + formatMoney(zakat))
                    else Text("নিসাব পূর্ণ হয়েছে নিশ্চিত না হওয়া পর্যন্ত যাকাতের অঙ্ক দেখানো হচ্ছে না।")
                }
            }
        }
        Text(
            text = "নোট: রূপার নিসাব একটি সাধারণ হিসাবের পদ্ধতি; স্থানীয় ফিকহি মত, সম্পদের ধরন এবং দায়-ঋণের প্রযোজ্যতা অনুযায়ী চূড়ান্ত হিসাব পরিবর্তিত হতে পারে।",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

private fun formatMoney(value: Double): String = String.format(Locale.US, "%.2f", value)