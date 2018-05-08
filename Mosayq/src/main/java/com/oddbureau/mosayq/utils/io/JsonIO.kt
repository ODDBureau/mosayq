package com.oddbureau.mosayq.utils.io

import android.content.Context
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


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
object JsonIO {

    fun readJSONFromFile(context: Context, fileName: String): JsonElement? {

        val stream: InputStream

        try {
            stream = context.assets.open(fileName)
        } catch (e: IOException) {
            return null
        }

        return JsonParser().parse(InputStreamReader(stream))
    }
}