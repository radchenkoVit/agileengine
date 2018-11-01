package com.agileengine;

import com.agileengine.config.Constant;
import com.agileengine.validation.DocPageValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.agileengine.config.Constant.CSS_TARGET_ELEMENT_ID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Run {
    private static Logger LOGGER = LoggerFactory.getLogger(Run.class);

    private static String[] arrays = {"D:\\bobocode\\html_pages\\sample-0-origin.html", "D:\\bobocode\\html_pages\\sample-1-evil-gemini.html"};

    public static void main(String[] filePaths) {
        List<Document> htmlDocPages = Arrays.stream(arrays).map(filePath -> getDocument(filePath).orElse(null)).filter(Objects::nonNull).collect(toList());
        DocPageValidation.validateHtmlDocPages(htmlDocPages);

        Document initDocument = htmlDocPages.get(0);
        Optional<Elements> initTargetElement = Optional.of(initDocument.select(CSS_TARGET_ELEMENT_ID));
        Map<String, String> targetElementAttributesMap = getElementsAttributesMap(initTargetElement).get();

        Map<String, String> resultMap = new HashMap<>();

        for (int i = 1; i < htmlDocPages.size(); i++) {
            Document currentDoc = htmlDocPages.get(i);

            Elements foundElementForSecondPage = DocumentScrapper.findElement(currentDoc, targetElementAttributesMap);
            String foundElementPath = buildElementPath(foundElementForSecondPage);
            resultMap.put(arrays[i], foundElementPath);
        }

        resultMap.forEach((key, value) -> LOGGER.info("Doc name: " + key + ", element path: " + value));
    }


    private static String buildElementPath(Elements elements) {
        if (elements == null) {
            return "NOT FOUND";
        }
        Element child = elements.first();

        List<String> elementPathList = new ArrayList<>();
        elementPathList.add(child.tagName() + "[?]");//TODO: need to find number of element if it has siblings

        Element parent = child.parent();
        while (parent != null) {
            elementPathList.add(parent.tagName() + "[?]");
            parent = parent.parent();
        }

        Collections.reverse(elementPathList);

        return elementPathList.stream().collect(Collectors.joining(" -> "));
    }


    private static Optional<Document> getDocument(String path) {
        File htmlFile = Paths.get(path).toFile();

        try {
            return Optional.of(Jsoup.parse(
                    htmlFile,
                    Constant.DEFAULT_CHARSET_NAME,
                    htmlFile.getAbsolutePath()));
        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }

    private static Optional<Map<String, String>> getElementsAttributesMap(Optional<Elements> elements) {
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
