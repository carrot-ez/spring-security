package kr.carrot.springsecurity.security.utils;

import org.junit.jupiter.api.Test;

class HashingUtilsTest {

    @Test
    public void test_1() throws Exception {
        String key = "1234";

        String s = HashingUtils.encryptSha256(key);
        System.out.println("s = " + s);
    }

}