package org.example;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Нет аргументов");
            return;
        }

        // Получаем текущую директорию
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Текущая директория: " + currentDirectory);

        // Обработка аргументов командной строки
        List<String> inputFiles = new ArrayList<>();
        String outputPath = ""; // По умолчанию пустая строка (текущая директория)
        String prefix = ""; // По умолчанию префикс пустой
        boolean appendMode = false; // По умолчанию режим перезаписи
        String statistic = ""; // По умолчанию статистика не выводится

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o") && i + 1 < args.length) {
                outputPath = args[i + 1]; // Извлекаем путь
                i++; // Пропускаем следующий аргумент (путь)
            } else if (args[i].equals("-p") && i + 1 < args.length) {
                prefix = args[i + 1]; // Извлекаем префикс
                i++; // Пропускаем следующий аргумент (префикс)
            } else if (args[i].equals("-a")) {
                appendMode = true; // Включаем режим добавления
            } else if (args[i].equals("-s")) {
                statistic = "simple"; // Включаем краткую статистику
            } else if (args[i].equals("-f")) {
                statistic = "full"; // Включаем полную статистику
            } else if (!args[i].startsWith("-")) {
                inputFiles.add(args[i]); // Добавляем путь к файлу
            }
        }

        // Проверка наличия входных файлов
        if (inputFiles.isEmpty()) {
            System.out.println("Ошибка: не указаны входные файлы.");
            return;
        }

        // Сбор данных и распределение по файлам
        List<String> integers = new ArrayList<>();
        List<String> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String filePath : inputFiles) {
            try {
                readTextFromFile(filePath, outputPath, prefix, appendMode, integers, floats, strings);
            } catch (IOException e) {
                System.out.println("Ошибка при обработке файла " + filePath + ": " + e.getMessage());
            }
        }

        // Вывод статистики
        if (statistic.equals("simple")) {
            printShortStatistics(integers, floats, strings);
        } else if (statistic.equals("full")) {
            printFullStatistics(integers, floats, strings);
        }
    }


    public static void readTextFromFile(String filePath, String outputPath, String prefix, boolean appendMode,
                                        List<String> integers, List<String> floats, List<String> strings) throws IOException {
        // Формируем имена файлов с учетом пути и префикса
        String integerFile = outputPath + File.separator + prefix + "integers.txt";
        String floatFile = outputPath + File.separator + prefix + "floats.txt";
        String stringFile = outputPath + File.separator + prefix + "strings.txt";

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        BufferedWriter integerWriter = null;
        BufferedWriter floatWriter = null;
        BufferedWriter stringWriter = null;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim(); // Удаляем лишние пробелы
            if (line.isEmpty()) continue; // Пропускаем пустые строки

            String type = determineType(line);

            // Записываем строку в соответствующий файл и список
            switch (type) {
                case "целое число":
                    if (integerWriter == null) {
                        integerWriter = new BufferedWriter(new FileWriter(integerFile, appendMode));
                    }
                    integerWriter.write(line);
                    integerWriter.newLine();
                    integers.add(line);
                    break;
                case "вещественное число":
                    if (floatWriter == null) {
                        floatWriter = new BufferedWriter(new FileWriter(floatFile, appendMode));
                    }
                    floatWriter.write(line);
                    floatWriter.newLine();
                    floats.add(line);
                    break;
                case "строка":
                    if (stringWriter == null) {
                        stringWriter = new BufferedWriter(new FileWriter(stringFile, appendMode));
                    }
                    stringWriter.write(line);
                    stringWriter.newLine();
                    strings.add(line);
                    break;
            }
        }

        // Закрываем все потоки
        reader.close();
        if (integerWriter != null) integerWriter.close();
        if (floatWriter != null) floatWriter.close();
        if (stringWriter != null) stringWriter.close();
    }

    /**
     * Метод для определения типа строки.
     *
     * @param str Входная строка.
     * @return Тип строки: "целое число", "вещественное число" или "строка".
     */
    public static String determineType(String str) {
        if (str.matches("-?\\d+")) {
            return "целое число";
        } else if (str.matches("-?\\d+(\\.\\d+)?")) {
            return "вещественное число";
        } else {
            return "строка";
        }
    }

    /**
     * Выводит краткую статистику.
     *
     * @param integers Список целых чисел.
     * @param floats   Список вещественных чисел.
     * @param strings  Список строк.
     */
    public static void printShortStatistics(List<String> integers, List<String> floats, List<String> strings) {
        System.out.println("Краткая статистика:");
        System.out.println("Целые числа: " + integers.size());
        System.out.println("Вещественные числа: " + floats.size());
        System.out.println("Строки: " + strings.size());
    }

    /**
     * Выводит полную статистику.
     *
     * @param integers Список целых чисел.
     * @param floats   Список вещественных чисел.
     * @param strings  Список строк.
     */
    public static void printFullStatistics(List<String> integers, List<String> floats, List<String> strings) {
        System.out.println("Полная статистика:");

        // Статистика для целых чисел
        if (!integers.isEmpty()) {
            int minInt = integers.stream().mapToInt(Integer::parseInt).min().orElse(0);
            int maxInt = integers.stream().mapToInt(Integer::parseInt).max().orElse(0);
            int sumInt = integers.stream().mapToInt(Integer::parseInt).sum();
            double avgInt = integers.stream().mapToInt(Integer::parseInt).average().orElse(0);

            System.out.println("Целые числа:");
            System.out.println("  Количество: " + integers.size());
            System.out.println("  Минимальное: " + minInt);
            System.out.println("  Максимальное: " + maxInt);
            System.out.println("  Сумма: " + sumInt);
            System.out.println("  Среднее: " + avgInt);
        } else {
            System.out.println("Целые числа: нет данных");
        }

        // Статистика для вещественных чисел
        if (!floats.isEmpty()) {
            double minFloat = floats.stream().mapToDouble(Double::parseDouble).min().orElse(0);
            double maxFloat = floats.stream().mapToDouble(Double::parseDouble).max().orElse(0);
            double sumFloat = floats.stream().mapToDouble(Double::parseDouble).sum();
            double avgFloat = floats.stream().mapToDouble(Double::parseDouble).average().orElse(0);

            System.out.println("Вещественные числа:");
            System.out.println("  Количество: " + floats.size());
            System.out.println("  Минимальное: " + minFloat);
            System.out.println("  Максимальное: " + maxFloat);
            System.out.println("  Сумма: " + sumFloat);
            System.out.println("  Среднее: " + avgFloat);
        } else {
            System.out.println("Вещественные числа: нет данных");
        }

        // Статистика для строк
        if (!strings.isEmpty()) {
            int minLength = strings.stream().mapToInt(String::length).min().orElse(0);
            int maxLength = strings.stream().mapToInt(String::length).max().orElse(0);

            System.out.println("Строки:");
            System.out.println("  Количество: " + strings.size());
            System.out.println("  Самая короткая строка: " + minLength + " символов");
            System.out.println("  Самая длинная строка: " + maxLength + " символов");
        } else {
            System.out.println("Строки: нет данных");
        }
    }
}