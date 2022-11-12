package org.softauto.core;



public abstract class AbstractSerialization {

    public abstract byte[] serialize(Object obj) throws Exception;

    public abstract <T> T deserialize(final byte[] objectData) throws Exception;

    public abstract <T> T deserialize(final byte[] objectData,Class<T> type) throws Exception;
}
