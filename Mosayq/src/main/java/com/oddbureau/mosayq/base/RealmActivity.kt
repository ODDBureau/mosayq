package com.oddbureau.mosayq.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.oddbureau.mosayq.utils.Debugger
import io.realm.Realm

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

open class RealmActivity : AppCompatActivity() {

    var realm: Realm? = null
    val TAG: String = "RealmActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        Debugger.logDebug(TAG, "onCreate")

        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onDestroy() {

        Debugger.logDebug(TAG, "onDestroy")

        super.onDestroy()
        realm?.close()
    }
}
