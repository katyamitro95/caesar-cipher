package com.javarush.caesar.service;

import com.javarush.caesar.exception.CipherException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager {

    public String readFile(String filePath) throws CipherException {
        try {
            Path path = Path.of(filePath);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CipherException("Возникла ошибка при чтения файла: " + filePath, e);
        }
    }

    public void writeFile(String content, String filePath) throws CipherException {
        try {
            Path path = Path.of(filePath);
            Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new CipherException("Возникла ошибка при записи в файл (возможно, он уже существует): " + filePath, e);
        }
    }
}
