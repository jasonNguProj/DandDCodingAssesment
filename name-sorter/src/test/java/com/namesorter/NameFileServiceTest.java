package com.namesorter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NameFileServiceTest {
    @TempDir Path directory;
    private final NameFileService files = new NameFileService();

    @Test
    void readsUtf8AndRetainsBlankLinesForValidation() throws IOException {
        Path input = directory.resolve("input.txt");
        Files.writeString(input, "Élodie Martin\n\nAmy Smith\n", StandardCharsets.UTF_8);
        assertEquals(List.of("Élodie Martin", "", "Amy Smith"), files.read(input));
    }

    @Test
    void writesUtf8AndOverwritesExistingContent() throws IOException {
        Path output = directory.resolve("output.txt");
        Files.writeString(output, "old content that must disappear\nsecond line\n");
        files.write(output, List.of("Élodie Martin"));
        assertEquals(List.of("Élodie Martin"), Files.readAllLines(output, StandardCharsets.UTF_8));
        files.write(output, List.of());
        assertEquals(0L, Files.size(output));
    }

    @Test
    void readsEmptyFile() throws IOException {
        assertEquals(List.of(), files.read(Files.createFile(directory.resolve("empty.txt"))));
    }

    @Test
    void propagatesReadAndWriteFailures() {
        assertThrows(IOException.class, () -> files.read(directory.resolve("missing.txt")));
        assertThrows(IOException.class, () -> files.write(directory, List.of("Amy Smith")));
    }
}
