package com.agileengine;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.agileengine.config.Constant.CSS_LOCATOR_ATTRIBUTE_PATTERN;

public class DocumentScrapper {
    private static Logger LOGGER = LoggerFactory.getLogger(DocumentScrapper.class);

    public static Elements findElement(Document document, Map<String, String> attributeMap) {
        Elements foundElement = null;

        for (Map.Entry<String, String> attribute: attributeMap.entrySet()) {
            Elements requiredElement = document.select(String.format(CSS_LOCATOR_ATTRIBUTE_PATTERN, attribute.getKey(), attribute.getValue()));
            if (!requiredElement.isEmpty()) {
                foundElement = requiredElement;
                break;
            }
        }

        return foundElement;
    }
}
