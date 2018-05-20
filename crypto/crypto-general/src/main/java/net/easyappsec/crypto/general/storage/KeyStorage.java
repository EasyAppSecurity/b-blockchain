package net.easyappsec.crypto.general.storage;

import java.security.Key;

public interface KeyStorage<K extends Key> {

    public void save(K key);

    public K getKey();

}
