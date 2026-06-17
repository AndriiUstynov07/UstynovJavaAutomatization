package com.ustynov.automation;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateBuildInfoTask extends DefaultTask {

    @TaskAction
    public void run() {
        File reportsDir = new File(getProject().getLayout().getBuildDirectory().get().getAsFile(), "custom-reports");
        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        File reportFile = new File(reportsDir, "build-info.txt");
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write("=========================================\n");
            writer.write("ПРОЄКТ: " + getProject().getName() + "\n");
            writer.write("Група: " + getProject().getGroup() + "\n");
            writer.write("Версія: " + getProject().getVersion() + "\n");
            writer.write("Час автоматизації: " + currentTime + "\n");
            writer.write("Розробник: Андрій Устинов (Java Plugin)\n");
            writer.write("=========================================\n");

            getLogger().lifecycle("--- [Java Plugin] Новий звіт успішно створено: " + reportFile.getAbsolutePath());
        } catch (IOException e) {
            getLogger().error("Помилка при записі файлу звіту", e);
        }
    }
}