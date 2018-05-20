package net.easyappsec.merkletree;

public class MerkleProofHash {

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
