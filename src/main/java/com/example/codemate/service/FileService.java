package com.example.codemate.service;

import com.example.codemate.exception.InfiniteLoopException;
import com.example.codemate.exception.RunException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class FileService {
    public static String readOutputFile(String filePath, int exitCode, String filename) throws IOException {
        StringBuilder outputBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
            String line;
            if (exitCode == 0) {
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
            } else {
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(removeWordFromString(line, filename)).append("\n");
                }
            }

        } catch (IOException e) {
            throw new IOException();
        }

        return outputBuilder.toString();
    }

    public static int executeCommand(String command) {
        int exitCode = -1;

        long timeoutMillis = 10000l;

        try {
            Process process = new ProcessBuilder("bash", "-c", command).start();

            // 명령어 실행 시작 시간 기록
            long startTime = System.currentTimeMillis();

            while (true) {
                try {
                    exitCode = process.exitValue(); //exitValue는 프로세스가 종료되었을 때 호출 가능. 아니면 예외 발생
                    break; // 종료되었으면 예외가 발생하지 않기 때문에 여기서 break로 반복 멈추게 됨.
                } catch (IllegalThreadStateException e) {
                    // 프로세스가 종료되지 않았을 때

                    // 현재 시간과 시작 시간의 차이가 timeoutMillis보다 크면 프로세스를 강제 종료하고 종료 코드를 -1로 설정
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - startTime > timeoutMillis) {
                        process.destroy();
                        exitCode = -1;
                        break;
                    }

                    // 일정 시간 동안 기다렸다가 다시 체크
                    Thread.sleep(200); // 200밀리초 대기
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RunException();
        }
        return exitCode;
    }

    private static String removeWordFromString(String input, String word) {
        int index = input.indexOf(word);
        if (index != -1) {
            return input.substring(index + word.length()).trim();
        }
        return input;
    }

    // 파일을 메모리에 전부 올리지 않고 스트림을 이용하여 처리하는 방법으로 변경
    public static boolean containsInput(String[] inputKeywords, MultipartFile file) throws IOException {


        try (InputStream inputStream = file.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                for (String keyword : inputKeywords) {
                    if (line.contains(keyword)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
