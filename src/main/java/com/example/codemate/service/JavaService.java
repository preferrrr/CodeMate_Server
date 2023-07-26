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

        if(containsInput(file) && input.isEmpty()) // 입력받는 함수가 있는데, input file을 보내지 않은 경우.
            throw new NoInputValueException();

        String filePath = javaFilesPath + file.getOriginalFilename();
        //클라이언트에서 보낸 파일 저장.
        try {
            Path destination = new File(filePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        String compileCommand;

        String inputFilePath = "/home/ubuntu/JavaFiles/" + input.getOriginalFilename();

        if (input.isEmpty()) { // 입력받는 경우가 없으면
            compileCommand = "java " + filePath + " > /home/ubuntu/JavaFiles/output.txt 2>&1";
        }
        else { // 입력받는 경우가 있으면
            try {
                Path destination = new File(inputFilePath).toPath();
                Files.copy(input.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException();
            }
            compileCommand = "java " + filePath + " < " + inputFilePath + " > /home/ubuntu/JavaFiles/output.txt 2>&1";

        }

        String outputFilePath = "/home/ubuntu/JavaFiles/output.txt";

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

        return content.contains("InputStreamReader") || content.contains("Scanner");
    }
}
