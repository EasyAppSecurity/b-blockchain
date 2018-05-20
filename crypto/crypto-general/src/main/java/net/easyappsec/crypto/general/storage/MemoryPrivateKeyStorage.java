package net.easyappsec.crypto.general.storage;

import java.security.PrivateKey;

public class MemoryPrivateKeyStorage implements KeyStorage<PrivateKey>{

    private PrivateKey privateKey;

    @Override
    public synchronized void save(PrivateKey key) {
        this.privateKey = key;
    }

    @Override
    public synchronized PrivateKey getKey() {
        return privateKey;
    }
}
