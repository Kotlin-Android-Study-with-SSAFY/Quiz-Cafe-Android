package com.android.quizcafe.main

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Context 확장 프로퍼티로 PreferencesDataStore 인스턴스를 생성
// preferencesDataStore 위임 덕분에 앱 전체에서 "auth_prefs" 이름의 단일 파일(auth_prefs.preferences_pb)에 crud 가능
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

// TODO : @HiltAndroidApp 으로 Hilt 초기화
class QuizCafeApplication : Application(){
    // TODO:  Application 수준에서 설정해줄 것들 추가하기 ex. Timber

}