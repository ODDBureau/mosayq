package com.oddbureau.mosayq.data.transactions

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.oddbureau.mosayq.base.Constants
import com.oddbureau.mosayq.data.models.Palette
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.data.models.Settings
import com.oddbureau.mosayq.utils.io.JsonIO
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
class InitialTransaction(var context: Context) : Realm.Transaction {


    override fun execute(realm: Realm?) {

        val gson = GsonBuilder().create()

        //Initialize palettes
        var palettesJson = JsonIO.readJSONFromFile(context, Constants.FILE_JSON_PALETTES)
        var palettes: List<Palette> = gson.fromJson( palettesJson, object : TypeToken<List<Palette>>() {}.type)
        realm?.copyToRealm(palettes)

        //Initialize patterns
        var patternsJson = JsonIO.readJSONFromFile(context, Constants.FILE_JSON_PATTERNS)
        var patterns: List<Pattern> = gson.fromJson( patternsJson, object : TypeToken<List<Pattern>>() {}.type)
        realm?.copyToRealm(patterns)

        //Initialize settings
        var settingsJson = JsonIO.readJSONFromFile(context, Constants.FILE_JSON_SETTINGS)
        var settings: Settings = gson.fromJson( settingsJson, object : TypeToken<Settings>() {}.type)
        realm?.copyToRealm(settings)

    }

}