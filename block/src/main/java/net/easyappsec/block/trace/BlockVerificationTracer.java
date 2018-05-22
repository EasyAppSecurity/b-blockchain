package net.easyappsec.block.trace;

import net.easyappsec.block.Block;

import java.util.logging.Logger;

public class BlockVerificationTracer implements BlockTracer<Boolean, Void, Void> {

    private Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    private final boolean verbose;

    public BlockVerificationTracer(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public void trace(Block block, Boolean valid) {
        if (verbose) {
            if (!valid) {
                log.severe(String.format("Block number [%s] : FAILED VERIFICATION", block.getGuid()));
            } else {
                log.warning(String.format("Block number [%s] : pass verification", block.getGuid()));
            }
        }
    }

    @Override
    public void trace(Block block, Boolean aBoolean, Void aVoid, Void aVoid2) {
        trace(block, aBoolean);
    }

    @Override
    public void trace(Block block, Boolean aBoolean, Void aVoid) {
        trace(block, aBoolean);
    }
}
