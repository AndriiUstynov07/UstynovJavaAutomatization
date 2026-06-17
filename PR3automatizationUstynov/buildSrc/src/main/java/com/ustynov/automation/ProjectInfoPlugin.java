package com.ustynov.automation;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

public class ProjectInfoPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Реєструємо Task 1 (Очищення)
        Task cleanTask = project.getTasks().create("cleanOldReports", CleanOldReportsTask.class);
        cleanTask.setGroup("ustynov-automation");
        cleanTask.setDescription("Deletes outdated build reports.");

        // Реєструємо Task 2 (Генерація)
        Task generateTask = project.getTasks().create("generateBuildInfo", GenerateBuildInfoTask.class);
        generateTask.setGroup("ustynov-automation");
        generateTask.setDescription("Generates text file with project build details.");

        // Встановлюємо логічну залежність: очищення виконується ПЕРЕД генерацією
        generateTask.dependsOn(cleanTask);
    }
}