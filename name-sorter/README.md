# Name Sorter

A Java command-line application that sorts names alphabetically by last name, followed by given names.

Built with Java 17 and Maven.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.9+

### Build

```bash
mvn clean package
```

### Run

```bash
java -jar target/name-sorter.jar ./unsorted-names-list.txt
```

Alternatively, use the provided Unix launcher:

```bash
./name-sorter ./unsorted-names-list.txt
```

The application prints the sorted names to the console and creates or overwrites `sorted-names-list.txt` in the current working directory.

## Example

**Input (`unsorted-names-list.txt`)**

```text
Janet Parsons
Adonis Julius Archer
Marin Alvarez
Beau Tristan Bentley
```

**Output (`sorted-names-list.txt`)**

```text
Marin Alvarez
Adonis Julius Archer
Beau Tristan Bentley
Janet Parsons
```

## Testing

Run the automated unit and integration tests:

```bash
mvn clean test
```

To generate a JaCoCo coverage report:

```bash
mvn clean verify
```

The report is available at `target/site/jacoco/index.html`.

Tests cover sorting rules, name validation, file operations, and command-line execution.

## Design

The application separates responsibilities across three classes:

- **Main:** Handles command-line arguments and coordinates execution.
- **NameSorter:** Validates names and sorts them using Java's `Comparator`.
- **NameFileService:** Handles UTF-8 file reading and writing.

## Assumptions

- Each name contains one to three given names and one last name.
- Sorting is case-sensitive, using Java's natural string ordering.
- Blank lines are ignored, and surrounding whitespace is removed.
- Duplicate names are preserved.
- Invalid names produce an error identifying the original input line.
- File errors result in a nonzero exit status.