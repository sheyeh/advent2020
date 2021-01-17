package advent2020;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReader {
    public static List<String> readFile(String fileName) {
        try {
            return Files.readAllLines(Paths.get(fileName)).stream()
                    .collect(Collectors.toList());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }
}