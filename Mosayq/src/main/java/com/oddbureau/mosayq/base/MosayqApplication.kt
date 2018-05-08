package com.oddbureau.mosayq.base

import android.app.Application
import com.oddbureau.mosayq.data.transactions.InitialTransaction
import io.realm.Realm
import io.realm.RealmConfiguration

/*
 * Copyright 2017 Joel Oliveira
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 *
 */
class MosayqApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initRealm()
    }

    fun initRealm(){
        Realm.init(this)

        // TODO - Change the key with something stored on KeyStore http://www.androidauthority.com/use-android-keystore-store-passwords-sensitive-information-623779/
     //   val key = ByteArray(64)
     //   SecureRandom().nextBytes(key)
        var config : RealmConfiguration = RealmConfiguration.Builder()
                .name(Constants.FILE_REALM)
     //           .encryptionKey(key)
                .initialData(InitialTransaction(this))
                .build()
        Realm.setDefaultConfiguration(config)
    }
}