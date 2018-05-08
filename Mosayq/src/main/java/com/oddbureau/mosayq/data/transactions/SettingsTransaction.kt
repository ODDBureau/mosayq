package com.oddbureau.mosayq.data.transactions

import android.content.Context
import com.oddbureau.mosayq.data.models.Settings
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
class SettingsTransaction(private val context: Context) : DataTransaction() {


    fun updateBackgroundHistory(value: Int) {

        var transaction = Realm.Transaction {

            realm ->
            var settings : Settings = realm.where(Settings::class.java).findFirst()
            settings.maxBackground = value

        }

        super.execute(transaction)
    }


    fun updateTimeInterval(value: Int) {

        var transaction = Realm.Transaction {

            realm ->
            var settings : Settings = realm.where(Settings::class.java).findFirst()
            settings.timeInterval = value

        }

        super.execute(transaction)
    }


    fun updateScreenMultiplier(value: Int) {

        var transaction = Realm.Transaction {

            realm ->
            var settings : Settings = realm.where(Settings::class.java).findFirst()
            settings.screenSizeMultiplier = value

        }

        super.execute(transaction)
    }
}