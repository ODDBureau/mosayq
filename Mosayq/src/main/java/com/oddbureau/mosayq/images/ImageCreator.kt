package com.oddbureau.mosayq.images

import android.graphics.*
import com.oddbureau.mosayq.data.models.Pattern
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
open class ImageCreator( val pattern: Pattern, val colors: ArrayList<String>) {

    var canvasWidth = CanvasData.width * CanvasData.multiplier * 2
    var canvasHeight = CanvasData.height * 2

    open fun generateBitmap(): Bitmap {
        var bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        return bitmap
    }

    fun generateCanvas(bitmap: Bitmap): Canvas {
        val canvas = Canvas(bitmap)
        return canvas
    }

/*
    open fun downscaleBitmap(image: Bitmap): Bitmap{
        return Bitmap.createScaledBitmap(image, canvasWidth / 2,canvasHeight/2, true)
    }
    */

    open fun downscaleBitmap(bitmap: Bitmap): Bitmap {

        var newWidth = canvasWidth / 2
        var newHeight = canvasHeight / 2

        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY)

        val canvas = Canvas(scaledBitmap)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bitmap, 0f, 0f, Paint(Paint.FILTER_BITMAP_FLAG))


        return scaledBitmap

    }

    fun getVector(width: Float, height: Float): FloatArray {
        val hip = Math.sqrt(Math.pow(width.toDouble(), 2.0) + Math.pow(height.toDouble(), 2.0)).toFloat()
        return floatArrayOf(width / hip, height / hip)
    }

    fun drawBackground(bitmap: Bitmap, backgroundColor: String) {
        var backgroundCanvas = Canvas(bitmap)
        var backgroundPaint = Paint()
        backgroundPaint.style = Paint.Style.FILL
        backgroundPaint.color = Color.parseColor(backgroundColor)
        backgroundCanvas?.drawRect(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), backgroundPaint)
    }

    fun drawGradientBackground(bitmap: Bitmap, fromColor: String, toColor: String)
    {
        var backgroundCanvas = Canvas(bitmap)
        var backgroundPaint = Paint()
        backgroundPaint.shader = LinearGradient(canvasWidth.toFloat() / 2f, 0f, canvasWidth.toFloat() / 2f, canvasHeight.toFloat(), Color.parseColor(fromColor), Color.parseColor(toColor), Shader.TileMode.MIRROR)
        backgroundCanvas?.drawRect(0f, 0f, canvasWidth.toFloat(), canvasHeight.toFloat(), backgroundPaint)
    }

    fun drawTriangle(vertices: FloatArray, paint: Paint, canvas: Canvas) {

        val path = Path()
        path.fillType = Path.FillType.EVEN_ODD
        path.moveTo(vertices[0], vertices[1])
        path.lineTo(vertices[2], vertices[3])
        path.lineTo(vertices[4], vertices[5])
        path.close()

        canvas.drawPath(path, paint)
    }

}