package com.islamicknowledge.platform.feature.hadith

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.islamicknowledge.platform.core.design.components.SectionHeader

data class HadithItem(
    val id: String,
    val book: String,
    val number: Int,
    val arabic: String,
    val bengali: String,
    val reference: String,
)

private val hadiths = listOf(
    HadithItem("niyyah", "সহীহুল বুখারী", 1, "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى", "নিশ্চয়ই সমস্ত কাজ নিয়তের ওপর নির্ভরশীল। আর প্রত্যেক ব্যক্তি যা নিয়ত করে কেবল তা-ই পায়।", "সহীহ বুখারী: ১; সহীহ মুসলিম"),
    HadithItem("jibreel", "সহীহ মুসলিম", 8, "أَنْ تَعْبُدَ اللهَ كَأَنَّكَ تَرَاهُ، فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ", "ইহসান হলো—তুমি এমনভাবে আল্লাহর ইবাদত করবে যেন তুমি তাঁকে দেখছো; আর যদি তুমি তাঁকে দেখতে না পাও, তবে নিশ্চয় তিনি তোমাকে দেখছেন।", "সহীহ মুসলিম: ৮"),
    HadithItem("brotherhood", "সহীহুল বুখারী", 13, "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لِأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ", "তোমাদের কেউ ততক্ষণ পর্যন্ত প্রকৃত মুমিন হতে পারবে না, যতক্ষণ না সে নিজের ভাইয়ের জন্য তা-ই পছন্দ করবে যা সে নিজের জন্য পছন্দ করে।", "সহীহ বুখারী: ১৩; সহীহ মুসলিম"),
    HadithItem("character", "জামে আত-তিরমিযী", 2002, "مَا مِنْ شَيْءٍ أَثْقَلُ فِي مِيزَانِ الْمُؤْمِنِ يَوْمَ الْقِيَامَةِ مِنْ حُسْنِ الْخُلُقِ", "কিয়ামতের দিন মুমিনের দাঁড়িপাল্লায় উত্তম চরিত্রের চেয়ে ভারী আর কোনো জিনিস হবে না।", "সহীহ আত-তিরমিযী: ২০০২"),
    HadithItem("anger", "সহীহুল বুখারী", 6114, "لَيْسَ الشَّدِيدُ بِالصُّرَعَةِ، إِنَّمَا الشَّدِيدُ الَّذِي يَمْلِكُ نَفْسَهُ عِنْدَ الْغَضَبِ", "প্রকৃত শক্তিশালী সে, যে রাগের সময় নিজেকে নিয়ন্ত্রণে রাখতে পারে।", "সহীহ বুখারী: ৬১১৪; সহীহ মুসলিম"),
    HadithItem("smile", "জামে আত-তিরমিযী", 1956, "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ", "তোমার মুসলিম ভাইয়ের মুখের দিকে তাকিয়ে মুচকি হাসি দেওয়াও তোমার জন্য সদকা।", "সহীহ আত-তিরমিযী: ১৯৫৬"),
)

@Composable
fun HadithScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    val list = if (query.isBlank()) {
        hadiths
    } else {
        hadiths.filter {
            it.bengali.contains(query, ignoreCase = true) ||
                it.book.contains(query, ignoreCase = true)
        }
    }

    Column(modifier) {
        SectionHeader(
            title = "হাদিস",
            modifier = Modifier.padding(16.dp),
        )
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("হাদিস খুঁজুন") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(list, key = { it.id }) { hadith ->
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            "${hadith.book} • ${hadith.number}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(hadith.arabic, style = MaterialTheme.typography.titleLarge)
                        Text(hadith.bengali, style = MaterialTheme.typography.bodyLarge)
                        Text(hadith.reference, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
