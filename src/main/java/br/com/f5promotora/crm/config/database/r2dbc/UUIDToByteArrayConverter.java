package br.com.f5promotora.crm.config.database.r2dbc;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class UUIDToByteArrayConverter implements Converter<UUID, byte[]> {

  public static final UUIDToByteArrayConverter INSTANCE = new UUIDToByteArrayConverter();

  @Override
  public byte[] convert(UUID source) {
    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
    bb.putLong(source.getMostSignificantBits());
    bb.putLong(source.getLeastSignificantBits());

    return bb.array();
  }
}
