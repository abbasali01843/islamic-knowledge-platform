package com.islamicknowledge.platform.feature.learn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader
data class SalahLesson(val title:String,val type:String,val body:String,val arabic:String="")
private val lessons=listOf(
SalahLesson("অজুর ফরজ","অজু","মুখমণ্ডল, কনুইসহ হাত, মাথা মাসেহ এবং টাখনুসহ পা ধোয়া।"),
SalahLesson("গোসলের ফরজ","গোসল","কুলি, নাকে পানি এবং পুরো শরীরে পানি পৌঁছানো।"),
SalahLesson("নামাজের ধাপ ১","কিয়াম","কিবলামুখী হয়ে নিয়ত করে আল্লাহু আকবার বলে নামাজ শুরু করুন।","اللَّهُ أَكْبَرُ"),
SalahLesson("নামাজের ধাপ ২","কিয়াম","সানা ও কিরাআত পাঠ করুন।","سُبْحَانَكَ اللَّهُمَّ وَبِحَمْدِكَ"),
SalahLesson("নামাজের ধাপ ৩","রুকু","আল্লাহু আকবার বলে রুকুতে গিয়ে তাসবীহ পড়ুন।","سُبْحَانَ رَبِّيَ الْعَظِيمِ"),
SalahLesson("নামাজের ধাপ ৪","সিজদাহ","রুকু থেকে উঠে দুই সিজদা করুন এবং তাসবীহ পড়ুন।","سُبْحَانَ رَبِّيَ الْأَعْلَى"),
SalahLesson("নামাজের ধাপ ৫","শেষ বৈঠক","তাশাহহুদ, দরুদ ও দোয়া পড়ে সালাম ফিরিয়ে নামাজ শেষ করুন।")
)
@Composable fun LearnSalahScreen(modifier:Modifier=Modifier){
var filter by rememberSaveable{mutableStateOf("সব")}; val filters=listOf("সব","অজু","গোসল","কিয়াম","রুকু","সিজদাহ","শেষ বৈঠক"); val list=if(filter=="সব") lessons else lessons.filter{it.type==filter}
Column(modifier.fillMaxSize()){SectionHeader(title="নামাজ শিক্ষা",modifier=Modifier.padding(16.dp));LazyRow(contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){items(filters){f->FilterChip(selected=filter==f,onClick={filter=f},label={Text(f)})}};LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){items(list){l->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text(l.title,style=MaterialTheme.typography.titleMedium);if(l.arabic.isNotBlank())Text(l.arabic,style=MaterialTheme.typography.titleLarge);Text(l.body)}}}}}}