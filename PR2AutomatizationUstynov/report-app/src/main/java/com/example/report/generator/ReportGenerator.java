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