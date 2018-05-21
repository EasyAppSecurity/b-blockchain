package net.easyappsec.bee2j.provider;

import java.security.*;

public final class Bee2SecurityProvider extends Provider {
    public Bee2SecurityProvider() {
        super("Bee2", 1.0, "Bee2 Security Provider v1.0");
        put("MessageDigest.BeltHash", "BeltMessageDigest");
        put("MessageDigest.Bash256", "Bash256MessageDigest");
        put("MessageDigest.Bash384", "Bash384MessageDigest");
        put("MessageDigest.Bash512", "Bash512MessageDigest");
        put("Signature.Bign", "BignSignature");
        put("KeyPairGenerator.Bign", "BignKeyPairGenerator");
        put("Cipher.Belt", "BeltCipher");
        put("Cipher.Bign", "by.bcrypto.bee2j.provider.by.BignCipherSpi");
        //put("KeyGenerator.Belt", BeltKeyGenerator.class.getName());
        put("SecureRandom.Brng", "BrngSecureRandom");
        put("Mac.BeltMAC", "BeltMAC");
    }
}
