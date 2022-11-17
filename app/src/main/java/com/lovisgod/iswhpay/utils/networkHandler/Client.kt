package com.lovisgod.iswhpay.utils.networkHandler

import com.lovisgod.iswhpay.utils.Constants
import com.lovisgod.iswhpay.utils.networkHandler.simplecalladapter.SimpleCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit


class KimonoClient {
    private var retrofit: Retrofit? = null

    fun getClient(): kimonoInterface {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(70, TimeUnit.SECONDS)
            .writeTimeout(70, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()


        val strategy = AnnotationStrategy()
        val serializer = Persister(strategy)

        retrofit = Retrofit.Builder()
            .baseUrl(Constants.ISW_KIMONO_BASE_URL(false))
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
            .addCallAdapterFactory(SimpleCallAdapterFactory.create())
            .client(client)
            .build()
        return retrofit!!.create(kimonoInterface::class.java)
    }
}