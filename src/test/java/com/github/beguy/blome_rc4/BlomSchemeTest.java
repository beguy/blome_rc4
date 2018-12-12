package com.github.beguy.blome_rc4;

import org.junit.Assert;
import org.junit.Test;
import java.math.BigInteger;

public class BlomSchemeTest {
    @Test
    public void twoClients() {
        // key distribution center
        BlomScheme kdc = new BlomScheme(2, 256);

        BigInteger[] aOpenKey = kdc.getOpentId();
        BigInteger[] aSecKey = kdc.getSecret(aOpenKey);

        BigInteger[] bOpenKey = kdc.getOpentId();
        BigInteger[] bSecKey = kdc.getSecret(bOpenKey);

        BigInteger mod = kdc.getModule();
        BigInteger aSharedKey = BlomScheme.calculateKey(aSecKey, bOpenKey, mod);
        BigInteger bSharedKey = BlomScheme.calculateKey(bSecKey, aOpenKey, mod);

        Assert.assertEquals(aSharedKey, bSharedKey);
    }
}
