package com.islamicknowledge.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class MoreItem(
    val title: String,
    val subtitle: String,
    val route: Int
)

@Composable
fun MoreScreen(
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        MoreItem("হাদিস", "হাদিস ও বিষয়ভিত্তিক অনুসন্ধান", 1),
        MoreItem("দোয়া ও জিকির", "দৈনন্দিন দোয়া ও জিকির", 2),
        MoreItem("কিবলা", "GPS ও কম্পাস দিয়ে কিবলার দিক", 3),
        MoreItem("ইসলামি ক্যালেন্ডার", "গুরুত্বপূর্ণ ইসলামি দিবস", 4),
        MoreItem("যাকাত হিসাব", "নিসাব ও ২.৫% হিসাব", 5),
        MoreItem("নামাজ শিক্ষা", "অজু ও সালাতের ধাপ", 6),
        MoreItem("রমজান", "সিয়াম ও রমজানের আমল", 7),
        MoreItem("হজ শিক্ষা", "হজের ধাপ ও প্রস্তুতি", 8),
        MoreItem("সীরাতুন নবী ﷺ", "নবীজির জীবনের ধারাবাহিক পাঠ", 9)
    )

    Column(modifier.fillMaxSize()) {
        Text(
            text = "আরও",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(20.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items, key = { it.route }) { item ->
                Card(
                    onClick = { onItemClick(item.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.subtitle,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
