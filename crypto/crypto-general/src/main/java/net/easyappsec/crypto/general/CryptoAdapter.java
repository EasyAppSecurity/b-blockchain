package net.easyappsec.crypto.general;

public interface CryptoAdapter {

    public byte[] hash(byte[] data);

    public byte[] generateMacKey();

    public byte[] mac(byte[] data, byte[] key);

    public void assignSignatureKeyPair();

    public byte[] getSignature(byte[] hashOfData);

    public boolean verifySignature(byte[] hashOfData, byte[] signature);

}
