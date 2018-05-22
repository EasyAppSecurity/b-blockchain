package net.easyappsec.block.json.impl;

import net.easyappsec.block.json.JsonMapper;
import net.easyappsec.block.plain.SingleBlock;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class JacksonJsonMapperTest {

    @org.junit.Test
    public void getJson() {
        JsonMapper<SingleBlock> mapper = new JacksonJsonMapper<>();

        SingleBlock parent = new SingleBlock(UUID.randomUUID().toString(), "11111111".getBytes(), null);

        SingleBlock block = new SingleBlock(UUID.randomUUID().toString(), "3453".getBytes(), parent);
        block.setBlockHash(parent);
        block.setPreviousBlockHash("34343434");
        block.setCreatedDate(new Date());

        assertNotNull(mapper.getJson(block));
    }
}