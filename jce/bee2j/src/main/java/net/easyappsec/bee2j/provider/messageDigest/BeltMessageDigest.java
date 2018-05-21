package net.easyappsec.bee2j.provider.messageDigest;

import java.security.MessageDigestSpi;
import java.util.ArrayList;
import net.easyappsec.bee2j.Bee2CryptoLibrary;
import by.bcrypto.bee2j.provider.*;
import net.easyappsec.bee2j.provider.Util;

public class BeltMessageDigest extends MessageDigestSpi implements Cloneable {

    private ArrayList<Byte> data = new ArrayList<Byte>();

    protected void engineUpdate(byte input) {
        data.add(input);
    }

    protected void engineUpdate(byte[] input, int offset, int len) {
        for (int i = offset; i < offset + len; i++)
            data.add(input[i]);
    }

    protected byte[] engineDigest() {

        byte[] bytes= Util.bytes(data);

        Bee2CryptoLibrary bee2 = Bee2CryptoLibrary.INSTANCE;
        byte[] hash = new byte[32];

        int res = bee2.beltHash(hash, bytes, bytes.length);
        if(res!=0)
            throw new RuntimeException("BeltHash hash was broken");
        return hash;
    }

    protected void engineReset() {
        data.clear();
    }

    protected int engineGetDigestLength() {
        return 32;
    }
}
