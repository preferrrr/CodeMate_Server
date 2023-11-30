# CodeMate
<div align="center" >

</br>

![메인 화면](https://github.com/preferrrr/CodeMate_Server/assets/99793526/f3ae2a41-21ff-4ae0-a9fa-612d8ca834e9)
</div>

<div align="center" >
</br>
CodeMate는 웹에서 코드를 작성하고 실행할 수 있는 크롬 확장 프로그램입니다. c, cpp, python, java를 지원하며, 사용자 편의성을 위해 OCR 기능과 AI 질문 기능을 지원합니다.
</br>
</br>
</div>

# 아키텍쳐
<div align="center" >

![아키텍쳐](https://github.com/preferrrr/CodeMate_Server/assets/99793526/26eb62cb-8171-4438-91cb-f477e6f182c5)

</br>

</div>

### 코드 실행

1. 클라이언트는 사용자의 코드와 입력을 파일로 웹 서버에 보냅니다.
2. 웹 서버는 서버에 파일을 저장 후, 각 언어에 맞는 명령어로 코드를 실행합니다.
3. 결과 텍스트 파일을 읽어서 클라이언트에게 반환합니다.


### OCR
1. 클라이언트는 이미지 파일을 웹 서버에 보냅니다.
2. 웹 서버는 오픈 소스 tesseract를 사용하여 문자 인식 후 결과를 반환합니다.

### AI 질문
1. 클라이언트는 질문을 웹 서버에 보냅니다.
2. 웹 서버는 ChatGPT API를 호출하여 질문에 대한 답을 받은 후, 클라이언트에게 반환합니다.

### 다수의 사용자들은 동시에 코드 실행 요청을 할 수도 있습니다. 
- `파일명을 random uuid를 사용하여 동시에 같은 파일을 read write 하는 경우가 일어나지 않도록 합니다.`

### 사용자의 입력이 필요한데 입력값이 없어서 프로세스가 입력을 계속 기다리는 경우, 무한 루프에 빠진 경우 이 프로세스를 종료해야 합니다.
- `10초의 시간 제한을 두고 10초 안에 프로세스가 종료되지 않으면, 프로세스를 강제 종료합니다.`
- `while문으로 Process의 exitValue()를 반복 호출하여 프로세스의 exit code를 확인합니다.`
- `exitValue()는 프로세스가 종료되었다면 exit code를 반환하고 종료되지 않으면 예외를 발생하는데, 이때 실행 시간을 비교하여 10초가 지났다면 프로세스를 강제 종료하고 아니면 다음 반복으로 넘어갑니다.`
- `while문을 쉬지 않고 반복하는 것은 서버에 부하를 주고 cpu 자원이 낭비되므로, Thread.sleep()을 사용하여 0.2초마다 반복되도록 합니다.`

</br></br>
<div>
<h2>C, C++</h2>
        
        1. c 또는 cpp 코드를 컴파일합니다. 만약 컴파일에 실패했으면 에러 메세지를 결과 텍스트 파일로 리다이렉션합니다.
            - gcc CFile.c -o CFile 2> output.txt

	    2. 컴파일 실행 결과의 exit code가 0(컴파일 성공)이면 실행 파일을 실행합니다.
            - input 없는 경우 : ./CFile > output.txt
            - input 있는 경우 : ./CFile < input.txt > output.txt

        3. output.txt를 읽어서 결과를 반환합니다.


<h2>Java</h2>
        
- `java는 javac 명령어로 컴파일하여 바이트 코드 파일(.class)을 만들 때 public class명과 파일명이 같아야 하는 이유가 있습니다. (단, class의 경우 상관없음)`
    - `why? : JVM이 이를 진입점(Entry Point)으로 인식하기 때문`
- `그래서 바이트 코드 파일을 만들지 않고 java 명령어로 코드를 바로 해석하는 방식으로 실행합니다.`
</br>

        1. java 명령어로 코드를 실행하여 실행 결과 또는 에러 메세지를 output.txt로 리다이렉션 합니다.
            - input 없는 경우 : java JavaFile.java > output.txt 2>&1
            - input 있는 경우 : java JavaFile.java < input.txt > output.txt 2>&1
            
        2. output.txt를 읽어서 결과를 반환합니다.


<h2>Python</h2>
        
        1. python3 명령어로 코드를 실행하여 실행 결과 또는 에러 메세지를 output.txt로 리다이렉션 합니다.
            - input 없는 경우 : python3 PythonFile.py > output.txt 2>&1
            - input 있는 경우 : python3 PythonFile.py < input.txt > output.txt 2>&1
        2. output.txt를 읽어서 결과를 반환합니다.


<h4>참고 명령어</h4>

- `2> : 표준 에러 리다이렉션. 표준 에러만 리다이렉션하며 표준 출력은 터미널에 표시합니다.`
- `2>&1 : 표준 에러를 표준 출력이 보내진 곳과 동일한 곳을 보냅니다.`

</div>
</br>
</br>

<h2>크롬 확장 프로그램</h2>
크롬 확장 프로그램을 설치함으로써 유튜브 오른쪽의 추천 동영상을 밑으로 밀고 코드 실행 웹이 삽입되도록 함

- tistory, velog 등 다른 웹 사이트에도 적용 예정


![확장 프로그램 실행한 유튜브 화면](https://github.com/preferrrr/CodeMate_Server/assets/99793526/4e4589ed-ca77-4d0e-96c8-e8be889aab1f)
