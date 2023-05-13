package com.example.music.data.remote

import androidx.databinding.ktx.BuildConfig
import com.squareup.moshi.Moshi
import com.example.music.BASE_URL
import com.example.music.data.remote.moshiFactories.MyKotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by TruyenIT
 */


private const val timeoutRead = 30   //In seconds // Thời gian timeout khi đọc dữ liệu là 30 giây.
private const val contentType = "Content-Type"
private const val contentTypeValue = "application/json" // Giá trị của header Content-Type là application/json.
private const val timeoutConnect = 30   //In seconds  // Thời gian timeout khi kết nối là 30 giây.

@Singleton
// Class ServiceGenerator được đánh dấu là @Singleton để đảm bảo rằng chỉ có một instance của nó trong toàn bộ ứng dụng.
class ServiceGenerator @Inject constructor() {
    private val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
    private val retrofit: Retrofit

    private var headerInterceptor = Interceptor { chain ->
        val original = chain.request()

        val request = original.newBuilder()
            .header(contentType, contentTypeValue)
            .method(original.method, original.body)
            .build()

        chain.proceed(request)
    }

    private val logger: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                loggingInterceptor.apply { level = HttpLoggingInterceptor.Level.BODY }
            }
            return loggingInterceptor
        }

    init {
        okHttpBuilder.addInterceptor(headerInterceptor)
        okHttpBuilder.addInterceptor(logger)
        okHttpBuilder.connectTimeout(timeoutConnect.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(timeoutRead.toLong(), TimeUnit.SECONDS)
        val client = okHttpBuilder.build()
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
            .build()
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    private fun getMoshi(): Moshi {
        return Moshi.Builder()
            .add(MyKotlinJsonAdapterFactory())
            .add(com.example.music.data.remote.moshiFactories.MyStandardJsonAdapters.FACTORY)
            .build()
    }
}
