package org.example;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n"); // Добавляем каждую строку в StringBuilder
        }

        // Закрываем поток
        reader.close();
        return content.toString();


    }
}