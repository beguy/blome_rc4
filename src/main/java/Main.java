import com.github.beguy.blome_rc4.ARC4;
import com.github.beguy.blome_rc4.BlomScheme;

import java.math.BigInteger;
import java.util.logging.Logger;

public class Main {
    private static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        BlomScheme kdc = new BlomScheme(4, 1024);
        class Client {
            BigInteger[] secret;
            BigInteger[] openId;
            Client(BigInteger[] openId, BigInteger[] secret) {
                this.openId = openId;
                this.secret = secret;
            }
        }

        Client alice, bob;
        BigInteger[] openId = kdc.getOpentId();
        BigInteger[] secret = kdc.getSecret(openId);
        alice = new Client(openId, secret);

        openId = kdc.getOpentId();
        secret = kdc.getSecret(openId);
        bob = new Client(openId, secret);

        BigInteger aliceToBobSharedKey = BlomScheme.calculateKey(alice.secret, bob.openId, kdc.getModule());
        BigInteger bobToAliceSharedKey = BlomScheme.calculateKey(bob.secret, alice.openId, kdc.getModule());
        log.info(String.format("Alice and bob generate shared secret.\nAlice shared key: %s\nBob shared key: %s", aliceToBobSharedKey, bobToAliceSharedKey));

        // Alice
        ARC4 cypher = new ARC4(aliceToBobSharedKey.toByteArray());
        String aliceMsg = "Hello Bob, у меня ребенок от русского!";
        byte[] alicePlainData = aliceMsg.getBytes();
        byte[] aliceCipherData = cypher.encrypt(alicePlainData);
        log.info("Alice send to Bob encrypted msg:\n" + new String(aliceCipherData));

        // Bob
        cypher = new ARC4(bobToAliceSharedKey.toByteArray());
        String decryptedMsgFromAlice = new String(cypher.decrypt(aliceCipherData));
        log.info("Bob decrypt msg from Alice:\n" + decryptedMsgFromAlice);
    }
}
