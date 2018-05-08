package com.oddbureau.mosayq.data.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

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
open class Palette: RealmObject() {

    @PrimaryKey
    var id: Long? = null

    var colors: RealmList<Color>? = null
    var name: String? = null

    fun getColorHexes() : ArrayList<String> {

        var colorHexes = ArrayList<String>()

        colors?.forEach {
            colorHexes.add(it.hex.toString())
        }

        return colorHexes
    }
}