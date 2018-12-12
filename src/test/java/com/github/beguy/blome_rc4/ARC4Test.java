package com.github.beguy.blome_rc4;

import org.junit.Assert;
import org.junit.Test;

public class ARC4Test {
    @Test
    public void byteArr() {
        byte[] key = "passw0rd".getBytes();
        byte[] plainData = "Super secret data!".getBytes();

        ARC4 cypher = new ARC4(key);
        byte[] cipherData = cypher.encrypt(plainData);
        cypher.init(key); // reset
        byte[] decryptedData = cypher.decrypt(cipherData);

        Assert.assertArrayEquals(decryptedData, plainData);
    }

    @Test
    public void ruString() {
        byte[] key = "012345".getBytes();
        String plainData = "Строка на русском могучем и великом языку";

        ARC4 cypher = new ARC4(key);
        byte[] cipherData = cypher.encrypt(plainData.getBytes());
        cypher.init(key); // reset
        Assert.assertEquals(new String(cypher.decrypt(cipherData)), plainData);
    }
}
