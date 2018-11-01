package com.agileengine;

import com.agileengine.config.Constant;
import com.agileengine.validation.DocPageValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Run {
    private static Logger LOGGER = LoggerFactory.getLogger(Run.class);

    public static void main(String[] filePaths) {
        List<Document> htmlDocPages = Arrays.stream(filePaths).map(filePath -> getDocument(filePath).orElse(null)).filter(Objects::nonNull).collect(toList());
        DocPageValidation.validateHtmlDocPages(htmlDocPages);



    }


    private static Optional<Document> getDocument(String path) {
        File htmlFile = Paths.get(path).toFile();

        try {
            return Optional.of(Jsoup.parse(
                    htmlFile,
                    Constant.DEFAULT_CHARSET_NAME,
                    htmlFile.getAbsolutePath()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
