package com.islamicknowledge.platform.feature.ramadan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
private val items=listOf("রোজার নিয়ত ও সিয়াম","সেহরি ও ইফতার","তারাবীহ","কুরআন ও দোয়া","ইতিকাফ","শেষ দশক ও লাইলাতুল কদর","যাকাত ও সদকা")
@Composable fun RamadanScreen(modifier:Modifier=Modifier){Column(modifier.fillMaxSize()){SectionHeader(title="রমজান",modifier=Modifier.padding(16.dp));Text("রমজানের তারিখ স্থানীয় চাঁদ দেখা ও কর্তৃপক্ষের ঘোষণার ওপর নির্ভরশীল।",modifier=Modifier.padding(horizontal=16.dp),style=MaterialTheme.typography.bodySmall);LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){items(items){x->Card(Modifier.fillMaxWidth()){Text(x,Modifier.padding(18.dp),style=MaterialTheme.typography.titleMedium)}}}}}