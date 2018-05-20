package net.easyappsec.crypto.general.util;

import java.security.MessageDigest;

public class CryptoUtil {

    private CryptoUtil(){
    }

    public static byte[] sha256(byte[] data) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
