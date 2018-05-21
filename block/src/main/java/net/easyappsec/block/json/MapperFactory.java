package net.easyappsec.block.json;

import net.easyappsec.block.json.impl.JacksonJsonMapper;

public class MapperFactory {

    private MapperFactory() {
    }

    public static <T> JsonMapper<T> getMapper() {
        return new JacksonJsonMapper<T>();
    }

}
