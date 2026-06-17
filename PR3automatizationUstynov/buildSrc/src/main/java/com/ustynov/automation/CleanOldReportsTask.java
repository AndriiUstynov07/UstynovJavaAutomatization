package com.ustynov.automation;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import java.io.File;

public class CleanOldReportsTask extends DefaultTask {

    @TaskAction
    public void run() {
        // Отримуємо шлях до папки build/custom-reports
        File reportsDir = new File(getProject().getLayout().getBuildDirectory().get().getAsFile(), "custom-reports");

        if (reportsDir.exists()) {
            File[] files = reportsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".txt")) {
                        if (file.delete()) {
                            getLogger().lifecycle("--- [Java Plugin] Видалено застарілий звіт: " + file.getName());
                        }
                    }
                }
            }
        }
    }
}