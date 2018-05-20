package net.easyappsec.crypto.general;

import net.easyappsec.crypto.general.storage.KeyStorage;
import net.easyappsec.crypto.general.storage.MemoryPrivateKeyStorage;
import net.easyappsec.crypto.general.storage.MemoryPublicKeyStorage;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class GeneralCryptoAdapter implements CryptoAdapter{

    private KeyStorage<PublicKey> publicKeyStorage = new MemoryPublicKeyStorage();
    private KeyStorage<PrivateKey> privateKeyKeyStorage = new MemoryPrivateKeyStorage();

    public byte[] hash(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static final int MAC_KEY_SIZE = 32;

    public byte[] generateMacKey() {
        try {
            byte[] bytes = new byte[MAC_KEY_SIZE];
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            return bytes;
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public byte[] mac(byte[] data, byte[] key) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            return mac.doFinal(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public void assignSignatureKeyPair() {
        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair keyPair = kpg.genKeyPair();
            publicKeyStorage.save(keyPair.getPublic());
            privateKeyKeyStorage.save(keyPair.getPrivate());
        }catch(Exception ex){
            throw  new RuntimeException(ex);
        }
    }

    public byte[] getSignature(byte[] hashOfData) {
        if (privateKeyKeyStorage.getKey() == null){
            throw new IllegalStateException("You should generate the keys first!");
        }
        try{
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initSign(privateKeyKeyStorage.getKey());
            sig.update(hashOfData);
            return sig.sign();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public boolean verifySignature(byte[] hashOfData, byte[] signature) {
        if (privateKeyKeyStorage.getKey() == null){
            throw new IllegalStateException("You should generate the keys first!");
        }
        try{
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKeyStorage.getKey());
            sig.update(hashOfData);
            return sig.verify(signature);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
