package com.namesorter;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NameSorterTest {
    private final NameSorter sorter = new NameSorter();

    @Test
    void sortsByLastNameBeforeGivenNamesWithoutChangingInput() {
        List<String> input = new ArrayList<>(List.of("Amy Zebra", "Zoe Archer", "Bob Brown"));
        assertEquals(List.of("Zoe Archer", "Bob Brown", "Amy Zebra"), sorter.sort(input));
        assertEquals(List.of("Amy Zebra", "Zoe Archer", "Bob Brown"), input);
    }

    @Test
    void comparesGivenNamesInSequenceAndPlacesPrefixesFirst() {
        assertEquals(List.of("Amy Smith", "Amy Ann Smith", "Amy Ann Beth Smith",
                        "Amy Ann Zoe Smith", "Amy Bob Smith", "Zoe Smith"),
                sorter.sort(List.of("Zoe Smith", "Amy Bob Smith", "Amy Ann Zoe Smith",
                        "Amy Ann Beth Smith", "Amy Ann Smith", "Amy Smith")));
    }

    @Test
    void retainsDuplicatesAndAlreadySortedNames() {
        List<String> names = List.of("Amy Archer", "Amy Archer", "Zoe Brown");
        assertEquals(names, sorter.sort(names));
    }

    @Test
    void acceptsEmptyInputAndIgnoresBlankLines() {
        assertEquals(List.of(), sorter.sort(List.of()));
        assertEquals(List.of(), sorter.sort(List.of("", "  ", "\t")));
    }

    @Test
    void stripsOuterWhitespaceAndPreservesInternalSpacingAndSpelling() {
        assertEquals(List.of("Vaugh  Lewis", "Amy\tSmith"),
                sorter.sort(List.of(" Amy\tSmith ", " Vaugh  Lewis ")));
    }

    @Test
    void reportsInvalidNamesWithOriginalLineNumbers() {
        for (String invalid : List.of("Prince", "One Two Three Four Five")) {
            IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                    () -> sorter.sort(List.of("", "Amy Smith", invalid)));
            assertTrue(error.getMessage().contains("line 3"));
        }
    }

    @Test
    void usesCaseSensitiveNaturalStringOrder() {
        assertEquals(List.of("Zoe Smith", "amy Smith", "Amy smith"),
                sorter.sort(List.of("Amy smith", "amy Smith", "Zoe Smith")));
    }

    @Test
    void sortsOneThousandNames() {
        List<String> input = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        for (int index = 999; index >= 0; index--) {
            input.add("Given Surname" + String.format("%04d", index));
        }
        for (int index = 0; index < 1000; index++) {
            expected.add("Given Surname" + String.format("%04d", index));
        }
        assertEquals(expected, sorter.sort(input));
    }
}
