package com.oddbureau.mosayq.images

import android.graphics.*
import com.oddbureau.mosayq.algorithms.polygons.PolygonDrawingUtil
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

class ImageGeometricCreator : ImageCreator {

    constructor(pattern: Pattern, colors: ArrayList<String>) : super(pattern, colors)

    override fun generateBitmap(): Bitmap {

        var bitmap = super.generateBitmap()
        val canvas = super.generateCanvas(bitmap)

        when (pattern.category) {
            "gradient-simple" -> {
                drawGradient(bitmap)
                drawPolygonInsideOut(canvas, getPaintStroke(12f), 150, (canvasWidth*1.5f).toInt(), false)
            }

            "gradient-spiral" -> {
                drawGradient(bitmap)
                drawPolygonInsideOut(canvas, getPaintStroke(12f), 50, (canvasWidth/2), true)
            }

            "progressive" -> {
                val backgroundColor = colors[Randomiser().randomiseInt(colors.size)]
                drawBackground(bitmap, backgroundColor)
                colors.remove(backgroundColor)

                drawPolygonProgressive(canvas, 15f, 250, (canvasWidth/2))
            }

            else -> {

            }
        }

        return downscaleBitmap(bitmap)
    }

    fun drawGradient(bitmap: Bitmap){
        val toColor = colors[Randomiser().randomiseInt(colors.size)]
        colors.remove(toColor)
        val fromColor = colors[Randomiser().randomiseInt(colors.size)]
        colors.remove(fromColor)

        drawGradientBackground(bitmap, fromColor, toColor)
    }

    fun getPaintStroke( paintStrokeWidth: Float ): Paint{
        var paintStroke = Paint()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.color = Color.parseColor(colors[Randomiser().randomiseInt(colors.size)])
        paintStroke.strokeWidth = paintStrokeWidth
        paintStroke.isAntiAlias = true

        return paintStroke
    }

    fun drawPolygonInsideOut(canvas: Canvas, paintStroke: Paint, standardRadius: Int, maxRadius: Int, isRotation: Boolean) {

        var radius = standardRadius

        val centerX = ( canvasWidth / 2)
        val centerY = ( canvasHeight / 2)

        var verticesNumber = Randomiser().randomiseIntBetween(3, 10)
        var angleRotation = 90f

        while(radius < maxRadius)
        {
            if(isRotation){
                if(angleRotation == 360f)
                    angleRotation = 5f
                else
                    angleRotation += 5f
            }

            PolygonDrawingUtil().drawPolygon(canvas,verticesNumber, centerX.toFloat(), centerY.toFloat(), radius.toFloat(), 0f, angleRotation, paintStroke)

            radius += standardRadius
        }
    }

    fun drawPolygonProgressive(canvas: Canvas, paintStrokeWidth: Float, standardRadius: Int, maxRadius: Int) {

        var radius = standardRadius

        val centerX = ( canvasWidth / 2)
        val centerY = ( canvasHeight / 2)

        var angleRotation = 90f
        var pathPoints : ArrayList<Point>
        var pathPointStroke : Paint

        while(radius < maxRadius)
        {

            var verticesNumber = Randomiser().randomiseIntBetween(3, 10)

            pathPoints = PolygonDrawingUtil().drawPolygon(canvas,verticesNumber, centerX.toFloat(), centerY.toFloat(), radius.toFloat(), 0f, angleRotation, getPaintStroke(paintStrokeWidth))

            pathPointStroke = getPaintStroke(paintStrokeWidth)
            for(point in pathPoints)
            {
                PolygonDrawingUtil().drawPolygon(canvas, verticesNumber, point.x.toFloat(), point.y.toFloat(), radius.toFloat(), 0f, angleRotation, pathPointStroke)
            }

            radius += standardRadius

        }
    }

}