import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IslamicKnowledgePlatform"
include(":app")
include(":core:common")
include(":core:model")
include(":core:database")
include(":core:network")
include(":core:navigation")
include(":core:search")
include(":core:audio")
include(":core:location")
include(":core:notifications")
include(":core:design")
include(":feature:home")
include(":feature:quran")
include(":feature:hadith")
include(":feature:prayer")
include(":feature:ramadan")
include(":feature:qibla")
include(":feature:dua")
include(":feature:athkar")
include(":feature:learn")
include(":feature:seerah")
include(":feature:calendar")
include(":feature:zakat")
include(":feature:hajj")
include(":feature:quiz")
include(":data:quran")
include(":data:hadith")
include(":data:dua")
include(":data:knowledge")
