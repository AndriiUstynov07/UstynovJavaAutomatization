plugins {
    java
    checkstyle // Плагін для статичного аналізу коду
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // Використовуємо офіційну платформу JUnit 6
    testImplementation(platform("org.junit:junit-bom:6.1.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform() // Запуск тестів через сучасний рушій
}

// Налаштування Checkstyle (статичний аналіз)
checkstyle {
    toolVersion = "10.12.4"
    isIgnoreFailures = false // Пайплайн впаде, якщо є помилки стилю
    configFile = file("${rootProject.projectDir}/config/checkstyle/checkstyle.xml")
}

// Кастомізація імені вихідного JAR файлу
tasks.jar {
    archiveFileName.set("PR6automatizationUstynov.jar")
}