package com.example.codemate.service;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.exception.FileSaveException;
import com.example.codemate.exception.InfiniteLoopException;
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

    public RunResponseDto compile(MultipartFile file, MultipartFile input) throws IOException {

//        // 입력 파일에 내용이 있어야 하는데 비어있는 경우 예외 처리
//        if (FileService.containsInput(file) && input.isEmpty()) {
//            throw new NoInputValueException();
//        }


        // 입력 파일 저장 경로 설정
        String filename = UUID.randomUUID().toString(); // 동시성 문제를 해결하기 위해서 랜덤문자열 파일명으로 저장.
        String pythonFilePath = PYTHON_DIR + filename + ".py";
        String outputFilePath = PYTHON_DIR + filename + ".txt";

        try { //파이썬 파일 저장
            Path destination = new File(pythonFilePath).toPath();
            file.transferTo(destination); //동기로 실행되기 때문에 저장이 완료되어야 다음 코드가 실행됨.
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveException();
        }

        String runCommand;

        if (input.isEmpty()) {

            runCommand = "python3 " + pythonFilePath + " > " + outputFilePath + " 2>&1";

        } else {

            String inputFilePath = PYTHON_DIR + UUID.randomUUID().toString() + ".txt";

            try {
                Path destination = new File(inputFilePath).toPath();
                input.transferTo(destination); //동기로 실행되기 때문에 저장이 완료되어야 다음 코드가 실행됨.
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileSaveException();
            }

            runCommand = "python3 " + pythonFilePath + " < " + inputFilePath + " > " + outputFilePath + " 2>&1";
        }

        int runExitCode = FileService.executeCommand(runCommand);

        if(runExitCode == -1)
            throw new InfiniteLoopException();

        String output = FileService.readOutputFile(outputFilePath, runExitCode, filename + ".py\",");

        // 결과 반환
        return RunResponseDto.builder().result(output).build();
    }

}
