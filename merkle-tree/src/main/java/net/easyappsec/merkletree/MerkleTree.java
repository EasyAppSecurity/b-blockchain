package net.easyappsec.merkletree;

import net.easyappsec.merkletree.factory.MerkleNodeFactory;
import net.easyappsec.merkletree.factory.impl.SimpleMerkleNodeFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class MerkleTree implements Serializable {

    private MerkleNode rootNode;
    private transient MerkleNodeFactory nodeFactory = new SimpleMerkleNodeFactory();

    public MerkleNode getRootNode() {
        return rootNode;
    }

    protected void setRootNode(MerkleNode rootNode) {
        this.rootNode = rootNode;
    }

    protected List<MerkleNode> nodes;
    protected List<MerkleNode> leaves;

    public static void contract(Supplier<Boolean> action, String msg) {
        if (!action.get()) {
            throw new MerkleException(msg);
        }
    }

    public MerkleTree() {
        this.nodes = new LinkedList<>();
        this.leaves = new LinkedList<>();
    }

    public MerkleNode appendLeaf(MerkleNode node) {
        nodes.add(node);
        leaves.add(node);

        return node;
    }

    public void appendLeaves(MerkleNode[] nodes) {
        Arrays.stream(nodes).forEach(this::appendLeaf);
    }

    public MerkleNode appendLeaf(MerkleHash hash) {
        MerkleNode node = nodeFactory.createNode(hash);

        nodes.add(node);
        leaves.add(node);

        return node;
    }

    public List<MerkleNode> appendLeaves(MerkleHash[] hashes) {
        List<MerkleNode> nodes = new LinkedList<MerkleNode>();
        Arrays.stream(hashes).forEach(hash -> nodes.add(appendLeaf(hash)));
        return nodes;
    }

    public MerkleHash addTree(MerkleTree tree) {
        contract(() -> leaves.size() > 0, "Cannot add to a tree with no leaves");
        tree.leaves.forEach(leave -> appendLeaf(leave));
        return buildTree();
    }

    public void fixOddNumberLeaves() {
        if ((leaves.size() & 1) == 1) {
            MerkleNode node = leaves.get(leaves.size() - 1);
            appendLeaf(node.getHash());
        }
    }

    public MerkleHash buildTree() {
        contract(() -> leaves.size() > 0, "Cannot build a tree with no leaves");
        buildTree(leaves);

        return rootNode.getHash();
    }

    protected void buildTree(List<MerkleNode> nodes) {
        contract(() -> nodes.size() > 0, "Node list not expected to be empty");

        if (nodes.size() == 1) {
            rootNode = nodes.get(0);
        } else {
            List<MerkleNode> parents = new LinkedList<>();

            for (int i = 0; i < nodes.size(); i += 2) {
                MerkleNode right = (i + 1 < nodes.size()) ? nodes.get(i + 1) : null;
                MerkleNode parent = nodeFactory.createNode(nodes.get(i), right);
                parents.add(parent);
            }

            buildTree(parents);
        }
    }

    public void setNodeFactory(MerkleNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
