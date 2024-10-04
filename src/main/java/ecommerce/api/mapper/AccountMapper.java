package ecommerce.api.mapper;


import ecommerce.api.dto.UserDetailDTO;
import ecommerce.api.dto.account.AccountCreateRequest;
import ecommerce.api.dto.account.ProfileUpdateRequest;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account fromCreateRequestToEntity(AccountCreateRequest request);
    Profile fromUpdateRequestToEntity(ProfileUpdateRequest request);
    UserDetailDTO fromEntityToUserDetailDTO(Account account);
}