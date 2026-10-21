package com.namesorter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class NameFileService {
    public List<String> read(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public void write(Path path, List<String> names) throws IOException {
        Files.write(path, names, StandardCharsets.UTF_8);
    }
}
