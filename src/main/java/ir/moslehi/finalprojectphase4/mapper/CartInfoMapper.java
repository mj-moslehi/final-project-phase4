package ir.moslehi.finalprojectphase4.mapper;

import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveRequest;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSaveResponse;
import ir.moslehi.finalprojectphase4.dto.cartInfo.CartInfoSignInRequest;
import ir.moslehi.finalprojectphase4.model.CartInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CartInfoMapper {

    CartInfoMapper INSTANCE= Mappers.getMapper(CartInfoMapper.class);

    CartInfo cartInfoSaveRequestToModel(CartInfoSaveRequest request);

    CartInfoSaveResponse modelToCartInfoSaveResponse(CartInfo bankCart);

    CartInfo cartInfoSignInRequestToModel(CartInfoSignInRequest signInRequest);


}
