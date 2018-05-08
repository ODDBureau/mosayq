package com.oddbureau.mosayq.base

import android.os.Bundle
import android.support.v4.app.Fragment
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
open class RealmFragment : Fragment(){

    var realm: Realm? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
    }

    fun initialize(){
        realm = Realm.getDefaultInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        realm?.close()
        realm = null
    }

}