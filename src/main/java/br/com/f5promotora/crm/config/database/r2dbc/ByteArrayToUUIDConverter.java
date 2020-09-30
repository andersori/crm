package br.com.f5promotora.crm.config.database.r2dbc;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class ByteArrayToUUIDConverter implements Converter<byte[], UUID> {

  @Override
  public UUID convert(byte[] source) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(source);
    Long high = byteBuffer.getLong();
    Long low = byteBuffer.getLong();

    return new UUID(high, low);
  }
}
