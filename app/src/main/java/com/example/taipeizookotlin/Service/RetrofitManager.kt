@file:Suppress("PackageName")

package com.example.taipeizookotlin.Service

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class RetrofitManager {
    //Singleton
    @Volatile
    private var mRetrofitManager: RetrofitManager? = null

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun <T> createService(service: Class<T>?): T {
        return retrofit().create(service)
    }

    fun getInstance(): RetrofitManager? {
        if (mRetrofitManager == null) {
            synchronized(Retrofit::class.java) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = RetrofitManager()
                }
            }
        }
        return mRetrofitManager
    }

    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://data.taipei") //url = baseurl + @GER("...")
            .addConverterFactory(GsonConverterFactory.create())
            .client(Objects.requireNonNull(okHttpClient()))
            .build()
    }


    private fun okHttpClient(): OkHttpClient? {
        try {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(Interceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request()
                    )
                })
                .retryOnConnectionFailure(true)
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build()
        } catch (e: Exception) {
            Log.v("OkhttpException", "Exception$e")
        }
        return null
    }


    /**
     * 设置https 访问的时候对所有证书都进行信任
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getSSLOkHttpClient(): OkHttpClient {
        val trustManager: X509TrustManager = @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @SuppressLint("TrustAllX509TrustManager")
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }
        }
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
    }


}