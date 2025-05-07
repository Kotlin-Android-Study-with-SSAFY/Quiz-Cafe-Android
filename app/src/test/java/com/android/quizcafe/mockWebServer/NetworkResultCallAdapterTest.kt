package com.android.quizcafe.mockWebServer
import com.android.quizcafe.core.network.model.NetworkResult
import com.android.quizcafe.core.network.model.log
import com.android.quizcafe.core.network.util.calladapter.NetworkResultCallAdapterFactory
import com.android.quizcafe.core.network.util.convertor.NullOnEmptyConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET

class NetworkResultCallAdapterTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: NetworkResultCallAdapterTestApiService

    // 테스트용 API 인터페이스
    interface NetworkResultCallAdapterTestApiService {

        @GET("/success")
        suspend fun getSuccess(): NetworkResult<TestResponse>

        @GET("/error")
        suspend fun getError(): NetworkResult<TestResponse>

        @GET("/timeout")
        suspend fun getTimeout(): NetworkResult<TestResponse>

        @GET("/unit")
        suspend fun getUnitResponse(): NetworkResult<Unit>

        @GET("/string")
        suspend fun getStringResponse(): NetworkResult<String>
    }

    @Serializable
    data class TestResponse(val message: String)

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(NullOnEmptyConverterFactory)
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
            .build()
            .create(NetworkResultCallAdapterTestApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    private fun MockResponse.setJsonBody(fileName: String): MockResponse {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        setBody(source.readString(Charsets.UTF_8))
        return this
    }

    @Test
    fun `200 성공 응답 시 NetworkResult Success가 반환된다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setJsonBody("networkCallAdapterTest/success_response.json")
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getSuccess()

        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals("Hello World", (result as NetworkResult.Success).data.message)
    }

    @Test
    fun `404 에러 응답 시 NetworkResult Error가 반환된다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setJsonBody("networkCallAdapterTest/error_404_response.json")
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getError()

        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
        assertEquals("Not Found", result.message)
    }

    @Test
    fun `Error 응답에 message 필드가 없으면 Exception이 발생한다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setJsonBody("networkCallAdapterTest/exception_404_response.json")
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getError()

        // Then
        assertTrue(result is NetworkResult.Exception)
        assertTrue((result as NetworkResult.Exception).e is IllegalStateException)
        assertNotNull(result.e.message)
    }

    @Test
    fun `API 호출 중 타임아웃이 발생하면 Exception이 발생한다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setSocketPolicy(SocketPolicy.NO_RESPONSE) // 강제 타임아웃
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getTimeout()

        // Then
        assertTrue(result is NetworkResult.Exception)
        assertNotNull((result as NetworkResult.Exception).e)
    }

    @Test
    fun `응답 본문이 null이고 T가 Unit 타입일 때 Success를 반환한다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200) // No Content
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getUnitResponse() // 반환 타입: NetworkResult<Unit>

        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(Unit, (result as NetworkResult.Success).data)
    }

    @Test
    fun `응답 본문이 null이고 T가 Unit 타입이 아닐 때 500 Error를 반환한다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200) // No Content
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getStringResponse()

        println("result : ${result.log()}")
        // Then
        assertTrue(result is NetworkResult.Error)
        assertEquals(500, (result as NetworkResult.Error).code)
    }

    /**
     * NullOnEmptyConvertorFactory 테스트
     */
    @Test
    fun `응답 본문이 비어있을 때 null로 변환 후 Success를 반환한다`() = runBlocking {
        // Given
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("")
        mockWebServer.enqueue(mockResponse)

        // When
        val result = apiService.getUnitResponse() // 반환 타입: NetworkResult<Unit>

        // Then
        assertTrue(result is NetworkResult.Success)
        assertEquals(Unit, (result as NetworkResult.Success).data)
    }
}
