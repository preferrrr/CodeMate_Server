package com.example.codemate.service;

import com.example.codemate.dto.RunResponseDto;
import com.example.codemate.exception.FileSaveException;
import com.example.codemate.exception.InfiniteLoopException;
import com.example.codemate.exception.NoInputValueException;
import com.example.codemate.exception.RunException;
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

    public RunResponseDto compile(MultipartFile file, MultipartFile input) throws IOException {

//        final String[] inputKeywords = {"scanf", "getchar", "fgets", "gets"};
//
//        if (FileService.containsInput(inputKeywords, file) && input.isEmpty()) //사용자 입력값을 받는 함수가 있는데 input file이 없으면 예외
//            throw new NoInputValueException();
        /**프로세스의 실행 시간이 10초가 지나면 종료시키므로써, 입력값이 필요하지만 없을 때의 예외가 처리됨.*/

        String filename = UUID.randomUUID().toString(); // 동시성 문제를 해결하기 위해서 랜덤문자열 파일명으로 저장.
        String cFilePath = CFILES_DIR + filename + ".c";
        String outputFilePath = CFILES_DIR + filename + ".txt";

        //서버에 파일 저장, copy-> transferTo로 변경
        try {
            Path destination = new File(cFilePath).toPath();
            file.transferTo(destination); //동기로 실행되기 때문에 저장이 완료되어야 다음 코드가 실행됨.
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileSaveException();
        }

        String compileCommand = "gcc " + cFilePath + " -o " + CFILES_DIR + filename + " 2> " + outputFilePath;

        int compileExitCode = FileService.executeCommand(compileCommand); // 컴파일 실행

        if (compileExitCode == 0) { //컴파일 성공했을 때
            String runCommand;
            if (input.isEmpty()) {// input 있을 때, 입력함수 없는데 input 넣어서 실행해도 괜찮음.

                runCommand = CFILES_DIR + "./" + filename + " > " + outputFilePath;

            } else {
                String inputPath = CFILES_DIR + UUID.randomUUID().toString() + ".txt";

                try {
                    Path destination = new File(inputPath).toPath();
                    input.transferTo(destination);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileSaveException();
                }

                runCommand = CFILES_DIR + "./" + filename + " < " + inputPath + " > " + outputFilePath;
            }

            int runExitCode = FileService.executeCommand(runCommand);

            if(runExitCode == -1)
                throw new InfiniteLoopException();

        }

        //컴파일에 성공했으면 결과, 컴파일 실패했으면 오류 메세지
        String output = FileService.readOutputFile(outputFilePath, compileExitCode, filename + ".c:");

        return RunResponseDto.builder().result(output).build();

    }




}
