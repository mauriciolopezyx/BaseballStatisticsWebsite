package com.auth_example.baseballguru.utility;

import java.text.Normalizer;

public class SlugUtil {

    public static String standardize(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        return normalized.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "");
    }

    public static String slugify(String name, long id) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);
        return normalized.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                + "-" + id;
    }

}
