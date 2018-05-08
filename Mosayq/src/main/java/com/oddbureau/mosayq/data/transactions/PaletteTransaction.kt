package com.oddbureau.mosayq.data.transactions

import android.content.Context
import com.oddbureau.mosayq.data.models.Color
import com.oddbureau.mosayq.data.models.Palette
import io.realm.Realm
import io.realm.RealmList

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
class PaletteTransaction (private val context: Context) : DataTransaction() {

    fun addPalette(colorsHex: MutableList<String>){

        var transaction = Realm.Transaction {

            realm ->

            var colors = RealmList<Color>()
            var name = ""

            for(hex: String in colorsHex)
            {
                var col = realm.createObject(Color::class.java)
                col.hex = hex
                colors.add(col)

                name += hex.replace("#", "", false)

            }

            var palette: Palette = realm.createObject(Palette::class.java, getNextKey(realm, "Palette"))
            palette.name = name
            palette.colors = colors

        }

        super.execute(transaction)
    }

    fun updatePalette(paletteId: Long?, colorsHex: MutableList<String>){

        var transaction = Realm.Transaction {

            realm ->

            var colors = RealmList<Color>()
            var name = ""

            for(hex: String in colorsHex)
            {
                var col = realm.createObject(Color::class.java)
                col.hex = hex
                colors.add(col)

                name += hex.replace("#", "", false)
            }

            var palette: Palette = realm.where(Palette::class.java).equalTo("id", paletteId).findFirst()
            palette.name = name
            palette.colors = colors

        }

        super.execute(transaction)
    }

    fun removePalette(paletteId: Long?){

        var transaction = Realm.Transaction {

            realm ->

            var palette: Palette = realm.where(Palette::class.java).equalTo("id", paletteId).findFirst()
            palette.deleteFromRealm()

        }

        super.execute(transaction)
    }
}