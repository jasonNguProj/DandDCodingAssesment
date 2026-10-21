# Name sorter

Sorts UTF-8 names by last name, then given names from left to right. Requires Java 17 or newer and Maven 3.9+.

From this directory:

```sh
mvn clean test
mvn clean package
java -jar target/name-sorter.jar ./unsorted-names-list.txt
# Or use the Unix launcher:
./name-sorter ./unsorted-names-list.txt
# With this directory on PATH, the PDF's exact command also works:
PATH="$PWD:$PATH" name-sorter ./unsorted-names-list.txt
```

To run both test classes with JaCoCo coverage, use `mvn clean verify`.
Open `target/site/jacoco/index.html` in a browser for the report.
To select just these classes explicitly, use
`mvn clean verify -Dtest=NameSorterTest,NameFileServiceTest`.
The report includes all production classes; `Main` is not covered by these two unit test classes.
Coverage shows which code executed; the assertions verify the expected business behavior.
`mvn test` also runs `MainIntegrationTest`, which launches the CLI in a separate JVM.
It verifies sorted console/file output and confirms that invalid input returns status 1,
reports the original line number, and leaves the existing output file byte-for-byte unchanged.
The child JVM is not instrumented by JaCoCo, so this integration test does not increase `Main`'s reported coverage.

The application prints sorted names and overwrites `sorted-names-list.txt` in the current working directory. Failures print a message to stderr and exit with status 1. Output is written before console printing so a file failure does not print a misleading success result.

Ordering is case-sensitive: for the same surname, `Zoe Smith` sorts before `amy Smith` because Java's natural String order places uppercase Latin letters before lowercase letters.

Sample input is included. Its output is:

```text
Marin Alvarez
Adonis Julius Archer
Beau Tristan Bentley
Hunter Uriah Mathew Clarke
Leo Gardner
Vaugh Lewis
London Lindsey
Mikayla Lopez
Janet Parsons
Frankie Conner Ritter
Shelby Nathan Yoder
```

Assumptions: each nonblank line has two to four whitespace-separated components, with the final component being the last name. Blank lines are ignored; leading/trailing whitespace is stripped; internal spacing, spelling and capitalization are preserved. Comparisons use case-sensitive Java String order, without locale-specific collation. Given-name prefixes sort before longer sequences. Duplicates are retained. Invalid records fail the entire operation with the original line number, before output is written. The PDF changes `Vaugh Lewis` to `Vaughn Lewis` in its example; this implementation treats that as a typo and preserves the input.

`Main` validates arguments and coordinates the workflow and errors. `NameSorter` validates and sorts an independent list using a Comparator and Java's built-in sorting. `NameFileService` handles UTF-8 NIO file operations. Separating these responsibilities applies Single Responsibility without interfaces or frameworks that have no current purpose. Concrete collaborators keep the application easy to follow; SOLID does not require speculative abstractions.

For n names of maximum length L, sorting takes O(n log n × L) time and O(n × L) space including strings and comparison temporaries (O(n) references for the copied list and sort workspace). All names fit in memory, appropriate for the assessment's 1,000-name input. Multiword family names and culture-specific naming conventions are outside the specified format.

Tests cover surname and given-name order, all supported component counts, prefixes, duplicates, unchanged input, blank/empty input, invalid line numbers, whitespace, case, 1,000 names, UTF-8 files, overwriting, and file errors.

Execution walkthrough: `Main` checks one argument, reads lines through `NameFileService`, calls `NameSorter.sort`, writes the sorted list, then prints it. The sorter strips outer whitespace, validates component counts, and sorts by surname followed by each given name. Any file or validation error reaches Main's error handling and returns a nonzero process status.

Likely review questions:

1. **Why three classes?** File operations, sorting rules, and CLI coordination change for different reasons and can be understood independently.
2. **How are equal surnames handled?** Compare given names in order; if all shared given names match, the shorter sequence comes first.
3. **Why no custom sorting algorithm?** Java's tested standard sort already meets the requirement; the business rule belongs in the comparator.
4. **Why no interfaces?** There is only one implementation per responsibility; introducing interfaces would add navigation without a present benefit.
5. **What would change for huge files?** Reading everything would become the constraint; external sorting could be added if the scale required it.

The PDF requests a GitHub repository URL for submission. No repository is published automatically.
