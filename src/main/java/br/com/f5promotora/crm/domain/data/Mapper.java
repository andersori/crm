package br.com.f5promotora.crm.domain.data;

public interface Mapper<EntityJPA, EntityR2DBC, DTO, Form> {

  EntityJPA toEntity(Form form);

  DTO toDtoJpa(EntityJPA entity);

  DTO toDtoR2dbc(EntityR2DBC entity);
}
