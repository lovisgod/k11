package com.lovisgod.iswhpay.utils.networkHandler

import com.isw.iswkozen.core.network.models.PurchaseResponse
import com.lovisgod.iswhpay.utils.Constants
import com.lovisgod.iswhpay.utils.networkHandler.models.TokenConfigResponse
import com.lovisgod.iswhpay.utils.networkHandler.models.TokenRequestModel
import com.lovisgod.iswhpay.utils.networkHandler.simplecalladapter.Simple
import okhttp3.RequestBody
import retrofit2.http.*

interface kimonoInterface {

    @POST(Constants.ISW_TOKEN_URL)
    fun getISWToken( @Body request: TokenRequestModel):
            Simple<TokenConfigResponse>


    @Headers("Content-Type: application/xml", "Accept: application/xml", "Accept-Charset: utf-8")
    @POST(Constants.KIMONO_END_POINT)
    fun makeCashout(@Body request: RequestBody, @Header("Authorization") token: String ):
            Simple<PurchaseResponse>
}