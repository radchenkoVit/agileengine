package com.agileengine.validation;

import org.jsoup.nodes.Document;

import java.util.List;

public class DocPageValidation {
    public static void validateHtmlDocPages(List<Document> htmlDocPages) {
        if (htmlDocPages.isEmpty()) {
            throw new RuntimeException("No html pages file was found");
        }
    }
}
