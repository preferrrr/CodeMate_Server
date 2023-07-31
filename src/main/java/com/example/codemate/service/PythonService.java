package com.example.codemate.service;

import com.example.codemate.exception.NoInputValueException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PythonService {

    public String compile(MultipartFile file, MultipartFile input) throws IOException {
        // 입력 파일에 내용이 있어야 하는데 비어있는 경우 예외 처리
        if (containsInput(file) && input.isEmpty()) {
            throw new NoInputValueException();
        }

        // 입력 파일 저장 경로 설정
        String pythonFilePath = "/home/ubuntu/PythonFiles/" + file.getOriginalFilename();
        Path destination = new File(pythonFilePath).toPath();

        // 클라이언트에서 보낸 파일 저장.
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        String compileCommand;
        String outputFilePath = "/home/ubuntu/PythonFiles/output.txt";

        if (input.isEmpty()) {
            compileCommand = "python3 " + pythonFilePath + " > " + outputFilePath + " 2>&1";
        } else {
            String inputFilePath = "/home/ubuntu/PythonFiles/" + input.getOriginalFilename();
            Path destinationInput = new File(inputFilePath).toPath();
            Files.copy(input.getInputStream(), destinationInput, StandardCopyOption.REPLACE_EXISTING);

            compileCommand = "python3 " + pythonFilePath + " < " + inputFilePath + " > " + outputFilePath + " 2>&1";
        }

        Process process = new ProcessBuilder("bash", "-c", compileCommand).start();

        try {
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new IOException();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException();
        }

        // 결과 반환
        return readOutputFile(outputFilePath, file.getOriginalFilename() + "\",");
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

        return content.contains("input") || content.contains("readline()");
    }
}
