package com.example;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "merge-sources")
public class MergeSourcesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true)
    private File sourceDirectory;

    @Parameter(
            defaultValue = "${project.build.directory}/merged-sources.java",
            required = true
    )
    private File outputFile;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("=== PR2automatizationUstynov: збираємо Java файли ===");

        if (!sourceDirectory.exists()) {
            getLog().warn("Директорія не знайдена: " + sourceDirectory);
            return;
        }

        List<File> javaFiles = new ArrayList<>();
        collectJavaFiles(sourceDirectory, javaFiles);

        if (javaFiles.isEmpty()) {
            getLog().warn("Java файлів не знайдено!");
            return;
        }

        getLog().info("Знайдено файлів: " + javaFiles.size());
        outputFile.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("// ================================================\n");
            writer.write("// Проєкт: PR2automatizationUstynov\n");
            writer.write("// Репозиторій: UstynovJavaAutomatization\n");
            writer.write("// Файлів об'єднано: " + javaFiles.size() + "\n");
            writer.write("// ================================================\n\n");

            for (File javaFile : javaFiles) {
                getLog().info("Додаємо: " + javaFile.getName());
                writer.write("// ---- " + javaFile.getAbsolutePath() + " ----\n");
                writer.write(new String(Files.readAllBytes(javaFile.toPath())));
                writer.write("\n\n");
            }

        } catch (IOException e) {
            throw new MojoExecutionException("Помилка запису: " + outputFile, e);
        }

        getLog().info("Готово! Файл: " + outputFile.getAbsolutePath());
    }

    private void collectJavaFiles(File dir, List<File> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) collectJavaFiles(file, result);
            else if (file.getName().endsWith(".java")) result.add(file);
        }
    }
}