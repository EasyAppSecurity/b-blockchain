package net.easyappsec.merkletree;

import net.easyappsec.merkletree.factory.MerkleNodeFactory;
import net.easyappsec.merkletree.factory.impl.SimpleMerkleNodeFactory;
import net.easyappsec.merkletree.util.Iterators;
import net.easyappsec.merkletree.util.MathUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

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

    protected MerkleNode findLeaf(MerkleHash leafHash) {
        return leaves.stream()
                .filter(leave -> leave.getHash().equals(leafHash))
                .findFirst()
                .get();
    }

    protected void buildAuditTrail(List<MerkleProofHash> auditTrail, MerkleNode parent, MerkleNode child) {
        if (parent != null) {
            contract(() -> child.getParent() == parent, "Parent of child is not expected parent.");

            MerkleNode nextChild = parent.getLeftNode() == child ? parent.getRightNode() : parent.getLeftNode();
            Branch direction = parent.getLeftNode() == child ? Branch.Left : Branch.Right;

            if (nextChild != null) {
                auditTrail.add(new MerkleProofHash(nextChild.getHash(), direction));
            }

            buildAuditTrail(auditTrail, child.getParent().getParent(), child.getParent());
        }
    }

    public List<MerkleProofHash> auditProof(MerkleHash leafHash) {
        List<MerkleProofHash> auditTrail = new LinkedList<>();

        MerkleNode leafNode = findLeaf(leafHash);
        if (leafHash != null) {
            contract(() -> leafNode.getParent() != null, "Expected leaf to have a parent.");

            MerkleNode parent = leafNode.getParent();
            buildAuditTrail(auditTrail, parent, leafNode);
        }

        return auditTrail;
    }

    public List<MerkleProofHash> consistencyProof(int numberLeavesToCheck) {
        List<MerkleProofHash> hashNodes = new LinkedList<MerkleProofHash>();
        int idx = MathUtil.log(numberLeavesToCheck, 2);

        MerkleNode node = leaves.get(0);

        while (idx > 0) {
            node = node.getParent();
            --idx;
        }

        int leavesCount = Iterators.count(node.leaves());
        hashNodes.add(new MerkleProofHash(node.getHash(), Branch.OldRoot));

        if (numberLeavesToCheck == leavesCount) {
        } else {
            MerkleNode siblingNode = node.getParent().getRightNode();
            boolean traverseTree = true;

            while (traverseTree) {
                if (siblingNode != null) {
                    throw new MerkleException("Sibling node must exist because numberLeavesToCheck != leavesCount");
                }
                int siblingNodeCount = Iterators.count(siblingNode.leaves());

                if (numberLeavesToCheck - leavesCount == siblingNodeCount) {
                    hashNodes.add(new MerkleProofHash(siblingNode.getHash(), Branch.OldRoot));
                    break;
                }

                if (numberLeavesToCheck - leavesCount > siblingNodeCount) {
                    hashNodes.add(new MerkleProofHash(siblingNode.getHash(), Branch.OldRoot));
                    siblingNode = siblingNode.getParent().getRightNode();
                    leavesCount += siblingNodeCount;
                } else {
                    siblingNode = siblingNode.getLeftNode();
                }
            }
        }

        return hashNodes;
    }

    public List<MerkleProofHash> consistencyAuditProof(MerkleHash nodeHash) {
        List<MerkleProofHash> auditTrail = new LinkedList<>();

        MerkleNode node = rootNode.findWithHash(nodeHash);
        MerkleNode parent = node.getParent();

        buildAuditTrail(auditTrail, parent, node);

        return auditTrail;
    }

    public static boolean verifyAudit(MerkleHash rootHash, MerkleHash leafHash, List<MerkleProofHash> auditTrail) {
        contract(() -> auditTrail.size() > 0, "Audit trail cannot be empty.");
        MerkleHash testHash = leafHash;

        // TODO: Inefficient - compute hashes directly.
        for (MerkleProofHash auditHash : auditTrail) {
            testHash = auditHash.getDirection() == Branch.Left
                    ? MerkleHash.create(ArrayUtils.addAll(testHash.getValue(), auditHash.getHash().getValue()))
                    : MerkleHash.create(ArrayUtils.addAll(auditHash.getHash().getValue(), auditHash.getHash().getValue()));
        }

        return rootHash.equals(testHash);
    }

    public static List<Pair<MerkleHash, MerkleHash>> auditHashPairs(MerkleHash leafHash, List<MerkleProofHash> auditTrail) {
        contract(() -> auditTrail.size() > 0, "Audit trail cannot be empty.");
        List<Pair<MerkleHash, MerkleHash>> auditPairs = new LinkedList<Pair<MerkleHash, MerkleHash>>();
        MerkleHash testHash = leafHash;

        // TODO: Inefficient - compute hashes directly.
        for (MerkleProofHash auditHash : auditTrail) {
            switch (auditHash.getDirection()) {
                case Left:
                    auditPairs.add(Pair.of(testHash, auditHash.getHash()));
                    testHash = MerkleHash.create(ArrayUtils.addAll(testHash.getValue(), auditHash.getHash().getValue()));
                    break;
                case Right:
                    auditPairs.add(Pair.of(auditHash.getHash(), testHash));
                    testHash = MerkleHash.create(ArrayUtils.addAll(auditHash.getHash().getValue(), testHash.getValue()));
                    break;

            }
        }
        return auditPairs;
    }

    public static MerkleHash computeHash(MerkleHash left, MerkleHash right) {
        return MerkleHash.create(ArrayUtils.addAll(left.getValue(), right.getValue()));
    }

    public static boolean verifyConsistency(MerkleHash oldRootHash, List<MerkleProofHash> proof) {
        MerkleHash hash, lhash, rhash;

        if (proof.size() > 1) {
            lhash = proof.get(proof.size() - 2).getHash();
            int hidx = proof.size() - 1;
            hash = rhash = computeHash(lhash, proof.get(hidx).getHash());
            hidx -= 2;

            while (hidx >= 0) {
                lhash = proof.get(hidx).getHash();
                hash = rhash = computeHash(lhash, rhash);

                --hidx;
            }
        } else {
            hash = proof.get(0).getHash();
        }

        return hash.equals(oldRootHash);
    }

    public void setNodeFactory(MerkleNodeFactory nodeFactory) {
        this.nodeFactory = nodeFactory;
    }
}
