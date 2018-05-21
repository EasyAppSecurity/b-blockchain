package net.easyappsec.block.plain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.easyappsec.block.Block;
import net.easyappsec.block.json.MapperFactory;
import net.easyappsec.block.trace.BlockTracer;
import net.easyappsec.merkletree.factory.CryptoAdapterFactory;
import org.apache.commons.lang3.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

public class SingleBlock implements Block {

    private long blockNumber;
    private Date createdDate;
    @JsonIgnore
    private String blockHash;
    private String previousBlockHash;
    @JsonIgnore
    private Block nextBlock;
    private byte[] payload;

    public SingleBlock(long blockNumber, byte[] payload, Block parent) {
        this.blockNumber = blockNumber;
        this.payload = payload;
        this.createdDate = new Date();
        setBlockHash(parent);
    }

    @Override
    public long getBlockNumber() {
        return blockNumber;
    }

    @Override
    public Date getCreatedDate() {
        return createdDate;
    }

    @Override
    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    @Override
    @JsonIgnore
    public String getBlockHash() {
        return blockHash;
    }

    @Override
    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    @Override
    public void setPreviousBlockHash(String hash) {
        this.previousBlockHash = hash;
    }

    @Override
    public String calculateBlockHash(String previousBlockHash) {
        SingleBlock singleBlockClone = SerializationUtils.clone(this);
        singleBlockClone.setPreviousBlockHash(previousBlockHash);
        String json = MapperFactory.<SingleBlock>getMapper().getJson(singleBlockClone);
        return Base64.getEncoder().encodeToString(
                CryptoAdapterFactory.getCryptoAdapter()
                        .hash(json.getBytes(StandardCharsets.UTF_8))
        );
    }

    @Override
    public void setBlockHash(Block parent) {
        if (parent != null) {
            previousBlockHash = parent.getBlockHash();
            parent.setNextBlock(this);
        } else {
            previousBlockHash = null;
        }
        blockHash = calculateBlockHash(previousBlockHash);
    }

    @Override
    @JsonIgnore
    public Block getNextBlock() {
        return nextBlock;
    }

    @Override
    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }

    @Override
    @JsonIgnore
    public boolean isValidChain(String prevBlockHash, BlockTracer tracer) {
        boolean valid = true;
        String newBlockHash = calculateBlockHash(prevBlockHash);
        if (!newBlockHash.equals(blockHash)) {
            valid = false;
        } else {
            valid |= (prevBlockHash == null
                    ? (prevBlockHash == previousBlockHash)
                    : prevBlockHash.equals(previousBlockHash));
        }

        if (tracer != null) {
            tracer.trace(this, valid);
        }

        if (nextBlock != null) {
            return nextBlock.isValidChain(newBlockHash, tracer);
        }

        return valid;
    }

    @Override
    public byte[] getPayload() {
        return payload;
    }

    @Override
    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

}
