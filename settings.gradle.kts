pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {url = uri("https://jitpack.io")}
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven {url = uri("https://jitpack.io")}
    }
}

rootProject.name = "My Application"
include(":app")
 