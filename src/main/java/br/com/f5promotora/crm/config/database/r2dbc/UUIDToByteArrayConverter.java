package br.com.f5promotora.crm.config.database.r2dbc;

import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class UUIDToByteArrayConverter implements Converter<byte[], UUID> {

  @Override
  public UUID convert(byte[] source) {
    //		ByteBuffer bb = ByteBuffer.wrap(source);
    //		long firstLong = bb.getLong();
    //		long secondLong = bb.getLong();
    //		return new UUID(firstLong, secondLong);
    return UUID.nameUUIDFromBytes(source);
  }
}
