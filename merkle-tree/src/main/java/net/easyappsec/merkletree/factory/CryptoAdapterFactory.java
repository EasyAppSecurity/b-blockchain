package net.easyappsec.merkletree.factory;

import net.easyappsec.crypto.general.CryptoAdapter;
import net.easyappsec.crypto.general.GeneralCryptoAdapter;

public class CryptoAdapterFactory {

    private CryptoAdapterFactory() {
    }

    private static CryptoType cryptoType = CryptoType.COMMON;

    public static CryptoAdapter getCryptoAdapter() {
        switch (cryptoType) {
            case COMMON:
                return new GeneralCryptoAdapter();
            default:
                throw new UnsupportedOperationException("Unsupported adapter type");
        }
    }

    public static void setCryptoType(CryptoType cryptoType) {
        CryptoAdapterFactory.cryptoType = cryptoType;
    }
}
