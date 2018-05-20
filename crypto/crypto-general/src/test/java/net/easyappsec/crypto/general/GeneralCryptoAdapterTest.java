package net.easyappsec.crypto.general;

import static org.junit.Assert.*;

public class GeneralCryptoAdapterTest {

    private CryptoAdapter cryptoAdapter;

    @org.junit.Before
    public void setUp() throws Exception {
        this.cryptoAdapter = new GeneralCryptoAdapter();
    }

    @org.junit.Test
    public void hash() {
        byte[] data = "test".getBytes();
        assertNotNull(cryptoAdapter.hash(data));
    }

    @org.junit.Test
    public void generateMacKey() {
        assertNotNull(cryptoAdapter.generateMacKey());
    }

    @org.junit.Test
    public void mac() {
        byte[] data = "test".getBytes();
        assertNotNull(cryptoAdapter.mac(data, cryptoAdapter.generateMacKey()));
    }

    @org.junit.Test
    public void assignSignatureKeyPair() {
        cryptoAdapter.assignSignatureKeyPair();
    }

    @org.junit.Test
    public void getSignatureVerify() {
        byte[] data = "test".getBytes();
        cryptoAdapter.assignSignatureKeyPair();

        byte[] signature = cryptoAdapter.getSignature(data);
        assertNotNull(signature);

        assertTrue(cryptoAdapter.verifySignature(data, signature));
    }

}