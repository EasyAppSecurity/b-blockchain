package net.easyappsec.block.json;

public interface JsonMapper<T> {
    public String getJson(T obj);

    public T getObject(String string);
}
