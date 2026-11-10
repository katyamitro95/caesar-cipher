package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

import java.util.HashMap;
import java.util.Map;


public class Cipher {

    private final char[] alphabet;
    private final Map<Character, Integer> referenceMap;

    public Cipher(char[] alphabet) {
        this.alphabet = alphabet;
        this.referenceMap = new HashMap<>();
        for (int i = 0; i < alphabet.length; i++) {
            referenceMap.put(alphabet[i], i);
        }
    }

    public String encrypt(String text, int shift) throws CipherException {
        if (text == null || text.isEmpty()) {
            throw new CipherException("Текст для шифрования не может быть пустым или null");
        }

        int size = alphabet.length;
        if (shift < 0 || shift >= size) {
            throw new CipherException("Ключ должен быть в диапазоне от 0 до " + (size - 1));
        }

        StringBuilder strinBuid = new StringBuilder(text.length());
        for (char letter : text.toCharArray()) {
            Integer index = referenceMap.get(Character.toLowerCase(letter));
            if (index == null) {
                strinBuid.append(letter);
            } else {
                strinBuid.append(alphabet[(index + shift) % size]);
            }
        }
        return strinBuid.toString();
    }

    public String decrypt(String encryptedText, int shift) throws CipherException {
        if (encryptedText == null || encryptedText.isEmpty()) {
            throw new CipherException("Текст для расшифровки не может быть пустым или null");
        }

        int size = alphabet.length;
        if (shift < 0 || shift >= size) {
            throw new CipherException("Ключ должен быть в диапазоне от 0 до " + (size - 1));
        }

        StringBuilder strinBuid = new StringBuilder(encryptedText.length());
        for (char c : encryptedText.toCharArray()) {
            Integer index = referenceMap.get(Character.toLowerCase(c));
            if (index == null) {
                throw new CipherException("При расшифровке нашелся символ вне алфавита: '" + c + "'");
            } else {
                int newIndex = (index - shift + size) % size;
                strinBuid.append(alphabet[newIndex]);
            }
        }
        return strinBuid.toString();
    }
}
