package net.easyappsec.block.plain;

import net.easyappsec.block.Block;
import net.easyappsec.block.BlockChain;
import net.easyappsec.block.trace.BlockTracer;

import java.util.LinkedList;
import java.util.List;

public class PlainBlockChain implements BlockChain {

    private Block currentBlock;
    private Block headBlock;

    private BlockTracer blockTracer;

    private final List<Block> blocks;

    public PlainBlockChain() {
        this.blocks = new LinkedList<Block>();
    }

    @Override
    public void acceptBlock(Block block) {
        if (headBlock == null) {
            headBlock = block;
            headBlock.setPreviousBlockHash(null);
        }

        currentBlock = block;
        blocks.add(block);
    }

    @Override
    public boolean verifyChain() {
        if (headBlock == null) {
            throw new IllegalStateException("Genesis block not set.");
        }
        return headBlock.isValidChain(null, blockTracer);
    }

    public void setBlockTracer(BlockTracer blockTracer) {
        this.blockTracer = blockTracer;
    }
}
