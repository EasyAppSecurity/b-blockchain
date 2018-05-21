package net.easyappsec.block.trace;

import net.easyappsec.block.Block;

public interface BlockTracer<T, M, R> {

    public void trace(Block block, T t, M m, R r);

    public void trace(Block block, T t, M m);

    public void trace(Block block, T t);
}
