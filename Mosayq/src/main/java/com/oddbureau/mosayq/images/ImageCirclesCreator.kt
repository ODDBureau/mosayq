package com.oddbureau.mosayq.images

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.utils.Randomiser
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

class ImageCirclesCreator : ImageCreator {

    constructor(pattern: Pattern, colors: ArrayList<String>) : super(pattern, colors)

    override fun generateBitmap(): Bitmap {

        var bitmap = super.generateBitmap()
        val canvas = super.generateCanvas(bitmap)

        val backgroundColor = colors[Randomiser().randomiseInt(colors.size)]
        drawBackground(bitmap, backgroundColor)

        when (pattern.category) {

            "ripple-multi-color" -> {
                colors.remove(backgroundColor)
                drawRipples(canvas, getPaintStroke(10f), 25f, canvasWidth.toFloat())
            }

            else -> {

            }
        }

        return downscaleBitmap(bitmap)
    }

    fun getPaintStroke( paintStrokeWidth: Float ): Paint{
        var paintStroke = Paint()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.color = Color.parseColor(colors[Randomiser().randomiseInt(colors.size)])
        paintStroke.strokeWidth = paintStrokeWidth
        paintStroke.isAntiAlias = true

        return paintStroke
    }

    fun drawRipples(canvas: Canvas, paintStroke: Paint, standardRadius: Float, maxRadius: Float) {

        var radius = standardRadius

        var colorIndex = 0
        var randomColors = Randomiser().randomiseArrayList(colors) as ArrayList<String>

        while(radius < maxRadius)
        {
            paintStroke.color = Color.parseColor(randomColors[colorIndex])
            canvas.drawCircle(canvasWidth/2f, canvasHeight/2f, radius, paintStroke)
            radius += standardRadius

            if(colorIndex == randomColors.size-1)
                colorIndex = 0
            else
                colorIndex++

        }
    }


}