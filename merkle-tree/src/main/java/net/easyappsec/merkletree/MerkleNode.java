package net.easyappsec.merkletree;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class MerkleNode implements Iterable<MerkleNode>, Serializable {

    private MerkleHash hash;
    private MerkleNode rightNode;
    private MerkleNode leftNode;
    private MerkleNode parent;

    public boolean isLeaf() {
        return this.leftNode == null && this.rightNode == null;
    }

    public MerkleNode() {
    }

    public MerkleNode(MerkleHash hash) {
        this.hash = hash;
    }

    public MerkleNode(MerkleNode leftNode, MerkleNode rightNode) {
        this.rightNode = rightNode;
        this.leftNode = leftNode;
        this.leftNode.parent = this;
        final MerkleNode _this = this;
        Optional.ofNullable(rightNode)
                .ifPresent(right -> right.parent = _this);
    }

    @Override
    public String toString() {
        return hash.toString();
    }

    public Iterator<MerkleNode> iterator() {
        return iterate(this);
    }

    protected Iterator<MerkleNode> iterate(MerkleNode node) {
        if (this.leftNode != null) {
            return iterate(this.leftNode);
        }
        if (this.rightNode != null) {
            return iterate(this.rightNode);
        }
        return iterate(node);
    }

    public MerkleHash computeHash(byte[] buffer) {
        this.hash = MerkleHash.create(buffer);
        return this.hash;
    }

    public Iterator<MerkleNode> leaves() {
        return StreamSupport.stream(spliterator(), false)
                .filter(node -> node.rightNode == null && node.leftNode == null)
                .iterator();
    }

    public void setLeftNode(MerkleNode node) {
        this.leftNode = node;
        this.leftNode.parent = this;
        computeHash();
    }

    public void setRightNode(MerkleNode node) {
        this.rightNode = node;
        this.rightNode.parent = this;
        if (this.leftNode != null) {
            computeHash();
        }
    }


    public boolean canVerifyHash() {
        return (this.leftNode != null & this.rightNode != null) || leftNode != null;
    }

    public boolean verifyHash() {
        if (leftNode == null && rightNode == null) {
            return true;
        }
        if (rightNode == null) {
            return hash.equals(leftNode.hash);
        }
        MerkleHash leftRightHash = MerkleHash.create(leftNode.hash, rightNode.hash);
        return hash.equals(leftRightHash);
    }

    public boolean equals(MerkleNode node) {
        return hash.equals(node.hash);
    }

    protected void computeHash() {
        hash = rightNode == null
                ? leftNode.hash
                : MerkleHash.create(leftNode.hash, rightNode.hash);
        Optional.ofNullable(parent)
                .ifPresent(parent -> {
                            parent.computeHash();
                        }
                );
    }

    public void forEach(Consumer<? super MerkleNode> consumer) {
    }

    public Spliterator<MerkleNode> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED);
    }

    MerkleHash getHash() {
        return hash;
    }

    MerkleNode getRightNode() {
        return rightNode;
    }

    MerkleNode getLeftNode() {
        return leftNode;
    }

    MerkleNode getParent() {
        return parent;
    }

    protected void setHash(MerkleHash hash) {
        this.hash = hash;
    }

    protected void setParent(MerkleNode parent) {
        this.parent = parent;
    }
}
