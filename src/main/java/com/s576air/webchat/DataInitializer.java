package com.s576air.webchat;

import com.s576air.webchat.repository.ChatRepository;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import com.s576air.webchat.repository.UserRepository;
import com.s576air.webchat.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public DataInitializer(
        UserRepository userRepository,
        ChatroomRepository chatroomRepository,
        ChatroomParticipantsRepository chatroomParticipantsRepository,
        ChatRepository chatRepository
    ) {
        this.userRepository = userRepository;
        this.chatroomRepository = chatroomRepository;
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("데이터 초기화 작업 시작");
        if (userRepository.findByLoginId("test").isEmpty()) {
            System.out.println("테스트 아이디 없음. 추가 작업 시작");
            String passwordHash = PasswordUtil.hashPassword("password");

            // 계정 3개 추가
            Long userId1 = userRepository.insertUser("test", passwordHash, "name1").orElseThrow();
            Long userId2 = userRepository.insertUser("test2", passwordHash, "name2").orElseThrow();
            Long userId3 = userRepository.insertUser("test3", passwordHash, "name3").orElseThrow();

            // 채팅방 2개 추가
            Long chatroomId1 = chatroomRepository.insert("test2와의 챗방").orElseThrow();
            chatroomParticipantsRepository.insert(chatroomId1, userId1);
            chatroomParticipantsRepository.insert(chatroomId1, userId2);

            Long chatroomId2 = chatroomRepository.insert("모두와의 챗방").orElseThrow();
            chatroomParticipantsRepository.insert(chatroomId2, userId1);
            chatroomParticipantsRepository.insert(chatroomId2, userId2);
            chatroomParticipantsRepository.insert(chatroomId2, userId3);

            // 채팅 추가
            chatRepository.addTextChat(chatroomId1, userId1, "나는 user1이야. 첫 메시지네. 잘 부탁해.");
            Thread.sleep(1);
            chatRepository.addTextChat(chatroomId1, userId2, "나는 user2야. 나도 잘 부탁해.");
            Thread.sleep(1);
            chatRepository.addTextChat(chatroomId1, userId2, "세번째 메시지!");
            Thread.sleep(1);
            chatRepository.addTextChat(chatroomId1, userId2, "마지막 메시지!");

            chatRepository.addTextChat(chatroomId2, userId1, "내가 처음이다!");
            Thread.sleep(1);
            chatRepository.addTextChat(chatroomId2, userId2, "나는 두번째네..");
            Thread.sleep(1);
            chatRepository.addTextChat(chatroomId2, userId3, "내가 꼴찌네");

            System.out.println("데이터 초기화 완료");
        }
    }
}
