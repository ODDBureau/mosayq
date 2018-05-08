package com.oddbureau.mosayq.utils

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

class Randomiser(val seed : Long = System.nanoTime()) {
    fun randomiseArrayList(list: ArrayList<*>) : ArrayList<*>{
        Collections.shuffle(list, Random(seed))
        return list
    }

    fun randomiseBoolean(): Boolean{
        return Random(seed).nextBoolean()
    }

    fun randomiseInt(size: Int): Int{
        return Random(seed).nextInt(size)
    }

    fun randomiseIntBetween(min: Int, max: Int): Int{
        return Random(seed).nextInt(max - min + 1) + min
    }

    fun randomiseIntBetweenWithExclusion(min: Int, max: Int, exclusion: Int): Int{
        var randomisedValue = Random(seed).nextInt(max - min + 1) + min

        while(randomisedValue == exclusion) {
            randomisedValue = Random().nextInt(max - min + 1) + min
        }

        return randomisedValue
    }

    fun randomiseIntWithExclusion(size: Int, exclusion: Int): Int{

        var randomisedValue = Random(seed).nextInt(size)

        while(randomisedValue == exclusion) {
            randomisedValue = Random().nextInt(size)
        }

        return randomisedValue
    }


    fun randomiseFloat(size: Int): Float{
        return Random(seed).nextFloat() * size
    }


    fun randomiseDouble(): Double{
        return Random(seed).nextDouble()
    }


    fun randomiseColorHex(): String{
        var color = "#"
        var charHex = charArrayOf('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f')

        for(i in 1..6)
            color += charHex[Randomiser().randomiseInt(charHex.size)]

        return color
    }

    fun generateUUID(): String{
        return UUID.randomUUID().toString()
    }
}