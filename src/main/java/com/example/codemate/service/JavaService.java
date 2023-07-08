package com.example.codemate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class JavaService {


    public String compile(MultipartFile file) throws IOException {

        // 요청받은 C 파일을 /home/ubuntu/JavaFiles에 저장합니다.
        String javaFilePath = "/home/ubuntu/JavaFiles/" + file.getOriginalFilename();
        try {
            Path destination = new File(javaFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }

        String outputFilePath = "/home/ubuntu/JavaFiles/output.txt";
        String compileCommand = "javac " + javaFilePath + " 2> " + outputFilePath;
        int exitCode = executeCommand(compileCommand);

        System.out.println("exit code : " + exitCode);

        //TODO: output을 파일 이름으로 나중에 바꾸기
        if (exitCode == 0) {
            // 실행 파일 실행
            String runCommand = "java /home/ubuntu/JavaFiles/ " + file.getOriginalFilename().substring(0, file.getOriginalFilename().length() - 6);
            executeCommand(runCommand);
        }

        String output = readOutputFile(outputFilePath, exitCode, file.getOriginalFilename()); //TODO: 파일 이름 바꾸기

        // 결과 반환
        return output;

        //TODO: 에러메세지일 경우 :까지 자르기

    }


    private String readOutputFile(String filePath, int exitCode, String filename) throws IOException {
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


}
