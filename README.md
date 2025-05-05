# webchat
간단한 웹 채팅 서버입니다.

구현된 기능
1. 로그인 및 회원가입이 가능합니다.
2. 친구코드를 통해 친구 추가가 가능합니다.
3. 원하는 친구를 추가해 채팅방을 만들 수 있습니다.
4. 텍스트, 이미지, 영상, 소리 등의 미디어 파일을 전송 가능합니다.

참고사항
1. db는 h2 database를 사용하며 메모리 모드이기 때문에, 서버를 종료하면 기록된 내용도 사라집니다.
2. 서버를 중점적으로 만들었기 때문에, 프론트엔드가 미흡합니다.
3. 테스트용 아이디는 다음과 같습니다.
id: test, password: password
id: test2, password: password
id: test3, password: password
참고 링크: https://github.com/s576air/webchat/blob/main/src/main/java/com/s576air/webchat/DataInitializer.java
