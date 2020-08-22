package com.example.movieviewer.api.client

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ClientBuilder<T> {
	companion object {

		fun <T>buildRetroFitClient(
      apiClient: Class<T>,
      baseUrl: String,
			apiKey: String? = null
		): T {
			val moshi = Moshi.Builder()
				.add(KotlinJsonAdapterFactory())
				.build()

			val retrofitBuilder = Retrofit.Builder()
				.addConverterFactory(MoshiConverterFactory.create(moshi))
				.addCallAdapterFactory(CoroutineCallAdapterFactory())
				.baseUrl(baseUrl)

			val retrofit = if (apiKey != null) {
				retrofitBuilder.client(buildOkHttpClient(apiKey)).build()
			} else {
				retrofitBuilder.build()
			}

      return retrofit.create(apiClient)
		}

		private fun buildOkHttpClient(apiKey: String): OkHttpClient {
			return OkHttpClient.Builder()
				.retryOnConnectionFailure(false)
				.addInterceptor {
					val originalRequest = it.request()
					val updatedUrl = originalRequest.url().newBuilder()
						.addQueryParameter("api_key", apiKey)
						.build();

					val request = originalRequest.newBuilder()
						.url(updatedUrl).build()

					it.proceed(request)
				}.build()
		}
	}
}