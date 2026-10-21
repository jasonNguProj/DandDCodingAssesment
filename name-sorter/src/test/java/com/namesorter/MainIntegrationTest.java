package com.namesorter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MainIntegrationTest {
    @TempDir Path directory;

    @Test
    void sortsNamesThroughCliAndPreservesOutputWhenLaterInputIsInvalid() throws Exception {
        Path input = directory.resolve("input.txt");
        Path output = directory.resolve("sorted-names-list.txt");
        Path stdout = directory.resolve("stdout.txt");
        Path stderr = directory.resolve("stderr.txt");
        Files.write(input, List.of("Amy Smith", "Zoe Archer", "Amy Ann Smith", "Amy Smith"),
                StandardCharsets.UTF_8);
        Files.writeString(output, "stale output that should be overwritten\n");

        assertEquals(0, runCli(input, stdout, stderr));
        List<String> expected = List.of("Zoe Archer", "Amy Smith", "Amy Smith", "Amy Ann Smith");
        assertEquals(expected, Files.readAllLines(stdout, StandardCharsets.UTF_8));
        assertEquals(expected, Files.readAllLines(output, StandardCharsets.UTF_8));
        assertEquals("", Files.readString(stderr));

        byte[] savedOutput = Files.readAllBytes(output);
        Files.write(input, List.of("Zoe Archer", "", "Invalid"), StandardCharsets.UTF_8);

        assertEquals(1, runCli(input, stdout, stderr));
        assertEquals("", Files.readString(stdout));
        assertTrue(Files.readString(stderr).contains("Invalid name at line 3"));
        assertArrayEquals(savedOutput, Files.readAllBytes(output));
    }

    private int runCli(Path input, Path stdout, Path stderr) throws Exception {
        Path java = Path.of(System.getProperty("java.home"), "bin", "java");
        Path classes = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        Process process = new ProcessBuilder(java.toString(), "-cp", classes.toString(),
                Main.class.getName(), input.toString())
                .directory(directory.toFile())
                .redirectOutput(stdout.toFile())
                .redirectError(stderr.toFile())
                .start();
        try {
            assertTrue(process.waitFor(10, TimeUnit.SECONDS), "CLI did not finish within 10 seconds");
            return process.exitValue();
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }
}
