package com.islamicknowledge.platform.feature.dua
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

data class DuaItem(val id:String,val category:String,val title:String,val arabic:String,val meaning:String,val reference:String,val repeat:Int)
private val duas=listOf(
DuaItem("istighfar","সকাল-সন্ধ্যা","সাইয়্যিদুল ইস্তিগফার","اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ","হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা।","সহীহ বুখারী: ৬৩০৬",1),
DuaItem("kursi","সকাল-সন্ধ্যা","আয়াতুল কুরসী","اللَّهُ لَا إِلَهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ","আল্লাহ, যিনি ব্যতীত কোনো সত্য উপাস্য নেই; তিনি চিরঞ্জীব, সর্বসত্তার ধারক।","সূরা আল-বাকারাহ: ২৫৫",1),
DuaItem("protection","সকাল-সন্ধ্যা","সকল ক্ষতি ও অনিষ্ট থেকে বাঁচার দোয়া","بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ","আল্লাহর নামে, যাঁর নামের বরকতে জমিন ও আসমানে কোনো বস্তুই ক্ষতি করতে পারে না।","সুনানে আবু দাউদ: ৫০৮৮; তিরমিযী: ৩৩৮৮",3),
DuaItem("radheetu","সকাল-সন্ধ্যা","আল্লাহকে রব হিসেবে মেনে নেওয়ার দোয়া","رَضِيتُ بِاللَّهِ رَبًّا، وَبِالْإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ نَبِيًّا","আমি সন্তুষ্টচিত্তে আল্লাহকে রব, ইসলামকে দ্বীন এবং মুহাম্মদ ﷺ-কে নবী হিসেবে গ্রহণ করেছি।","সুনানে আবু দাউদ: ৫০৭৪; তিরমিযী: ৩৩৮৯",3),
DuaItem("hawla","জিকির","লা হাওলা ওয়ালা কুওয়াতা ইল্লা বিল্লাহ","لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ","আল্লাহর সাহায্য ছাড়া পাপ থেকে ফেরার এবং নেক আমল করার কোনো সামর্থ্য নেই।","সহীহ বুখারী: ৪২০৫",33),
DuaItem("rabbana","কুরআনের দোয়া","রব্বানা আতিনা ফিদ্দুনিয়া হাসানাহ","رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ","হে আমাদের রব! আমাদের দুনিয়াতে কল্যাণ দিন, আখিরাতেও কল্যাণ দিন এবং আমাদের আগুনের শাস্তি থেকে রক্ষা করুন।","সূরা আল-বাকারাহ: ২০১",1)
)
@Composable fun DuaScreen(modifier:Modifier=Modifier){
var category by remember{mutableStateOf("সকল")}
val cats=listOf("সকল","সকাল-সন্ধ্যা","জিকির","কুরআনের দোয়া")
val list=if(category=="সকল") duas else duas.filter{it.category==category}
Column(modifier){
SectionHeader(title="দোয়া ও জিকির",modifier=Modifier.padding(16.dp))
LazyRow(contentPadding=PaddingValues(horizontal=16.dp),horizontalArrangement=Arrangement.spacedBy(8.dp)){items(cats){c->FilterChip(selected=category==c,onClick={category=c},label={Text(c)})}}
LazyColumn(contentPadding=PaddingValues(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
items(list,key={it.id}){d->Card(Modifier.fillMaxWidth()){Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)){Text(d.title,style=MaterialTheme.typography.titleMedium);Text(d.arabic,style=MaterialTheme.typography.titleLarge);Text(d.meaning,style=MaterialTheme.typography.bodyLarge);Text(d.reference+" • "+d.repeat+" বার",style=MaterialTheme.typography.labelMedium)}}}
}}}}