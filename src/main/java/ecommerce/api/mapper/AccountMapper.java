package ecommerce.api.mapper;


import ecommerce.api.dto.account.request.AccountUpdateRequest;
import ecommerce.api.dto.account.request.RegistrationRequest;
import ecommerce.api.dto.general.UserDetailDTO;
import ecommerce.api.dto.account.request.AccountCreateRequest;
import ecommerce.api.dto.account.response.AccountResponse;
import ecommerce.api.dto.account.response.ProfileResponse;
import ecommerce.api.dto.account.request.ProfileUpdateRequest;
import ecommerce.api.entity.product.Product;
import ecommerce.api.entity.user.Account;
import ecommerce.api.entity.user.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account fromCreateRequestToEntity(AccountCreateRequest request);
    Account fromAccountUpdateRequestToEntity(AccountUpdateRequest request);
    Account fromRegistrationRequestToEntity(RegistrationRequest request);
    Profile fromUpdateRequestToEntity(ProfileUpdateRequest request);
    UserDetailDTO fromEntityToUserDetailDTO(Account account);


    ProfileResponse fromEntityToProfileResponse(Profile profile);
    AccountResponse fromEntityToAccountResponse(Account account);

    default Set<UUID> fromFavoriteProductsToUUIDs(Set<Product> products){
        return products.stream().map(Product::getId).collect(Collectors.toSet());
    }
}