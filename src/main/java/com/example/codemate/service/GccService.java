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
public class GccService {

    private static final String CFILES_DIR = "/home/ubuntu/CFiles/";

    public String compile(MultipartFile file, MultipartFile input) throws IOException {


        if (containsInput(file) && input.isEmpty())
            throw new NoInputValueException();


        String filename = UUID.randomUUID().toString(); // 동시성 문제를 해결하기 위해서 랜덤문자열 파일명으로 저장.
        String cFilePath = CFILES_DIR + filename + ".c";

        //서버에 파일 저장
        try {
            Path destination = new File(cFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        String outputFilePath = CFILES_DIR + filename + ".txt";
        String compileCommand = "gcc " + cFilePath + " -o " + CFILES_DIR + filename + " 2> " + outputFilePath;
        int exitCode = executeCommand(compileCommand);

        if (exitCode == 0) {
            String runCommand;
            if (input.isEmpty()) {
                runCommand = CFILES_DIR + "./" + filename + " > " + outputFilePath;
            } else {
                String inputPath = CFILES_DIR + UUID.randomUUID().toString() + ".txt";
                try {
                    Path destination = new File(inputPath).toPath();
                    Files.copy(input.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException();
                }
                runCommand = CFILES_DIR + "./" + filename + " < " + inputPath + " > " + outputFilePath;
            }
            executeCommand(runCommand);

        }

        String output = readOutputFile(outputFilePath, exitCode, filename);

        return output;

    }


    private String readOutputFile(String filePath, int exitCode, String filename) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            if (exitCode == 0) {
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(removeWordFromString(line, filename + ".c:")).append("\n");
                }
            }

        } catch (IOException e) {
            throw new IOException();
        }

        return outputBuilder.toString();
    }

    private int executeCommand(String command) {
        int exitCode = -1;
        try {
            Process process = new ProcessBuilder("bash", "-c", command).start();
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return exitCode;
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
        boolean result = content.contains("scanf") || content.contains("getchar")
                || content.contains("fgets") || content.contains("gets");

        return result;
    }

}
