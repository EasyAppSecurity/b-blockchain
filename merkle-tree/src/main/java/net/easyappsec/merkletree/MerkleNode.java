package net.easyappsec.merkletree;

import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class MerkleNode implements Iterable<MerkleNode>{

    private MerkleHash hash;
    private MerkleNode rightNode;
    private MerkleNode leftNode;
    private MerkleNode parent;

    public boolean isLeaf(){
        return this.leftNode == null && this.rightNode == null;
    }

    public MerkleNode(){
    }

    /**
     * Constructor for a base node (leaf), representing the lowest level of the tree.
     * @param hash
     */
    public MerkleNode(MerkleHash hash) {
        this.hash = hash;
    }

    /**
     * Constructor for a parent node.
     * @param rightNode
     * @param leftNode
     */
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

    /**
     * Bottom-up/left-right iteration of the tree.
     * @param node
     * @return
     */
    protected Iterator<MerkleNode> iterate(MerkleNode node){
        if (this.leftNode != null){
            return iterate(this.leftNode);
        }
        if (this.rightNode != null){
            return iterate(this.rightNode);
        }
        return iterate(node);
    }

    public MerkleHash computeHash(byte[] buffer){
        this.hash = MerkleHash.create(buffer);
        return this.hash;
    }

    /**
     * Return the leaves (not all children, just leaves) under this node
     * @return
     */
    public Iterator<MerkleNode> leaves(){
        return StreamSupport.stream(spliterator(), false)
                .filter(node -> node.rightNode == null && node.leftNode == null)
                .iterator();
    }

    public void setLeftNode(MerkleNode node){
        this.leftNode = node;
        this.leftNode.parent = this;
        computeHash();
    }

    public void setRightNode(MerkleNode node){
        this.rightNode = node;
        this.rightNode.parent = this;
        if (this.leftNode != null){
            computeHash();
        }
    }

    /**
     * True if we have enough data to verify our hash, particularly if we have child nodes.
     * @return True if this node is a leaf or a branch with at least a left node.
     */
    public boolean canVerifyHash(){
        return (this.leftNode != null & this.rightNode != null) || leftNode != null;
    }

    /**
     * Verifies the hash for this node against the computed hash for our child nodes.
     * If we don't have any children, the return is always true because we have nothing to verify against.
     * @return
     */
    public boolean verifyHash(){
        if (leftNode == null && rightNode == null){
            return true;
        }
        if (rightNode == null){
            return hash.equals(leftNode.hash);
        }
        MerkleHash leftRightHash = MerkleHash.create(leftNode.hash, rightNode.hash);
        return hash.equals(leftRightHash);
    }

    public boolean equals(MerkleNode node){
        return hash.equals(node.hash);
    }

    // Repeat the left node if the right node doesn't exist.
    // This process breaks the case of doing a consistency check on 3 leaves when there are only 3 leaves in the tree.
    //MerkleHash rightHash = RightNode == null ? LeftNode.Hash : RightNode.Hash;
    //Hash = MerkleHash.Create(LeftNode.Hash.Value.Concat(rightHash.Value).ToArray());

    // Alternativately, do not repeat the left node, but carry the left node's hash up.
    // This process does not break the edge case described above.
    // We're implementing this version because the consistency check unit tests pass when we don't simulate
    // a right-hand node.
    protected void computeHash(){
        hash = rightNode == null
                ? leftNode.hash
                : MerkleHash.create(leftNode.hash, rightNode.hash);
        Optional.ofNullable(parent)
                .ifPresent(parent ->{
                    parent.computeHash();
                    }
                );
    }

    public void forEach(Consumer<? super MerkleNode> consumer) {
    }

    public Spliterator<MerkleNode> spliterator() {
        return Spliterators.spliteratorUnknownSize(iterator(), Spliterator.ORDERED);
    }
}
