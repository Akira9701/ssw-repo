package org.example;
import java.io.*;


public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Использование: java ReadLocalFile <путь_к_файлу>");
            return;
        }

        String currentDirectory = System.getProperty("user.dir");
        System.out.println("Текущая директория: " + currentDirectory);


        String prefix = ""; // По умолчанию префикс пустой

        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-p") && i + 1 < args.length) {
                prefix = args[i + 1]; // Извлекаем префикс
                break;
            }
        }


        String filePath = args[1];
        System.out.println("Использование:" + filePath);

        try {
            String content = readTextFromFile(filePath, prefix);
            System.out.println("Содержимое файла:");
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }


    public static String readTextFromFile(String filePath, String prefix) throws IOException {
        String integerFile = prefix + "integer.txt";
        String floatFile = prefix + "float.txt";
        String stringFile = prefix + "string.txt";

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        BufferedWriter integerWriter = new BufferedWriter(new FileWriter(integerFile));
        BufferedWriter floatWriter = new BufferedWriter(new FileWriter(floatFile));
        BufferedWriter stringWriter = new BufferedWriter(new FileWriter(stringFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String type = determineType(line);
            switch (type) {
                case "целое число":
                    integerWriter.write(line);
                    integerWriter.newLine();
                    break;
                case "вещественное число":
                    floatWriter.write(line);
                    floatWriter.newLine();
                    break;
                case "строка":
                    stringWriter.write(line);
                    stringWriter.newLine();
                    break;
            }
        }

        // Закрываем потоки
        reader.close();
        integerWriter.close();
        floatWriter.close();
        stringWriter.close();
        return "111";


    }


    public static String determineType(String str) {
        if (str.matches("-?\\d+")) {
            return "целое число";
        } else if (str.matches("-?\\d+(\\.\\d+)?")) {
            return "вещественное число";
        } else {
            return "строка";
        }
    }

}