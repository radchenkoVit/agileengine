package com.agileengine;

import com.agileengine.config.Constant;
import com.agileengine.utils.DocumentScrapper;
import com.agileengine.validation.DocPageValidation;
import org.jsoup.Jsoup;
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

public class Run {
    private static Logger LOGGER = LoggerFactory.getLogger(Run.class);

    public static void main(String[] filePaths) {
        //TODO: refactor
        List<Document> htmlDocPages = Arrays.stream(filePaths).map(filePath -> getDocument(filePath).orElse(null)).filter(Objects::nonNull).collect(toList());
        DocPageValidation.validateHtmlDocPages(htmlDocPages);

        Document initDocument = htmlDocPages.get(0);
        Optional<Elements> initTargetElement = Optional.of(initDocument.select(CSS_TARGET_ELEMENT_ID));
        Map<String, String> targetElementAttributesMap = DocumentScrapper.getElementsAttributesMap(initTargetElement).get();

        Map<String, String> resultMap = new HashMap<>();

        //TODO: need to make it via IntStream
        for (int i = 1; i < htmlDocPages.size(); i++) {
            Document currentDoc = htmlDocPages.get(i);

            Elements foundElementForSecondPage = DocumentScrapper.findElement(currentDoc, targetElementAttributesMap);
            String foundElementPath = buildElementPath(foundElementForSecondPage);
            resultMap.put(filePaths[i], foundElementPath);
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
}
