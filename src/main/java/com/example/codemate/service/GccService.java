package com.example.codemate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class GccService {

    public String compileC(MultipartFile file) {
        // 요청받은 C 파일을 /home/ubuntu/CFiles에 저장합니다.
        String cFilePath = "/home/ubuntu/CFiles/" + file.getOriginalFilename();
        try {
            Path destination = new File(cFilePath).toPath();
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save C file.";
        }

        // C 파일을 gcc로 컴파일하고 실행 결과 또는 오류 메시지를 저장합니다.
        String outputFilePath = "/home/ubuntu/Outputs/" + file.getOriginalFilename() + ".log";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("gcc", cFilePath, "-o", outputFilePath);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                // 실행이 성공한 경우, 실행 결과를 읽어옵니다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                reader.close();

                // 실행 결과를 파일에 저장합니다.
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true));
                writer.write(output.toString());
                writer.close();

                // 파일의 내용을 읽어 반환합니다.
                String fileContent = new String(Files.readAllBytes(Paths.get(outputFilePath)));
                return fileContent;
            } else {
                // 실행이 실패한 경우, 오류 메시지를 읽어옵니다.
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                StringBuilder error = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    error.append(line).append("\n");
                }
                reader.close();

                // 오류 메시지를 파일에 저장합니다.
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath, true));
                writer.write(error.toString());
                writer.close();

                // 파일의 내용을 읽어 반환합니다.
                String fileContent = new String(Files.readAllBytes(Paths.get(outputFilePath)));
                return fileContent;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Compilation failed with an exception.";
        }
    }
}
