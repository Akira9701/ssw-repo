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

        String filePath = args[1];
        System.out.println("Использование:" + filePath);

        try {
            String content = readTextFromFile(filePath);
            System.out.println("Содержимое файла:");
            System.out.println(content);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }


    public static String readTextFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        BufferedWriter integerWriter = new BufferedWriter(new FileWriter("integer.txt"));
        BufferedWriter floatWriter = new BufferedWriter(new FileWriter("float.txt"));
        BufferedWriter stringWriter = new BufferedWriter(new FileWriter("string.txt"));
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