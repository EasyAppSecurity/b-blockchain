package net.easyappsec.merkletree.factory;

import net.easyappsec.merkletree.MerkleHash;
import net.easyappsec.merkletree.MerkleNode;

public interface MerkleNodeFactory {

    public MerkleNode createNode(MerkleHash hash);

    public MerkleNode createNode(MerkleNode left, MerkleNode right);

}
