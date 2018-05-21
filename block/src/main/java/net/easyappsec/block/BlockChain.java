package net.easyappsec.block;

import java.io.Serializable;

public interface BlockChain extends Serializable {

    public void acceptBlock(Block block);

    public boolean verifyChain();

}
