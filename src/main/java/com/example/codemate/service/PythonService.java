package com.example.codemate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class PythonService {

    public String compile(MultipartFile file) throws IOException {
        String pythonFilePath = "/home/ubuntu/PythonFiles/" + file.getOriginalFilename();

        //클라이언트에서 보낸 파일 저장.
        try {
            Path destination = new File(pythonFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }


        String outputFilePath = "/home/ubuntu/PythonFiles/output.txt";
        String compileCommand = "python3 " + pythonFilePath + " > /home/ubuntu/PythonFiles/output.txt 2>&1";

        Process process = new ProcessBuilder("bash", "-c", compileCommand).start();



        String output = readOutputFile(outputFilePath); //TODO: 파일 이름 바꾸기

        // 결과 반환
        return output;

        //TODO: 에러메세지일 경우 :까지 자르기
    }

    private String readOutputFile(String filePath) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;

            while ((line = reader.readLine()) != null) {
                outputBuilder.append(line).append("\n");
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
}
