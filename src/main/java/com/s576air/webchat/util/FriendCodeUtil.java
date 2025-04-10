package com.s576air.webchat.util;

import org.sqids.Sqids;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

public class FriendCodeUtil {
    // 모음 제거
    private static final String CHARSET = "bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ1234567890";
    private static final int TAG_LENGTH = 8;

    private static final Sqids sqids = Sqids.builder()
        .alphabet(CHARSET)
        .minLength(8)
        .build();
    private static final SecureRandom random = new SecureRandom();

    public static String encodeId(long userId) {
        return sqids.encode(List.of(userId));
    }

    private static Optional<Long> decode(String code) {
        List<Long> ids = sqids.decode(code);
        return ids.isEmpty() ? Optional.empty() : Optional.of(ids.get(0));
    }

    public static String generateTag() {
        StringBuilder sb = new StringBuilder(TAG_LENGTH);
        for (int i = 0; i < TAG_LENGTH; i++) {
            int index = random.nextInt(CHARSET.length());
            sb.append(CHARSET.charAt(index));
        }
        return sb.toString();
    }

    public static Optional<Long> getId(String friendCode) {
        if (friendCode.length() != 16) { return Optional.empty(); }
        return decode(friendCode.substring(0, 8));
    }

    public static Optional<String> getTag(String friendCode) {
        if (friendCode.length() != 16) { return Optional.empty(); }
        return Optional.of(friendCode.substring(8));
    }
}
