package com.sope.sope_ecommerce_backend.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class ParamUtil {
    private ParamUtil() {}

    /** Build query from sorted map; encodeValues=true for VNPay query */
    public static String toQueryString(Map<String, String> params, boolean encodeValues) {
        return params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + (encodeValues
                        ? URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8)
                        : e.getValue()))
                .reduce((a,b) -> a + "&" + b).orElse("");
    }

    /** Flatten request param map (GET/POST) to String->String */
    public static Map<String, String> flatten(Map<String, ?> in) {
        Map<String, String> out = new HashMap<>();
        in.forEach((k,v) -> {
            if (v == null) out.put(k, "");
            else if (v instanceof String[]) out.put(k, ((String[]) v)[0]);
            else out.put(k, v.toString());
        });
        return out;
    }

    /** Copy map and remove keys */
    public static Map<String, String> copyWithout(Map<String, String> src, String... removeKeys) {
        Map<String, String> m = new HashMap<>(src);
        for (String k: removeKeys) m.remove(k);
        return m;
    }
}