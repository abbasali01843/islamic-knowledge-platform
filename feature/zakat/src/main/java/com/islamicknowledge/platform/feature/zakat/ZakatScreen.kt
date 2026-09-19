package com.islamicknowledge.platform.feature.zakat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
private const val RATE=0.025
private const val SILVER_NISAB_GRAMS=612.36
@Composable fun ZakatScreen(modifier:Modifier=Modifier){
var assets by remember{mutableStateOf("")}
var debts by remember{mutableStateOf("")}
var silverPrice by remember{mutableStateOf("")}
val net=(assets.toDoubleOrNull()?:0.0)-(debts.toDoubleOrNull()?:0.0)
val silverNisab=(silverPrice.toDoubleOrNull()?:0.0)*SILVER_NISAB_GRAMS
val zakat=if(net>0) net*RATE else 0.0
Column(modifier.fillMaxSize()){
SectionHeader(title="যাকাত হিসাব",modifier=Modifier.padding(16.dp))
Text("নিসাব ও প্রযোজ্য সম্পদের ধরন মাজহাব/আলেমের ফিকহি নির্দেশনা অনুযায়ী যাচাই করুন।",modifier=Modifier.padding(horizontal=16.dp),style=MaterialTheme.typography.bodySmall)
Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
OutlinedTextField(assets,{assets=it},label={Text("যাকাতযোগ্য মোট সম্পদ (৳)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),modifier=Modifier.fillMaxWidth())
OutlinedTextField(debts,{debts=it},label={Text("বাদযোগ্য দায়/ঋণ (৳)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),modifier=Modifier.fillMaxWidth())
OutlinedTextField(silverPrice,{silverPrice=it},label={Text("রূপার প্রতি গ্রাম মূল্য (ঐচ্ছিক)")},keyboardOptions=KeyboardOptions(keyboardType=KeyboardType.Decimal),modifier=Modifier.fillMaxWidth())
Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text("নিট সম্পদ: ৳"+"%.2f".format(net),style=MaterialTheme.typography.titleMedium);Text("২.৫% হারে সম্ভাব্য যাকাত: ৳"+"%.2f".format(zakat));if(silverNisab>0)Text("রূপার ৬১২.৩৬ গ্রাম নিসাবের আনুমানিক মূল্য: ৳"+"%.2f".format(silverNisab))}}
Text("সাধারণ হিসাবের সূত্র: যোগ্য নিট সম্পদ × ২.৫%। চূড়ান্ত ফিকহি হিসাবের জন্য বিশ্বস্ত আলেমের পরামর্শ নিন।",modifier=Modifier.padding(16.dp),style=MaterialTheme.typography.bodySmall)
}}