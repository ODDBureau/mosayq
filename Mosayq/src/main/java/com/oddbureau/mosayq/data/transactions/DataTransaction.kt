package com.oddbureau.mosayq.data.transactions

import com.oddbureau.mosayq.data.models.BackgroundImage
import com.oddbureau.mosayq.data.models.Palette
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
open class DataTransaction {

    fun execute(transaction: Realm.Transaction)
    {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction(transaction)
        realm.close()
    }

    fun executeAsync(transaction: Realm.Transaction, transactionSuccess: Realm.Transaction.OnSuccess)
    {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync(transaction, transactionSuccess)
        realm.close()
    }

    fun getNextKey(realm: Realm, className: String): Long {
        try {

            if(className == "BackgroundImage")
                return realm.where(BackgroundImage::class.java).max("id").toLong() + 1

            if(className == "Palette")
                return realm.where(Palette::class.java).max("id").toLong() + 1

            return 0
        } catch (e: ArrayIndexOutOfBoundsException) {
            return 0
        } catch (e: NullPointerException) {
            return 0
        }

    }
}