package com.namesorter;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: name-sorter <input-file>");
            System.exit(1);
            return;
        }
        try {
            NameFileService files = new NameFileService();
            List<String> sortedNames = new NameSorter().sort(files.read(Path.of(args[0])));
            files.write(Path.of("sorted-names-list.txt"), sortedNames);
            sortedNames.forEach(System.out::println);
        } catch (IOException | InvalidPathException exception) {
            System.err.println("File error: " + exception.getMessage());
            System.exit(1);
        } catch (IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
