package com.javarush.caesar.core;

import com.javarush.caesar.exception.CipherException;

public class BruteForce {

    public String decryptByBruteForce(String encryptedText, char[] alphabet) throws CipherException {

        Cipher cipher = new Cipher(alphabet);

        String bestDecryptedText = "";
        int bestPoint = -1;
        int bestFoundKey = -1;

        for (int key = 0; key < alphabet.length; key++) {
            String variantDecryptedText = cipher.decrypt(encryptedText, key);
            int variantScore = evaluateDecryptedText(variantDecryptedText);

            // если оценка строго больше, то возьмем этот ключ
            // или если оценка равна, но пробелов в этом тексте больше - тоже берем
            if (variantScore > bestPoint ||
                    (variantScore == bestPoint && countSpaces(variantDecryptedText) > countSpaces(bestDecryptedText))) {
                bestPoint = variantScore;
                bestDecryptedText = variantDecryptedText;
                bestFoundKey = key;
            }

        }

        System.out.println("Подобрали ключ " + bestFoundKey);
        return bestDecryptedText;
    }

    private int evaluateDecryptedText(String decryptedText) {
        int points = 0;
        char[] charsArray = decryptedText.toCharArray();

        // баллы за пробелы, так как в нормальном тексте много пробелов, они разделяют слова
        for (char letter : charsArray) {
            if (letter == ' ') {
                points += 5;
            }
        }

        // баллы за некоторые короткие слова, которые встречаются в русском тексте очень часто
        String[] words = decryptedText.split(" ");
        String[] commonUsedArray = {"и", "в", "на", "не", "с", "а", "по"};
        for (String word : words) {
            for (String commonWord : commonUsedArray) {
                if (word.equalsIgnoreCase(commonWord)) {
                    points += 10;
                }
            }
        }

        // штраф за слова без гласных, так как в русском языке не бывает слов только из согласных (кроме предлогов)
        String russianVowels = "аеёиоуыэюя";
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            boolean hasVowel = false;
            for (char c : word.toLowerCase().toCharArray()) {
                if (russianVowels.indexOf(c) >= 0) {
                    hasVowel = true;
                    break;
                }
            }
            if (!hasVowel) {
                points -= 10;
            }
        }

        // после точки или запятой в тексте всегда должен идти пробел, за это баллы
        String punctuationMarks = ".,!?";
        for (int i = 0; i < charsArray.length - 1; i++) {
            if (punctuationMarks.indexOf(charsArray[i]) >= 0 && charsArray[i + 1] == ' ') {
                points += 8;
            }
        }

        // если между двумя буквами будет идти запятая или точка, будем считать это ошибкой шифра и штрафуем
        for (int i = 1; i < charsArray.length - 1; i++) {
            if (punctuationMarks.indexOf(charsArray[i]) >= 0
                    && Character.isLetter(charsArray[i - 1])
                    && Character.isLetter(charsArray[i + 1])) {
                points -= 5;
            }
        }

        return points;
    }

    private int countSpaces(String text) {
        if (text == null) return 0;
        int count = 0;
        for (char letter : text.toCharArray()) {
            if (letter == ' ') {
                count++;
            }
        }
        return count;
    }

}
