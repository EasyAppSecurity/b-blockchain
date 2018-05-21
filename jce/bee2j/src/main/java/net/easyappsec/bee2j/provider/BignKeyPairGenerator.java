package net.easyappsec.bee2j.provider;

import net.easyappsec.bee2j.Bee2CryptoLibrary;
import net.easyappsec.bee2j.BignParams;
import java.security.*;

public class BignKeyPairGenerator extends KeyPairGeneratorSpi{

    Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;
    private int _level;
    private SecureRandom _secureRandom;


    public void initialize(int keysize, SecureRandom random) {
        _level = keysize;
        _secureRandom = random;
    }

    public KeyPair generateKeyPair() {
        //lдлина ключа по умолчанию 128

        if(_level==0) {
            _level = 128;
            _secureRandom = new BrngSecureRandom();
        }


        byte[] byte_privKey = new byte[_level/4];
        byte[] byte_publicKey = new byte[_level/2];
        BignParams params = new BignParams(_level);
        byte[] brng_state = new byte[1024];
        BrngSecureRandom rng = (BrngSecureRandom) _secureRandom;
        PublicKey publicKey = new BignPublicKey(byte_publicKey);
        PrivateKey privateKey = new BignPrivateKey(byte_privKey);
        KeyPair keyPair = new KeyPair(publicKey, privateKey);
        return keyPair;
    }
}
