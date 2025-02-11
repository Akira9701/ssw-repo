package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testDetermineType() {
        assertEquals("целое число", Main.determineType("123"));
        assertEquals("целое число", Main.determineType("-456"));
        assertEquals("вещественное число", Main.determineType("123.45"));
        assertEquals("вещественное число", Main.determineType("-123.45"));
        assertEquals("строка", Main.determineType("abc"));
        assertEquals("строка", Main.determineType("123abc"));
    }

    @Test
    void testReadTextFromFile(@TempDir Path tempDir) throws IOException {
        // Create a temporary input file
        Path inputFile = tempDir.resolve("input.txt");
        Files.write(inputFile, List.of("123", "45.67", "hello", "-89", "world"));

        // Prepare output directory and prefix
        String outputPath = tempDir.toString();
        String prefix = "test_";

        // Lists to store results
        List<String> integers = new ArrayList<>();
        List<String> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        // Call the method
        Main.readTextFromFile(inputFile.toString(), outputPath, prefix, false, integers, floats, strings);

        // Verify the results
        assertEquals(List.of("123", "-89"), integers);
        assertEquals(List.of("45.67"), floats);
        assertEquals(List.of("hello", "world"), strings);

        // Verify the output files
        assertTrue(Files.exists(tempDir.resolve(prefix + "integers.txt")));
        assertTrue(Files.exists(tempDir.resolve(prefix + "floats.txt")));
        assertTrue(Files.exists(tempDir.resolve(prefix + "strings.txt")));

        // Verify the content of the output files
        assertEquals(List.of("123", "-89"), Files.readAllLines(tempDir.resolve(prefix + "integers.txt")));
        assertEquals(List.of("45.67"), Files.readAllLines(tempDir.resolve(prefix + "floats.txt")));
        assertEquals(List.of("hello", "world"), Files.readAllLines(tempDir.resolve(prefix + "strings.txt")));
    }

    @Test
    void testPrintShortStatistics() {
        List<String> integers = List.of("1", "2", "3");
        List<String> floats = List.of("1.5", "2.5");
        List<String> strings = List.of("a", "bb", "ccc");

        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Main.printShortStatistics(integers, floats, strings);

        String expectedOutput = """
                Краткая статистика:
                Целые числа: 3
                Вещественные числа: 2
                Строки: 3
                """;
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testPrintFullStatistics() {
        List<String> integers = List.of("1", "2", "3");
        List<String> floats = List.of("1.5", "2.5");
        List<String> strings = List.of("a", "bb", "ccc");

        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Main.printFullStatistics(integers, floats, strings);

        String expectedOutput = """
                Полная статистика:
                Целые числа:
                  Количество: 3
                  Минимальное: 1
                  Максимальное: 3
                  Сумма: 6
                  Среднее: 2.0
                Вещественные числа:
                  Количество: 2
                  Минимальное: 1.5
                  Максимальное: 2.5
                  Сумма: 4.0
                  Среднее: 2.0
                Строки:
                  Количество: 3
                  Самая короткая строка: 1 символов
                  Самая длинная строка: 3 символов
                """;
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testMainWithNoArguments() {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Main.main(new String[]{});

        assertEquals("Нет аргументов\n", outputStream.toString());
    }

    @Test
    void testMainWithInvalidFile(@TempDir Path tempDir) {
        // Redirect System.out to capture output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        String invalidFilePath = tempDir.resolve("nonexistent.txt").toString();
        Main.main(new String[]{invalidFilePath});

        assertTrue(outputStream.toString().contains("Ошибка при обработке файла " + invalidFilePath));
    }
}