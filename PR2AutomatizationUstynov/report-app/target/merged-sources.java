// ================================================
// Проєкт: PR2automatizationUstynov
// Репозиторій: UstynovJavaAutomatization
// Файлів об'єднано: 3
// ================================================

// ---- D:\UstynovJavaAutomatization\PR2AutomatizationUstynov\report-app\src\main\java\com\example\report\generator\ReportGenerator.java ----
package com.example.report.generator;

import com.example.report.model.ReportData;
import java.io.*;
import java.util.Map;

public class ReportGenerator {

    public void generateHtml(ReportData data, String outputPath)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head>")
                .append("<meta charset='UTF-8'>")
                .append("<title>").append(data.getTitle()).append("</title>")
                .append("<style>")
                .append("body{font-family:Arial;margin:40px}")
                .append("table{border-collapse:collapse;width:100%}")
                .append("th,td{border:1px solid #ccc;padding:8px;text-align:left}")
                .append("th{background:#4CAF50;color:white}")
                .append("</style></head><body>")
                .append("<h1>").append(data.getTitle()).append("</h1>")
                .append("<p>Автор: ").append(data.getAuthor()).append("</p>")
                .append("<p>Дата: ").append(data.getDate()).append("</p>")
                .append("<table><tr>");

        // Заголовки таблиці
        if (!data.getRows().isEmpty()) {
            for (String key : data.getRows().get(0).keySet()) {
                sb.append("<th>").append(key).append("</th>");
            }
        }
        sb.append("</tr>");

        // Рядки таблиці
        for (Map<String, String> row : data.getRows()) {
            sb.append("<tr>");
            for (String val : row.values()) {
                sb.append("<td>").append(val).append("</td>");
            }
            sb.append("</tr>");
        }

        sb.append("</table></body></html>");

        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(sb.toString());
        }

        System.out.println("HTML звіт збережено: " + outputPath);
    }

    public void generateTxt(ReportData data, String outputPath)
            throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(data.getTitle()).append(" ===\n");
        sb.append("Автор: ").append(data.getAuthor()).append("\n");
        sb.append("Дата:  ").append(data.getDate()).append("\n\n");

        for (Map<String, String> row : data.getRows()) {
            row.forEach((k, v) ->
                    sb.append(k).append(": ").append(v).append(" | "));
            sb.append("\n");
        }

        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(sb.toString());
        }

        System.out.println("TXT звіт збережено: " + outputPath);
    }
}

// ---- D:\UstynovJavaAutomatization\PR2AutomatizationUstynov\report-app\src\main\java\com\example\report\Main.java ----
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

// ---- D:\UstynovJavaAutomatization\PR2AutomatizationUstynov\report-app\src\main\java\com\example\report\model\ReportData.java ----
package com.example.report.model;

import java.util.List;
import java.util.Map;

public class ReportData {
    private String title;
    private String author;
    private String date;
    private List<Map<String, String>> rows;

    public ReportData(String title, String author,
                      String date, List<Map<String, String>> rows) {
        this.title  = title;
        this.author = author;
        this.date   = date;
        this.rows   = rows;
    }

    public String getTitle()  { return title;  }
    public String getAuthor() { return author; }
    public String getDate()   { return date;   }
    public List<Map<String, String>> getRows() { return rows; }
}

