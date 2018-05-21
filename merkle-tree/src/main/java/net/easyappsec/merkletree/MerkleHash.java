package net.easyappsec.merkletree;

import net.easyappsec.crypto.general.util.CryptoUtil;
import org.apache.commons.lang3.ArrayUtils;
import sun.nio.cs.StandardCharsets;

import java.io.Serializable;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;


public class MerkleHash implements Serializable {

    private byte[] value;

    protected MerkleHash() {
    }

    public static MerkleHash create(byte[] buffer) {
        MerkleHash hash = new MerkleHash();
        hash.computeHash(buffer);
        return hash;
    }

    public static MerkleHash create(String string) {
        return create(string.getBytes(UTF_8));
    }

    public static MerkleHash create(MerkleHash left, MerkleHash right) {
        return create(ArrayUtils.addAll(left.value, right.value));
    }

    protected byte[] getValue() {
        return value;
    }

    protected void setValue(byte[] value) {
        this.value = value;
    }

    public void computeHash(byte[] buffer) {
        setValue(CryptoUtil.sha256(buffer));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MerkleHash that = (MerkleHash) o;
        return Arrays.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        return Arrays.toString(value);
    }
}
