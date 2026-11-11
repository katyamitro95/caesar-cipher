package com.javarush.caesar;

import com.javarush.caesar.core.BruteForce;
import com.javarush.caesar.core.Cipher;
import com.javarush.caesar.exception.CipherException;
import com.javarush.caesar.service.FileManager;
import com.javarush.caesar.service.Validator;

import java.util.Scanner;

public class MainApp {

    private static final char[] ALPHABET = {
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п',
            'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',
            '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '
    };

    private final Scanner scanner = new Scanner(System.in);
    private final FileManager fileManager = new FileManager();
    private final Validator validator = new Validator();
    private final Cipher cipher = new Cipher(ALPHABET);
    private final BruteForce bruteForce = new BruteForce();

    public static void main(String[] args) {
        new MainApp().run();
    }

    private void run() {
        System.out.println("Шифр Цезаря: шифрование и расшифровка файлов");
        while (true) {
            printMenu();
            String choice = ask("Выберите действие: ");
            try {
                switch (choice) {
                    case "1":
                        encryptFile();
                        break;
                    case "2":
                        decryptFile();
                        break;
                    case "3":
                        bruteForceFile();
                        break;
                    case "0":
                    case "exit":
                        System.out.println("До свидания!");
                        return;
                    default:
                        System.out.println("Нет такого пункта: " + choice);
                }
            } catch (CipherException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Зашифровать файл");
        System.out.println("2. Расшифровать файл с ключом");
        System.out.println("3. Взломать перебором ключей");
        System.out.println("0. Выход (или exit)");
    }

    private void encryptFile() throws CipherException {
        String inputFile = ask("Введите путь к исходному файлу: ");
        if (!validator.isFileExists(inputFile)) {
            throw new CipherException("Файл не был найден или является папкой: " + inputFile);
        }

        String outputFile = ask("Введите путь к файлу для результата: ");
        if (outputFile == null || outputFile.isBlank()) {
            throw new CipherException("Путь к выходному файлу не может быть пустым!");
        }
        int key = readValidKey();

        String inputText = fileManager.readFile(inputFile);
        String encryptedText = cipher.encrypt(inputText, key);
        fileManager.writeFile(encryptedText, outputFile);

        System.out.println("Текст файла был успешно зашифрован и сохранен в: " + outputFile);
    }

    private void decryptFile() throws CipherException {
        String inputFile = ask("Введите путь к зашифрованному файлу: ");
        if (!validator.isFileExists(inputFile)) {
            throw new CipherException("Файл не был найден или является папкой: " + inputFile);
        }

        String outputFile = ask("Введите путь к файлу для результата: ");
        if (outputFile == null || outputFile.isBlank()) {
            throw new CipherException("Путь к выходному файлу не может быть пустым!");
        }
        int key = readValidKey();

        String inputText = fileManager.readFile(inputFile);
        String decryptedText = cipher.decrypt(inputText, key);
        fileManager.writeFile(decryptedText, outputFile);

        System.out.println("Текст файла был успешно расшифрован и сохранен в: " + outputFile);
    }

    private void bruteForceFile() throws CipherException {
        String inputFile = ask("Введите путь к зашифрованному файлу: ");
        if (!validator.isFileExists(inputFile)) {
            throw new CipherException("Файл не был найден или является папкой: " + inputFile);
        }

        String outputFile = ask("Введите путь к файлу для результата: ");
        if (outputFile == null || outputFile.isBlank()) {
            throw new CipherException("Путь к выходному файлу не может быть пустым!");
        }

        String inputText = fileManager.readFile(inputFile);
        String decryptedText = bruteForce.decryptByBruteForce(inputText, ALPHABET);
        fileManager.writeFile(decryptedText, outputFile);

        System.out.println("Текст файла был успешно взломан и сохранен в: " + outputFile);

    }

    private int readValidKey() {
        while (true) {
            String strKey = ask("Введите ключ (целое число от 0 до " + (ALPHABET.length - 1) + "): ");
            try {
                int key = Integer.parseInt(strKey);
                if (validator.isValidKey(key, ALPHABET)) {
                    return key;
                } else {
                    System.out.println("Ошибка: ключ вне допустимого диапазона.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите именно число.");
            }
        }
    }

    private String ask(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
