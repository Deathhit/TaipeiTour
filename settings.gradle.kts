pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Taipei Tour"
include(":app")
include(":core:app_database")
include(":core:app_ui")
include(":core:travel_taipei_api")
include(":data:attraction")
include(":data:attraction_image")
include(":data:event")
include(":data:language")
include(":domain")
include(":feature:attraction_list")
include(":feature:attraction_detail")
include(":feature:attraction_gallery")
include(":feature:event_list")
include(":feature:image_viewer")
include(":feature:set_language")