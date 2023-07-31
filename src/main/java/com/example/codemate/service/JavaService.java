package com.example.codemate.service;

import com.example.codemate.exception.NoInputValueException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.tools.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service
public class JavaService {

    private final String javaFilesPath = "/home/ubuntu/JavaFiles/";
    public String compile(MultipartFile file, MultipartFile input) throws IOException {
        if (containsInput(file) && input.isEmpty()) {
            throw new NoInputValueException();
        }

        String filePath = javaFilesPath + file.getOriginalFilename();

        Path destination = new File(filePath).toPath();
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        String compileCommand;
        String outputFilePath = "/home/ubuntu/JavaFiles/output.txt";

        if (input.isEmpty()) {
            compileCommand = "java " + filePath + " > " + outputFilePath + " 2>&1";
        } else {
            String inputFilePath = "/home/ubuntu/JavaFiles/" + input.getOriginalFilename();
            Path destinationInput = new File(inputFilePath).toPath();
            Files.copy(input.getInputStream(), destinationInput, StandardCopyOption.REPLACE_EXISTING);

            compileCommand = "java " + filePath + " < " + inputFilePath + " > " + outputFilePath + " 2>&1";
        }

        Process process = new ProcessBuilder("bash", "-c", compileCommand).start();

        try {
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

        // 결과 반환
        return readOutputFile(outputFilePath, file.getOriginalFilename() + "\\\",");
    }

    private String readOutputFile(String filePath, String filename) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;

            while ((line = reader.readLine()) != null) {
                outputBuilder.append(removeWordFromString(line, filename)).append("\n");
            }

        } catch (IOException e) {
            throw new IOException();
        }

        return outputBuilder.toString();
    }

    public static String removeWordFromString(String input, String word) {
        int index = input.indexOf(word);
        if (index != -1) {
            return input.substring(index + word.length()).trim();
        }
        return input;
    }

    private boolean containsInput(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());

        return content.contains("InputStreamReader") || content.contains("Scanner");
    }
}
