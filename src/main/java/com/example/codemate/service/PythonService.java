package com.example.codemate.service;

import com.example.codemate.exception.NoInputValueException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class PythonService {

    private static final String PYTHON_DIR = "/home/ubuntu/PythonFiles/";

    public String compile(MultipartFile file, MultipartFile input) throws IOException {
        // 입력 파일에 내용이 있어야 하는데 비어있는 경우 예외 처리
        if (containsInput(file) && input.isEmpty()) {
            throw new NoInputValueException();
        }


        // 입력 파일 저장 경로 설정
        String filename = UUID.randomUUID().toString(); // 동시성 문제를 해결하기 위해서 랜덤문자열 파일명으로 저장.
        String pythonFilePath = PYTHON_DIR + filename + ".py";
        Path destination = new File(pythonFilePath).toPath();

        // 클라이언트에서 보낸 파일 저장.
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        String compileCommand;
        String outputFilePath = PYTHON_DIR + filename + ".txt";

        if (input.isEmpty()) {
            compileCommand = "python3 " + pythonFilePath + " > " + outputFilePath + " 2>&1";
        } else {
            String inputFilePath = PYTHON_DIR + UUID.randomUUID().toString() + ".txt";
            Path destinationInput = new File(inputFilePath).toPath();
            Files.copy(input.getInputStream(), destinationInput, StandardCopyOption.REPLACE_EXISTING);

            compileCommand = "python3 " + pythonFilePath + " < " + inputFilePath + " > " + outputFilePath + " 2>&1";
        }

        Process process = new ProcessBuilder("bash", "-c", compileCommand).start();

        try {
            int exitCode = process.waitFor();

        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new IOException();
        }

        // 결과 반환
        return readOutputFile(outputFilePath, filename + ".py\\");
    }

    private String readOutputFile(String filePath, String filename) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {

            String line = reader.readLine();
            outputBuilder.append(removeWordFromString(line, filename)).append("\n");

            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append("\n");
            }

        } catch (IOException e) {
            throw new IOException();
        }

        return outputBuilder.toString();
    }

    public static String removeWordFromString(String input, String word) {
        int index = input.indexOf("line");
        if (index != -1) {
            return input.substring(index, input.length()).trim();
        }
        return input;
    }

    private boolean containsInput(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());

        return content.contains("input") || content.contains("readline()");
    }
}
