package com.s576air.webchat.util;

import org.sqids.Sqids;

import java.util.List;

public class FriendCodeUtil {
    private static final Sqids sqids = Sqids.builder()
        .alphabet("bcdfghjklmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ1234567890") // 모음 제거
        .minLength(8)
        .build();

    public static String encode(long userId) {
        return sqids.encode(List.of(userId));
    }

    public static long decode(String code) {
        List<Long> ids = sqids.decode(code);
        return ids.isEmpty() ? -1 : ids.get(0);
    }
}
