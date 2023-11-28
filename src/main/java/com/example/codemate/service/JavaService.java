package com.example.codemate.service;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.exception.FileSaveException;
import com.example.codemate.exception.InfiniteLoopException;
import com.example.codemate.exception.NoInputValueException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class JavaService {

    private static final String JAVAFILES_DIR = "/home/ubuntu/JavaFiles/";

    public RunResponseDto compile(MultipartFile file, MultipartFile input) throws IOException {

        //객체 선언만 해두거나 import만 해두면 입력값이 필요하지 않은데도 예외가 생길 수 있음.
//        final String[] inputKeywords = {"InputStreamReader", "Scanner", "BufferedReader"};
//
//        if (FileService.containsInput(inputKeywords, file) && input.isEmpty()) {//사용자 입력값이 필요한데 없으면 예외
//            throw new NoInputValueException();
//        }
        /**프로세스의 실행 시간이 10초가 지나면 종료시키므로써, 입력값이 필요하지만 없을 때의 예외가 처리됨.*/


        String filename = UUID.randomUUID().toString();
        String javaFilePath = JAVAFILES_DIR + filename + ".java";
        String outputFilePath = JAVAFILES_DIR + filename + ".txt";

        //서버에 파일 저장, copy-> transferTo로 변경
        try {
            Path destination = new File(javaFilePath).toPath();
            file.transferTo(destination); //동기로 실행되기 때문에 저장이 완료되어야 다음 코드가 실행됨.
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveException();
        }


        String runCommand;

        if (input.isEmpty()) {

            runCommand = "java " + javaFilePath + " > " + outputFilePath + " 2>&1";

        } else {

            String inputPath = JAVAFILES_DIR + UUID.randomUUID().toString() + ".txt";

            try {
                Path destination = new File(inputPath).toPath();
                input.transferTo(destination);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileSaveException();
            }

            runCommand = "java " + javaFilePath + " < " + inputPath + " > " + outputFilePath + " 2>&1";

        }

        int runExitCode = FileService.executeCommand(runCommand);

        if(runExitCode == -1)
            throw new InfiniteLoopException();

        String output = FileService.readOutputFile(outputFilePath, runExitCode, filename + ".java:");

        // 결과 반환
        return RunResponseDto.builder().result(output).build();
    }

}
