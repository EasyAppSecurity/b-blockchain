package net.easyappsec.merkletree;

public class MerkleException extends RuntimeException {

    public MerkleException() {
    }

    public MerkleException(String s) {
        super(s);
    }

    public MerkleException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public MerkleException(Throwable throwable) {
        super(throwable);
    }

    public MerkleException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
