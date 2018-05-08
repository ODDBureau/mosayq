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
class ImageTriangleCreator : ImageCreator {

    constructor(pattern: Pattern, colors: ArrayList<String>) : super(pattern, colors)

    override fun generateBitmap(): Bitmap {

        var bitmap = super.generateBitmap()
        val canvas = super.generateCanvas(bitmap)

        val backgroundColor = colors[Randomiser().randomiseInt(colors.size)]
        colors.remove(backgroundColor)

        drawBackground(bitmap, backgroundColor)

        when (pattern.category) {
            "rising-sun" -> {
                drawRisingSunTriangles(canvas)
            }
            "chaos" -> {
                drawChaosTriangles(canvas, 100)
            }

            else -> {

            }
        }

        return downscaleBitmap(bitmap)
    }

    fun drawChaosTriangles(canvas: Canvas, totalTriangles: Int){
        var paint = Paint()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        val numberOfColors = colors.size

        var vertices: FloatArray

        for(i in 0..totalTriangles) {

            paint.color = Color.parseColor(colors[Randomiser().randomiseInt(numberOfColors)])

            vertices = floatArrayOf(
                    Randomiser().randomiseFloat(canvasWidth),  Randomiser().randomiseFloat(canvasHeight),
                    Randomiser().randomiseFloat(canvasWidth),  Randomiser().randomiseFloat(canvasHeight),
                    Randomiser().randomiseFloat(canvasWidth),  Randomiser().randomiseFloat(canvasHeight)
            )

            drawTriangle(vertices, paint, canvas)
        }
    }

    fun drawRisingSunTriangles(canvas: Canvas) {

        val strokeWidthTop = (canvasWidth / (colors.size) ).toFloat() * .85
        val strokeWidthLeft = (canvasHeight / (colors.size) ).toFloat() * .85

        var paint = Paint()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        var vertices: FloatArray

        for ((index, value) in colors.withIndex()) {

            paint.color = Color.parseColor(value)

            vertices = floatArrayOf(
                    (canvasWidth - (index+1)*strokeWidthTop).toFloat(),  0f,
                    (canvasWidth - (index*strokeWidthTop)).toFloat()+5f, 0f,
                    canvasWidth.toFloat(), canvasHeight.toFloat())


            drawTriangle(vertices, paint, canvas)

            vertices = floatArrayOf(
                    0f,  (canvasHeight - ((index+1)*strokeWidthLeft)).toFloat(),
                    0f, (canvasHeight - (index*strokeWidthLeft)).toFloat()+5f,
                    canvasWidth.toFloat(), canvasHeight.toFloat())


            drawTriangle(vertices, paint, canvas)
        }

    }

}