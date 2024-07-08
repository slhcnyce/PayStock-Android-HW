package com.scy.paystock.network

import com.scy.paystock.views.addProduct.AddProductDto
import com.scy.paystock.views.login.LoginDto
import com.scy.paystock.views.notes.CreateNoteDto
import com.scy.paystock.views.notes.NoteDetailDto
import com.scy.paystock.views.renterCode.RenterDto
import com.scy.paystock.views.sale.PaymentDto
import com.scy.paystock.views.saleReport.SaleReportDto
import com.scy.paystock.views.stockQuery.ProductDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("check")
    suspend fun checkRenterCode(@Body renter: RenterDto): ResponseDataStatus

    @POST("authentication")
    suspend fun authentication(@Body userCredential: LoginDto): ResponseDataStatus


    @POST("products")
    suspend fun addProduct(@Body addProductDto: AddProductDto): ResponseDataStatus

    @GET("products/{productId}")
    suspend fun getProduct(@Path("productId") productId: Int): ProductDto


    @GET("notes/{renterCode}")
    suspend fun getNotes(@Path("renterCode") renterCode: Int): List<NoteDetailDto>


    @DELETE("notes/{renterCode}/{id}")
    suspend fun deleteNote(
        @Path("renterCode") renterCode: Long,
        @Path("id") id: Long
    ): ResponseDataStatus

    @POST("notes")
    suspend fun createNote(@Body createNoteDto: CreateNoteDto): ResponseDataStatus

    @POST("payment")
    suspend fun makePayment(@Body paymentDto: PaymentDto): ResponseDataStatus

    @GET("payment/{renterCode}")
    suspend fun getSaleReports(@Path("renterCode") renterCode: Long): SaleReportDto
}


object RetrofitInstance {
    private
    const val BASE_URL = "http://192.168.1.43:9090/"
    val api: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}


data class ResponseDataStatus(
    val isSuccess: Boolean
)