package com.example.report;

import com.example.report.generator.ReportGenerator;
import com.example.report.model.ReportData;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        List<Map<String, String>> rows = new ArrayList<>();

        Map<String, String> r1 = new LinkedHashMap<>();
        r1.put("Продукт", "Ноутбук");
        r1.put("Кількість", "10");
        r1.put("Ціна", "25000");
        rows.add(r1);

        Map<String, String> r2 = new LinkedHashMap<>();
        r2.put("Продукт", "Телефон");
        r2.put("Кількість", "25");
        r2.put("Ціна", "12000");
        rows.add(r2);

        Map<String, String> r3 = new LinkedHashMap<>();
        r3.put("Продукт", "Планшет");
        r3.put("Кількість", "15");
        r3.put("Ціна", "18000");
        rows.add(r3);

        ReportData reportData = new ReportData(
                "Звіт продажів",
                "Іван Петренко",
                LocalDate.now().toString(),
                rows
        );

        // Автоматично визначаємо шлях до target
        String targetDir = System.getProperty("user.dir") + "\\target\\";
        new File(targetDir).mkdirs();

        ReportGenerator generator = new ReportGenerator();
        generator.generateHtml(reportData, targetDir + "report.html");
        generator.generateTxt(reportData,  targetDir + "report.txt");

        System.out.println("Звіти успішно згенеровано!");
        System.out.println("Шлях: " + targetDir);
    }
}