package com.agileengine.validation;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DocPageValidation {
    private static Logger LOGGER = LoggerFactory.getLogger(DocPageValidation.class);

    public static void validateHtmlDocPages(List<Document> htmlDocPages) {
        LOGGER.info("Validate Page Size");
        if (htmlDocPages.isEmpty()) {
            throw new RuntimeException("No html pages file was found");
        }
    }
}
