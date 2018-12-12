package com.github.beguy.blome_rc4;

import java.math.BigInteger;
import java.util.Random;


public class BlomScheme {

    /**
     * Secret symmetric matrix
     */
    private final BigInteger[][] secrMatrix;

    /**
     * Module of the finite field
     */
    private final BigInteger module;

    /**
     * Bit length of the numbers to be randomized
     */
    private final int bigIntLength;

    private final Random randomizer;

    /**
     * Blom's scheme key exchanger.
     *
     * @param size   secret matrix size
     * @param length numbers bit length
     * @throws IllegalArgumentException if arguments are negative
     */
    public BlomScheme(int size, int length) throws IllegalArgumentException {
        if (size < 1 || length < 1) throw new IllegalArgumentException("Negative arguments!");
        randomizer = new Random();
        secrMatrix = new BigInteger[size][size];
        bigIntLength = length;
        BigInteger number;
        while (true) {
            number = randomBigInteger();
            // false probability is 8*10^-100
            if (number.isProbablePrime(100)) {
                module = number;
                break;
            }
        }
        randSecrMatr();
    }

    /**
     * Send module to the user
     *
     * @return Galois field module
     */
    public BigInteger getModule() {
        return module;
    }

    /**
     * Randomize open key for the user
     *
     * @return BigInteger array containing the key and module
     */
    public BigInteger[] getOpentId() {
        BigInteger[] result = new BigInteger[secrMatrix.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = randomBigInteger().remainder(module);
        }
        return result;
    }

    /**
     * Calculate users's secret key
     * using secret matrix multiplication
     *
     * @param openID array of BigInteger containing open key
     * @return array of BigInteger containing secret key
     * @throws IllegalArgumentException if key length and matrix size are not corresponding
     */
    public BigInteger[] getSecret(BigInteger[] openID) {
        if (openID.length != secrMatrix.length) throw new IllegalArgumentException("Wrong ID size!");
        BigInteger[] result = new BigInteger[secrMatrix.length];
        BigInteger buf;
        for (int i = 0; i < result.length; i++) {
            result[i] = BigInteger.ZERO;
            for (int j = 0; j < result.length; j++) {
                buf = secrMatrix[i][j].multiply(openID[j]);
                result[i] = result[i].add(buf).remainder(module);
                result[i] = result[i].remainder(module);
            }
        }
        return result;
    }

    /**
     * Shared key computation using open ID and secret key multiplication
     *
     * @param aSecret      first key
     * @param bOpenID      second key
     * @param module Galois field module
     * @return shared key in BigInteger format
     * @throws IllegalArgumentException if a and b size are not the same
     */
    public static BigInteger calculateKey(BigInteger[] aSecret, BigInteger[] bOpenID, BigInteger module) throws IllegalArgumentException {
        if (aSecret.length != bOpenID.length) throw new IllegalArgumentException("");
        BigInteger result = BigInteger.ZERO;
        BigInteger buf;
        for (int i = 0; i < aSecret.length; i++) {
            buf = aSecret[i].multiply(bOpenID[i]).remainder(module);
            result = result.add(buf).remainder(module);
        }
        return result;
    }

    /**
     * Secret matrix randomization method
     */
    private void randSecrMatr() {
        for (int i = 0; i < secrMatrix.length; i++) {
            for (int j = i; j < secrMatrix.length; j++) {
                secrMatrix[i][j] = randomBigInteger().remainder(module);
                if (i != j) secrMatrix[j][i] = secrMatrix[i][j];
            }
        }
    }

    /**
     * BigInteger randomizer
     * (BigInteger constructor wrapper)
     *
     * @return random BigInteger value of the necessary bit length
     */
    private BigInteger randomBigInteger() {
        return new BigInteger(bigIntLength, randomizer);
    }
}
