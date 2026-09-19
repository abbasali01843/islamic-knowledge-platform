package com.islamicknowledge.platform.feature.calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
data class IslamicEventInfo(val title:String,val hijri:String,val description:String,val amal:String)
private val events=listOf(
IslamicEventInfo("আশুরা","১০ মুহররম","মুহররম মাসের ১০ তারিখ।","৯ ও ১০ অথবা ১০ ও ১১ মুহররম রোজা রাখা।"),
IslamicEventInfo("শবে মেরাজ","২৭ রজব","ইসরা ও মেরাজের স্মরণীয় রাত।","নামাজ ও নফল ইবাদতে মনোযোগ দেওয়া।"),
IslamicEventInfo("শবে বরাত","১৫ শাবান","অর্ধ-শাবানের রাত।","দোয়া, ইস্তিগফার ও নফল ইবাদত।"),
IslamicEventInfo("রমজান","১ রমজান","সিয়াম, কুরআন ও তাকওয়ার মাস।","রোজা, তারাবীহ, কুরআন ও সদকা।"),
IslamicEventInfo("লাইলাতুল কদর","শেষ দশকের বিজোড় রাত","কদরের রাত হাজার মাসের চেয়েও উত্তম।","তাহাজ্জুদ ও বেশি বেশি দোয়া।"),
IslamicEventInfo("ঈদুল ফিতর","১ শাওয়াল","রমজানের পর আনন্দের দিন।","সাদাকাতুল ফিতর ও ঈদের সালাত।"),
IslamicEventInfo("আরাফাহ","৯ জিলহজ","হজের গুরুত্বপূর্ণ দিন।","অ-হাজীর জন্য নফল রোজা।"),
IslamicEventInfo("ঈদুল আজহা","১০ জিলহজ","কুরবানির ঈদ।","ঈদের সালাত ও সামর্থ্যবানদের কুরবানি।")
)
@Composable fun CalendarScreen(modifier:Modifier=Modifier){
val today=remember{LocalDate.now()}
var selected by remember{mutableStateOf(today)}
Column(modifier.fillMaxSize()){
SectionHeader(title="ইসলামি ক্যালেন্ডার",modifier=Modifier.padding(16.dp))
Card(Modifier.fillMaxWidth().padding(horizontal=16.dp)){Column(Modifier.padding(16.dp)){Text("আজ: "+today.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));Text("হিজরি তারিখ স্থানীয় চাঁদ দেখার ভিত্তিতে পরিবর্তিত হতে পারে।",style=MaterialTheme.typography.bodySmall);Spacer(Modifier.height(12.dp));Button(onClick={selected=today}){Text("আজকের তারিখ")}}}
Text("গুরুত্বপূর্ণ ইসলামি দিবস",style=MaterialTheme.typography.titleLarge,modifier=Modifier.padding(16.dp))
LazyColumn(contentPadding=PaddingValues(horizontal=16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){items(events){e->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){Text(e.title,style=MaterialTheme.typography.titleMedium);Text(e.hijri,style=MaterialTheme.typography.labelLarge);Text(e.description);Text("আমল: "+e.amal,style=MaterialTheme.typography.bodyMedium)}}}}
}}