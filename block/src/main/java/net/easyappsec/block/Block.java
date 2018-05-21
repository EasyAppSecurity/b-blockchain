package net.easyappsec.block;

import net.easyappsec.block.trace.BlockTracer;

import java.io.Serializable;
import java.util.Date;

public interface Block extends Serializable {

    public long getBlockNumber();

    public Date getCreatedDate();

    public void setCreatedDate(Date date);

    public String getBlockHash();

    public String getPreviousBlockHash();

    public void setPreviousBlockHash(String hash);

    public String calculateBlockHash(String previousBlockHash);

    public void setBlockHash(Block parent);

    public Block getNextBlock();

    public void setNextBlock(Block nextBlock);

    public boolean isValidChain(String prevBlockHash, BlockTracer blockTracer);

    public byte[] getPayload();

    public void setPayload(byte[] payload);

}
