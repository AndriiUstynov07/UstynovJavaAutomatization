plugins {
    `java-gradle-plugin` // Автоматично підключає Java та Gradle API
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins {
        // Використовуємо register замість create
        register("myJavaAutomationPlugin") {
            id = "my-automation-plugin"
            implementationClass = "com.ustynov.automation.ProjectInfoPlugin"
        }
    }
}