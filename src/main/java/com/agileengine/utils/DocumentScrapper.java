package com.agileengine.utils;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.agileengine.config.Constant.CSS_LOCATOR_ATTRIBUTE_PATTERN;
import static java.util.stream.Collectors.toMap;

public class DocumentScrapper {
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

    public static Optional<Map<String, String>> getElementsAttributesMap(Optional<Elements> elements) {
        return elements.map(buttons ->
                {
                    Map<String, String> attributeMap = new HashMap<>();
                    buttons.iterator().forEachRemaining(element ->
                            attributeMap.putAll(element.attributes().asList().stream().collect(toMap(Attribute::getKey, Attribute::getValue)))
                    );
                    return attributeMap;
                }
        );
    }
}
