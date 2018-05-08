package com.oddbureau.mosayq.images

import android.graphics.Bitmap
import com.oddbureau.mosayq.data.managers.PalettesManager
import com.oddbureau.mosayq.data.managers.PatternsManager
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.utils.Randomiser
import java.util.*


/*
 * Copyright 2016 Joel Oliveira
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

class ImageManager {

    fun getGeneratedImage(): Bitmap{

        val selectedPattern = PatternsManager().getRandomisedPattern()
        val selectedColors = Randomiser().randomiseArrayList(PalettesManager().getRandomisedColors()) as ArrayList<String>

        return getGeneratedImage(selectedPattern, selectedColors)
    }

    fun getGeneratedImage(pattern: Pattern, colors: ArrayList<String>): Bitmap {

        when(pattern.type){
            "lines" -> return ImageLineCreator(pattern, colors).generateBitmap()
            "triangles" -> return ImageTriangleCreator( pattern, colors).generateBitmap()
            "circles" -> return ImageCirclesCreator( pattern, colors).generateBitmap()
            "mosaics" -> return ImageMosaicCreator( pattern, colors).generateBitmap()
            "geometric" -> return ImageGeometricCreator( pattern, colors).generateBitmap()
            else -> {

            }
        }

        return Bitmap.createBitmap(CanvasData.width, CanvasData.height, Bitmap.Config.ARGB_8888)
    }

}