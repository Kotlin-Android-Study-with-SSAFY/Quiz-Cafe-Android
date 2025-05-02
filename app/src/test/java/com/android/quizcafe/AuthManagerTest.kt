package com.android.quizcafe

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.android.quizcafe.core.datastore.AuthManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class AuthManagerTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var authManager: AuthManager

    // 1) Test 전용 코루틴 스코프와 디스패처
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        // 2) 임시 파일 기반의 in-memory DataStore 생성
        val tempFile = File.createTempFile("datastore-test", ".preferences_pb")
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tempFile }
        )

        // 3) AuthManager 생성 (init 블록이 바로 collect 시작)
        authManager = AuthManager(dataStore)
    }

    @After
    fun tearDown() {
        //어케 없애노
        testScope.cancel()
    }

    @Test
    fun `cachedToken is null initially`() = testScope.runTest {
        assertNull(authManager.getToken())
    }

    @Test
    fun `saveAccessToken updates cachedToken`() = testScope.runTest {
        val token = "jwt-123"

        // 4) save -> DataStore + cachedToken 동시 업데이트
        authManager.saveAccessToken(token)

        assertEquals(token, authManager.getToken())
    }

    @Test
    fun `deleteAccessToken clears cachedToken`() = testScope.runTest {
        authManager.saveAccessToken("to-be-deleted")

        assertNotNull(authManager.getToken())

        // 삭제
        authManager.deleteAccessToken()

        assertNull(authManager.getToken())
    }
}