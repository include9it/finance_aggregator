package io.include9it.finance_aggregator.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class TestUtil {

    public static String readResourceFile(String resourceName) throws IOException, URISyntaxException {
        var resource = Objects.requireNonNull(TestUtil.class.getClassLoader().getResource(resourceName));
        var path = Paths.get(resource.toURI());
        return Files.readString(path);
    }
}
