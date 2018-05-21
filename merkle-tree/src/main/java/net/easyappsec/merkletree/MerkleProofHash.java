package net.easyappsec.merkletree;

import java.io.Serializable;

public class MerkleProofHash implements Serializable {

    private MerkleHash hash;
    private Branch direction;

    public MerkleProofHash(MerkleHash hash, Branch direction) {
        this.hash = hash;
        this.direction = direction;
    }

    public MerkleHash getHash() {
        return hash;
    }

    protected void setHash(MerkleHash hash) {
        this.hash = hash;
    }

    public Branch getDirection() {
        return direction;
    }

    protected void setDirection(Branch direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return hash.toString();
    }
}
