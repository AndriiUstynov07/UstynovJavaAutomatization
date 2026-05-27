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