plugins {
    java // Наш проєкт — це Java-додаток
    id("my-automation-plugin") // Підключаємо наш Java-плагін, створений вище
}

group = "com.ustynov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

// 2) Додаємо щонайменше 1 task в build.gradle.kts
tasks.register<Zip>("zipProjectReport") {
    group = "ustynov-automation"
    description = "Archives the generated build report into a ZIP file."

    // Звідки брати файли та куди класти архів
    from("${layout.buildDirectory.get()}/custom-reports")
    archiveFileName.set("BuildReport_Ustynov.zip")
    destinationDirectory.set(file("${layout.buildDirectory.get()}/outputs/archives"))

    // 3) Логічний зв'язок: архівуємо тільки ПІСЛЯ генерації звіту Java-плагіном
    dependsOn("generateBuildInfo")

    doLast {
        logger.lifecycle("--- [Main Build] Звіт успішно заархівовано в BuildReport_Ustynov.zip")
    }
}