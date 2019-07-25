import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.31"
    kotlin("kapt") version "1.3.31"
    id("com.github.johnrengelman.shadow") version "5.0.0"
    id("flavor.pie.promptsign") version "1.1.0"
    id("maven-publish")
}

group = "PS_TEMPLATE_GROUP_NAME"
version = "PS_TEMPLATE_VERSION"

repositories {
    mavenCentral()
    maven {
        name = "sponge"
        url = uri("https://repo.spongepowered.org/maven/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io/")
    }
    maven {
        name = "bstats"
        url = uri("https://repo.codemc.org/repository/maven-public")
    }
}

dependencies {
    val sponge = create(group = "org.spongepowered", name = "spongeapi", version = "7.1.0")
    api(sponge)
    kapt(sponge)
    val kotlin = kotlin("stdlib-jdk8")
    api(kotlin)
    shadow(kotlin)
    val kludge = create(group = "com.github.pie-flavor", name = "kludge", version = "477392a")
    implementation(kludge)
    shadow(kludge)
    val bstats = create(group = "org.bstats", name = "bstats-sponge-lite", version = "1.4")
    implementation(bstats)
    shadow(bstats)
}

tasks.named("jar") {
    enabled = false
}

val shadowJar = tasks.named<ShadowJar>("shadowJar") {
    configurations = listOf(project.configurations.shadow.get())
    archiveClassifier.set("")
    relocate("kotlin", "PS_TEMPLATE_BASE_PACKAGE_NAME.runtime.kotlin")
    relocate("flavor.pie.kludge", "PS_TEMPLATE_BASE_PACKAGE_NAME.util.kludge")
}

tasks.build {
    dependsOn(shadowJar)
}

tasks.named("signArchives") {
    dependsOn(shadowJar)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create("sponge", MavenPublication::class.java) {
            project.shadow.component(this)
            pom {
                name.set("PS_TEMPLATE_PLUGIN_NAME")
                description.set("PS_TEMPLATE_PLUGIN_DESCRIPTION")
                url.set("https://ore.spongepowered.org/PS_TEMPLATE_USER_NAME/PS_TEMPLATE_PLUGIN_ID/")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/PS_TEMPLATE_GITHUB_USER_NAME/PS_TEMPLATE_PROJECT_NAME/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("PS_TEMPLATE_USER_NAME")
                        name.set("PS_TEMPLATE_USER_REAL_NAME")
                        email.set("PS_TEMPLATE_USER_EMAIL")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/PS_TEMPLATE_GITHUB_USER_NAME/PS_TEMPLATE_PROJECT_NAME.git")
                    developerConnection.set("scm:git:ssh://github.com/PS_TEMPLATE_GITHUB_USER_NAME/PS_TEMPLATE_PROJECT_NAME.git")
                    url.set("https://github.com/PS_TEMPLATE_GITHUB_USER_NAME/PS_TEMPLATE_PROJECT_NAME/")
                }
            }
        }
        repositories {
            maven {
                val spongePublishingUri: String by project
                val spongePublishingUsername: String by project
                val spongePublishingPassword: String by project
                url = uri(spongePublishingUri)
                credentials {
                    username = spongePublishingUsername
                    password = spongePublishingPassword
                }
            }
        }
    }
}
