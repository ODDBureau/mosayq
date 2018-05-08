package com.oddbureau.mosayq.data.transactions

import android.content.Context
import com.oddbureau.mosayq.data.models.Pattern
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
class PatternTransaction(private val context: Context) : DataTransaction() {

    fun updatePatternSelectedById(id: Long?, selected: Boolean) {

        var transaction = Realm.Transaction {

            realm ->
            var pattern : Pattern = realm.where(Pattern::class.java).equalTo("id", id).findFirst()
            pattern.isSelected = selected

        }

        super.execute(transaction)
    }
}