package com.islamicknowledge.platform.feature.quran

data class ReaderAyah(
    val number: Int,
    val arabic: String,
    val bengali: String,
)

/**
 * Bundled Quran reader content.
 *
 * The reader consumes this provider rather than owning content inside Compose UI.
 * The same API can later be backed by a generated JSON/SQLite content package.
 */
fun quranReaderAyahs(surahNumber: Int): List<ReaderAyah> = when (surahNumber) {
    1 -> alFatihahAyahs
    2 -> alBaqarahAyahs
    else -> emptyList()
}

fun quranReaderSearch(query: String): List<Pair<Int, ReaderAyah>> {
    if (query.isBlank()) return emptyList()
    return readerContent.flatMap { (surahNumber, ayahs) ->
        ayahs.filter {
            it.arabic.contains(query, ignoreCase = true) || it.bengali.contains(query, ignoreCase = true)
        }.map { surahNumber to it }
    }
}

val quranReaderContentSurahNumbers: Set<Int> = setOf(1, 2)

private val readerContent: Map<Int, List<ReaderAyah>> = mapOf(
    1 to listOf(
        ReaderAyah(1, "بِسۡمِ ٱللَّهِ ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ", "পরম করুণাময় অতি দয়ালু আল্লাহর নামে।"),
        ReaderAyah(2, "ٱلۡحَمۡدُ لِلَّهِ رَبِّ ٱلۡعَٰلَمِينَ", "সকল প্রশংসা কেবল আল্লাহর জন্য, যিনি সৃষ্টিকুলের রব।"),
        ReaderAyah(3, "ٱلرَّحۡمَٰنِ ٱلرَّحِيمِ", "(যিনি) পরম করুণাময়, অতি দয়ালু।"),
        ReaderAyah(4, "مَٰلِكِ يَوۡمِ ٱلدِّينِ", "(যিনি) বিচার দিবসের মালিক।"),
        ReaderAyah(5, "إِيَّاكَ نَعۡبُدُ وَإِيَّاكَ نَسۡتَعِينُ", "আমরা শুধু আপনারই ‘ইবাদাত করি এবং শুধু আপনার নিকটই সাহায্য চাই।"),
        ReaderAyah(6, "ٱهۡدِنَا ٱلصِّرَٰطَ ٱلۡمُسۡتَقِيمَ", "আপনি আমাদেরকে সরল পথের হিদায়াত দিন।"),
        ReaderAyah(7, "صِرَٰطَ ٱلَّذِينَ أَنۡعَمۡتَ عَلَيۡهِمۡ غَيۡرِ ٱلۡمَغۡضُوبِ عَلَيۡهِمۡ وَلَا ٱلضَّآلِّينَ", "তাদের পথ, যাদেরকে আপনি নি‘আমত দিয়েছেন। তাদের পথ নয়, যাদের ওপর আপনার ক্রোধ নিপতিত হয়েছে এবং যারা পথভ্রষ্ট হয়ে গেছে।"),
    ),
    2 to listOf(
        ReaderAyah(1, "الٓمٓ", "আলিফ-লাম-মীম।"),
        ReaderAyah(2, "ذَٰلِكَ ٱلۡكِتَٰبُ لَا رَيۡبَۛ فِيهِۛ هُدٗى لِّلۡمُتَّقِينَ", "এটি সেই কিতাব, যাতে কোনো সন্দেহ নেই; মুত্তাকীদের জন্য এটি হিদায়াত।"),
        ReaderAyah(3, "ٱلَّذِينَ يُؤۡمِنُونَ بِٱلۡغَيۡبِ وَيُقِيمُونَ ٱلصَّلَوٰةَ وَمِمَّا رَزَقۡنَٰهُمۡ يُنفِقُونَ", "যারা গায়েবের প্রতি ঈমান আনে, সালাত কায়েম করে এবং আমি তাদেরকে যে রিযিক দিয়েছি তা থেকে ব্যয় করে।"),
        ReaderAyah(4, "وَٱلَّذِينَ يُؤۡمِنُونَ بِمَآ أُنزِلَ إِلَيۡكَ وَمَآ أُنزِلَ مِن قَبۡلِكَ وَبِٱلۡأٓخِرَةِ هُمۡ يُوقِنُونَ", "আর যারা ঈমান আনে আপনার প্রতি যা নাযিল হয়েছে এবং যা আপনার পূর্বে নাযিল হয়েছে, আর আখিরাতের প্রতি তারা নিশ্চিত বিশ্বাস রাখে।"),
        ReaderAyah(5, "أُوْلَٰٓئِكَ عَلَىٰ هُدٗى مِّن رَّبِّهِمۡۖ وَأُوْلَٰٓئِكَ هُمُ ٱلۡمُفۡلِحُونَ", "তারাই তাদের রবের পক্ষ থেকে হিদায়াতের উপর আছে এবং তারাই সফলকাম।"),
    ),
)

private val alFatihahAyahs = readerContent.getValue(1)
private val alBaqarahAyahs = readerContent.getValue(2)
