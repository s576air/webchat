package com.s576air.webchat;

import com.s576air.webchat.domain.User;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import com.s576air.webchat.repository.UserRepository;
import com.s576air.webchat.service.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;

    @Autowired
    public DataInitializer(
        UserRepository userRepository,
        ChatroomRepository chatroomRepository,
        ChatroomParticipantsRepository chatroomParticipantsRepository
    ) {
        this.userRepository = userRepository;
        this.chatroomRepository = chatroomRepository;
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("데이터 초기화 작업 시작");
        if (userRepository.findByLoginId("test").isEmpty()) {
            System.out.println("테스트 아이디 없음. 추가 작업 시작");
            String passwordHash = PasswordUtil.hashPassword("password");

            Long userId1 = userRepository.insertUser("test", passwordHash, "name1").orElseThrow();
            Long userId2 = userRepository.insertUser("test2", passwordHash, "name2").orElseThrow();
            Long userId3 = userRepository.insertUser("test3", passwordHash, "name3").orElseThrow();

            Long chatroomId1 = chatroomRepository.insert("test2와의 챗방").orElseThrow();
            chatroomParticipantsRepository.insert(chatroomId1, userId1);
            chatroomParticipantsRepository.insert(chatroomId1, userId2);

            Long chatroomId2 = chatroomRepository.insert("모두와의 챗방").orElseThrow();
            chatroomParticipantsRepository.insert(chatroomId2, userId1);
            chatroomParticipantsRepository.insert(chatroomId2, userId2);
            chatroomParticipantsRepository.insert(chatroomId2, userId3);

            System.out.println("데이터 초기화 완료");
        }
    }
}
