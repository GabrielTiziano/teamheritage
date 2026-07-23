package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.UserRequest;
import com.gabriel.tiziano.teamheritage.dto.response.UserResponse;
import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "scopes", ignore = true)
    User toEntity(UserRequest userRequest);

    @Mapping(target = "scopes", source = "scopes", qualifiedByName = "mapScopesEntitiesToStringScopes")
    UserResponse toResponse(User user);

    @Named("mapScopesEntitiesToStringScopes")
    default List<String> mapScopesEntitiesToStringScopes(List<Scope> scopes) {
        if (scopes == null) {
            return null;
        }
        return scopes.stream().map(Scope::getName).collect(Collectors.toList());
    }
}
