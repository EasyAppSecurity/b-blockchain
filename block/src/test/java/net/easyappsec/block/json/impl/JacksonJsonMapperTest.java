package net.easyappsec.block.json.impl;

import net.easyappsec.block.json.JsonMapper;
import net.easyappsec.block.plain.SingleBlock;

import java.util.Date;

import static org.junit.Assert.*;

public class JacksonJsonMapperTest {

    @org.junit.Test
    public void getJson() {
        JsonMapper<SingleBlock> mapper = new JacksonJsonMapper<>();

        SingleBlock parent = new SingleBlock(000, "11111111".getBytes(), null);

        SingleBlock block = new SingleBlock(111, "3453".getBytes(), parent);
        block.setBlockHash(parent);
        block.setPreviousBlockHash("34343434");
        block.setCreatedDate(new Date());

        String expected = "{\"blockNumber\":111,\"createdDate\":\"2018-05-21T15:27:05.104+0000\",\"blockHash\":\"iZWMN4AKlUHfB/+ceoFxX5DT04QOEMHmMGsFgvp4g3U=\",\"previousBlockHash\":\"34343434\",\"payload\":\"MzQ1Mw==\"}";

        assertNotNull(mapper.getJson(block));
    }
}