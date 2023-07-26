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

        if(containsInput(file) && input.isEmpty())
            throw new NoInputValueException();

        String pythonFilePath = "/home/ubuntu/PythonFiles/" + file.getOriginalFilename();
        String inputFilePath = "/home/ubuntu/PythonFiles/" + input.getOriginalFilename();
        //클라이언트에서 보낸 파일 저장.
        try {
            Path destination = new File(pythonFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        String compileCommand;


        if (input.isEmpty()) {
            compileCommand = "python3 " + pythonFilePath + " > /home/ubuntu/PythonFiles/output.txt 2>&1";
        } else {
            try {
                Path destination = new File(inputFilePath).toPath();
                Files.copy(input.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
            compileCommand = "python3 " + pythonFilePath + " < " + inputFilePath + " > /home/ubuntu/PythonFiles/output.txt 2>&1";

        }

        String outputFilePath = "/home/ubuntu/PythonFiles/output.txt";

        Process process = new ProcessBuilder("bash", "-c", compileCommand).start();


        String output = readOutputFile(outputFilePath, file.getOriginalFilename() + "\\\",");

        // 결과 반환
        return output;

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
