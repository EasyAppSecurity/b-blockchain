package net.easyappsec.merkletree;

import java.util.List;
import java.util.function.Supplier;

public class MerkleTree {

    private MerkleNode rootNode;

    public MerkleNode getRootNode() {
        return rootNode;
    }

    protected void setRootNode(MerkleNode rootNode) {
        this.rootNode = rootNode;
    }

    protected List<MerkleNode> nodes;
    protected List<MerkleNode> leaves;

    public static void contract(Supplier<Boolean> action, String msg)
            throws MerkleException{
        if (!action.get()){
            throw new MerkleException(msg);
        }
    }
}
