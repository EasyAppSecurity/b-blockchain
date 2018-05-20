package net.easyappsec.crypto.general.storage;

import java.security.PublicKey;

public class MemoryPublicKeyStorage implements KeyStorage<PublicKey>{

    private PublicKey publicKey;

    @Override
    public synchronized void save(PublicKey key) {
        this.publicKey = key;
    }

    @Override
    public synchronized PublicKey getKey() {
        return publicKey;
    }
}
