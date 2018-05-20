package net.easyappsec.merkletree;

public enum Branch {
    Left,
    Right,
    //used for linear list of hashes to compute the old root in a consistency proof.
    OldRoot
}
