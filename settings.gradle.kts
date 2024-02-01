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

rootProject.name = "Taipei Tour"
include(":app")
include(":core:app_database")
include(":core:app_ui")
include(":core:travel_taipei_api")
include(":data:attraction")
include(":data:attraction_image")
include(":data:language")
include(":domain")
include(":feature:attraction_list")
include(":feature:attraction_detail")
include(":feature:attraction_gallery")
include(":feature:image_viewer")
include(":feature:set_language")