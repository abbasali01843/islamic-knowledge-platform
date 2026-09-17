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

// Only include modules that currently have real build scripts.
// Placeholder directories under core/, feature/, and data/ remain in the
// tree for roadmap structure but are not Gradle projects until implemented.
include(":app")
include(":core:design")
include(":core:model")
include(":feature:home")
include(":feature:quran")
