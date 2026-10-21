package com.namesorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class NameSorter {
    public List<String> sort(List<String> names) {
        List<String> sortedNames = new ArrayList<>();
        for (int index = 0; index < names.size(); index++) {
            String name = names.get(index).strip();
            if (name.isEmpty()) {
                continue;
            }
            int components = name.split("\\s+").length;
            if (components < 2 || components > 4) {
                throw new IllegalArgumentException("Invalid name at line " + (index + 1)
                        + ": expected 1 to 3 given names and a last name.");
            }
            sortedNames.add(name);
        }
        sortedNames.sort(Comparator.comparing(NameSorter::lastName)
                .thenComparing(NameSorter::compareGivenNames));
        return sortedNames;
    }

    private static String lastName(String name) {
        String[] components = name.split("\\s+");
        return components[components.length - 1];
    }

    private static int compareGivenNames(String left, String right) {
        String[] leftParts = left.split("\\s+");
        String[] rightParts = right.split("\\s+");
        int sharedGivenNames = Math.min(leftParts.length, rightParts.length) - 1;
        for (int index = 0; index < sharedGivenNames; index++) {
            int comparison = leftParts[index].compareTo(rightParts[index]);
            if (comparison != 0) {
                return comparison;
            }
        }
        return Integer.compare(leftParts.length, rightParts.length);
    }
}
