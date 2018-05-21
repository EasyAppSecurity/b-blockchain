package net.easyappsec.block.plain;

import net.easyappsec.block.Block;
import net.easyappsec.block.trace.BlockVerificationTracer;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlainBlockChainTest {

    @Test
    public void verifyChain() {
        PlainBlockChain plainBlockChain = new PlainBlockChain();
        plainBlockChain.setBlockTracer(new BlockVerificationTracer(true));

        Block block1 = new SingleBlock(1, "111111".getBytes(), null);
        Block block2 = new SingleBlock(2, "222222".getBytes(), block1);
        Block block3 = new SingleBlock(3, "333333".getBytes(), block2);
        Block block4 = new SingleBlock(4, "444444".getBytes(), block3);
        Block block5 = new SingleBlock(5, "555555".getBytes(), block4);
        Block block6 = new SingleBlock(6, "666666".getBytes(), block5);
        Block block7 = new SingleBlock(7, "777777".getBytes(), block6);
        Block block8 = new SingleBlock(8, "888888".getBytes(), block7);
        Block block9 = new SingleBlock(9, "999999".getBytes(), block8);

        plainBlockChain.acceptBlock(block1);
        plainBlockChain.acceptBlock(block2);
        plainBlockChain.acceptBlock(block3);
        plainBlockChain.acceptBlock(block4);
        plainBlockChain.acceptBlock(block5);
        plainBlockChain.acceptBlock(block6);
        plainBlockChain.acceptBlock(block7);
        plainBlockChain.acceptBlock(block8);
        plainBlockChain.acceptBlock(block9);

        assertTrue(plainBlockChain.verifyChain());

        block7.setPayload("dsfgsdg".getBytes());

        assertFalse(plainBlockChain.verifyChain());
    }
}