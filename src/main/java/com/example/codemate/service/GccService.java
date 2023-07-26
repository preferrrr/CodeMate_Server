package com.example.codemate.service;

import com.example.codemate.exception.NoInputValueException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@Service
public class GccService {

    public String compile(MultipartFile file, MultipartFile input) throws IOException {


        if(containsInput(file) && input.isEmpty())
            throw new NoInputValueException();

        String cFilePath = "/home/ubuntu/CFiles/" + file.getOriginalFilename();

        //서버에 파일 저장
        try {
            Path destination = new File(cFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
        String inputPath = "/home/ubuntu/CFiles/" + input.getOriginalFilename();

        String outputFilePath = "/home/ubuntu/CFiles/output.txt";
        String compileCommand = "gcc " + cFilePath + " -o /home/ubuntu/CFiles/output 2> " + outputFilePath;
        int exitCode = executeCommand(compileCommand);

        if (exitCode == 0) {
            String runCommand;
            if (input.isEmpty()) {
                runCommand = "/home/ubuntu/CFiles/./output > " + outputFilePath;
            } else {
                try {
                    Path destination = new File(inputPath).toPath();
                    Files.copy(input.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException();
                }
                runCommand = "/home/ubuntu/CFiles/./output < " + inputPath + " > " + outputFilePath;
            }
            executeCommand(runCommand);

        }

        String output = readOutputFile(outputFilePath, exitCode, file.getOriginalFilename());

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
                    outputBuilder.append(removeWordFromString(line, filename + ":")).append("\n");
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
        System.out.println(content);
        boolean result = content.contains("scanf") || content.contains("getchar")
                || content.contains("fgets") || content.contains("gets");

        if(result)
            System.out.println("true");
        else System.out.println("false");

        return result;
    }

}
