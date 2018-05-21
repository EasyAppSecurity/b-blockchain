package net.easyappsec.merkletree.factory.impl;

import net.easyappsec.merkletree.MerkleHash;
import net.easyappsec.merkletree.MerkleNode;
import net.easyappsec.merkletree.factory.MerkleNodeFactory;

public class SimpleMerkleNodeFactory implements MerkleNodeFactory {

    @Override
    public MerkleNode createNode(MerkleHash hash) {
        return new MerkleNode(hash);
    }

    @Override
    public MerkleNode createNode(MerkleNode left, MerkleNode right) {
        return new MerkleNode(left, right);
    }
}
