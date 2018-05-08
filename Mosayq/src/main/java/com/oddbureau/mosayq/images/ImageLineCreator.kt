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
class ImageLineCreator: ImageCreator {

    constructor(pattern: Pattern, colors: ArrayList<String>) : super(pattern, colors)

    override fun generateBitmap(): Bitmap {

        var bitmap = super.generateBitmap()
        val canvas = super.generateCanvas(bitmap)

        val backgroundColor = colors[Randomiser().randomiseInt(colors.size)]
        drawBackground(bitmap, backgroundColor)

        when (pattern.category) {
            "horizontal" -> {
                drawStraightLines(canvas, true)
            }

            "diagonal" -> {
                drawDiagonalLines(canvas)
            }

            "vertical" -> {
                drawStraightLines(canvas, false)
            }

            "traces" -> {
                colors.remove(backgroundColor)
                drawTraceLines(canvas, (intArrayOf(100, 150, 200))[Randomiser().randomiseInt(3)])
            }

            else -> {

            }
        }

        return downscaleBitmap(bitmap)
    }

    fun drawTraceLines(canvas: Canvas, totalLines: Int){

        var paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.isAntiAlias = true


        val numberOfColors = colors.size


        val positionsLength = 4
        var originPosition: Int = Randomiser().randomiseInt(positionsLength) // 0 = top, 1 = left, 2 = bottom, 3 = right, -1 = lol
        var originCoordinates = getRandomCoordinatesByPosition(originPosition)

        var nextPosition: Int
        var nextCoordinates: FloatArray

        for(i in 0..totalLines) {

            paint.color = Color.parseColor(colors[Randomiser().randomiseInt(numberOfColors)])

            nextPosition = Randomiser().randomiseIntWithExclusion(positionsLength, originPosition)
            nextCoordinates = getRandomCoordinatesByPosition(nextPosition)

            canvas.drawLine(originCoordinates[0], originCoordinates[1], nextCoordinates[0], nextCoordinates[1], paint)

            originPosition = nextPosition
            originCoordinates = nextCoordinates
        }
    }

    fun getRandomCoordinatesByPosition(position: Int): FloatArray{

        when(position){
            0 -> return floatArrayOf(Randomiser().randomiseFloat(canvasWidth), 0f)
            1 -> return floatArrayOf(0f, Randomiser().randomiseFloat(canvasHeight))
            2 -> return floatArrayOf(Randomiser().randomiseFloat(canvasWidth), canvasHeight.toFloat())
            3 -> return floatArrayOf(canvasWidth.toFloat(), Randomiser().randomiseFloat(canvasHeight))
        }

        return floatArrayOf(0f,0f)
    }


    fun drawStraightLines(canvas: Canvas, isHorizontal: Boolean) {


        val measure = if (isHorizontal) canvasHeight else canvasWidth
        val strokeWidth = (measure / ((colors.size*2)-1)).toFloat()

        var paint = Paint()
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true


        val middle = (measure / 2).toFloat()

        for ((index, value) in colors.withIndex()) {
            paint.color = Color.parseColor(value)

            val multiplier = index * strokeWidth

            if(isHorizontal)
            {
                if (index == 0) {
                    canvas.drawLine(0f, middle, canvasWidth.toFloat(), middle, paint)

                }
                else {
                    canvas.drawLine( 0f, middle - multiplier, canvasWidth.toFloat(), middle - multiplier, paint)
                    canvas.drawLine( 0f, middle + multiplier, canvasWidth.toFloat(), middle + multiplier, paint)
                }
            }else{
                if (index == 0)
                    canvas.drawLine(middle, 0f, middle, canvasHeight.toFloat(), paint)
                else {
                    canvas.drawLine( middle - multiplier, 0f, middle - multiplier, canvasHeight.toFloat(), paint)
                    canvas.drawLine( middle + multiplier, 0f, middle + multiplier, canvasHeight.toFloat(), paint)
                }
            }



        }

    }

    fun drawDiagonalLines(canvas: Canvas) {

        val measure = if (canvasHeight > canvasWidth) canvasWidth else canvasHeight
        val strokeWidth = (measure / (colors.size)).toFloat()

        var paint = Paint()
        paint.style = Paint.Style.FILL
        paint.strokeWidth = strokeWidth
        paint.isAntiAlias = true


        val vector = getVector(canvasWidth.toFloat(), canvasHeight.toFloat())
        val vectorRotatePositive = floatArrayOf(vector[1], -vector[0])
        val vectorRotateNegative = floatArrayOf(-vector[1], vector[0])

        for ((index, value) in colors.withIndex()) {
            paint.color = Color.parseColor(value)

            val multiplier = index * strokeWidth

            if (index == 0)
                canvas.drawLine(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), paint)
            else {
                canvas.drawLine(vectorRotatePositive[0] * multiplier, vectorRotatePositive[1] * multiplier, canvasWidth.toFloat() * multiplier, canvasHeight.toFloat() * multiplier, paint)
                canvas.drawLine(vectorRotateNegative[0] * multiplier, vectorRotateNegative[1] * multiplier, canvasWidth.toFloat() * multiplier, canvasHeight.toFloat() * multiplier, paint)
            }


        }

    }


}