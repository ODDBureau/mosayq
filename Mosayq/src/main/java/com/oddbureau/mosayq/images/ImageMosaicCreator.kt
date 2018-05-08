package com.oddbureau.mosayq.images

import android.graphics.*
import com.oddbureau.mosayq.algorithms.polygons.PolygonDrawingUtil
import com.oddbureau.mosayq.algorithms.voronoi.Edge
import com.oddbureau.mosayq.algorithms.voronoi.Point
import com.oddbureau.mosayq.algorithms.voronoi.Voronoi
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
class ImageMosaicCreator : ImageCreator {

    constructor(pattern: Pattern, colors: ArrayList<String>) : super(pattern, colors)

    override fun generateBitmap(): Bitmap {

        var bitmap = super.generateBitmap()
        val canvas = super.generateCanvas(bitmap)

        val backgroundColor = colors[Randomiser().randomiseInt(colors.size)]
        drawBackground(bitmap, backgroundColor)


        when (pattern.category) {
            "arcs" -> {
                colors.remove(backgroundColor)
                drawArcs(canvas, backgroundColor, 10, 60, 100f)
            }

            "squares" -> {
                //colors.remove(backgroundColor)
                drawSquares(canvas, backgroundColor, 14f, 25)
            }

            "polygons" -> {
                colors.remove(backgroundColor)
                drawMosaicPolygons(canvas, 15)
            }

            "voronoi-triangles" -> {
                colors.remove(backgroundColor)
                drawVoronoiDiagram(canvas, backgroundColor, 200, true)
            }

            "voronoi-polygons" -> {
                colors.remove(backgroundColor)
                drawVoronoiDiagram(canvas, backgroundColor, 200, false)
            }

            "pixels" -> {
                drawTriangles(canvas, 50, true)
            }

            "triangles" -> {
                drawTriangles(canvas, 30, false)
            }

            else -> {

            }
        }

        return downscaleBitmap(bitmap)
    }


    fun drawTriangles(canvas: Canvas, divider: Int, isPixels: Boolean){

        var paintFill = Paint()
        paintFill.style = Paint.Style.FILL

        val numberOfColors = colors.size

        var size = (canvasWidth / divider).toFloat()
        var numColumns = Math.ceil((canvasWidth / size).toDouble()).toInt()
        var numRows = Math.ceil((canvasHeight / size).toDouble()).toInt()

        for (x in 0..numColumns) {
            for (y in 0..numRows) {
                paintFill.color = Color.parseColor(colors[Randomiser().randomiseInt(numberOfColors)])

                drawTriangle(
                        floatArrayOf(
                            x * size, y * size,
                            x * size, (y+1) * size,
                            (x+1) * size, y * size
                        ),
                        paintFill, canvas
                )

                if(!isPixels)
                    paintFill.color = Color.parseColor(colors[Randomiser().randomiseInt(numberOfColors)])

                drawTriangle(
                        floatArrayOf(
                                (x+1) * size, y * size,
                                (x+1) * size, (y+1) * size,
                                x * size, (y+1) * size
                        ),
                        paintFill, canvas
                )
            }
        }

    }

    fun drawVoronoiDiagram(canvas: Canvas, backgroundColor: String, siteNumber: Int, isTriangle: Boolean) {

        var paint = Paint()
        paint.style = Paint.Style.FILL

        var paintStroke = Paint()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.strokeWidth = 10f
        paintStroke.isAntiAlias = true

        val points = ArrayList<Point>()

        for (i in 0..siteNumber - 1) {
            val x = Randomiser().randomiseDouble()
            val y = Randomiser().randomiseDouble()
            points.add( Point( x, y, Color.parseColor(colors[Randomiser().randomiseInt(colors.size)]) ) )
        }

        val diagram = Voronoi(points)

        for (e in diagram.edges) {
            drawVoronoiEdge(e.site_left, e, paint, canvas, isTriangle)
            drawVoronoiEdge(e.site_right, e, paint, canvas, isTriangle)
        }

        for (e in diagram.edges) {
            paintStroke.color = Color.parseColor(backgroundColor)
            canvas.drawLine(e.start.x.toFloat()*canvasWidth, e.start.y.toFloat()*canvasHeight, e.end.x.toFloat()*canvasWidth, e.end.y.toFloat()*canvasHeight, paintStroke)
        }

    }

    fun drawVoronoiEdge(point: Point, edge: Edge, paint: Paint, canvas: Canvas, isTriangle: Boolean){
        if (point != null) {
            if(isTriangle)
                paint.color = Color.parseColor(colors[Randomiser().randomiseInt(colors.size)])
            else
                paint.color = point.color

            drawTriangle(floatArrayOf(
                    edge.start.x.toFloat() * canvasWidth, edge.start.y.toFloat() * canvasHeight,
                    edge.end.x.toFloat() * canvasWidth, edge.end.y.toFloat() * canvasHeight,
                    point.x.toFloat() * canvasWidth, point.y.toFloat() * canvasHeight
            ), paint, canvas)
        }
    }

    fun drawMosaicPolygons(canvas: Canvas, divider: Int) {

        var paintFill = Paint()
        paintFill.style = Paint.Style.FILL
        paintFill.isAntiAlias = true


        var size = (canvasWidth / divider).toFloat()
        var numColumns = Math.ceil((canvasWidth / size).toDouble()).toInt()
        var numRows = Math.ceil((canvasHeight / size).toDouble()).toInt()

        var sideCount = Randomiser().randomiseIntBetweenWithExclusion(3, 6, 5)

        if(sideCount == 3)
            drawTriangles(canvas, numColumns, numRows, size, paintFill)
        else
            drawPolygons(canvas, numColumns, numRows, sideCount, size, paintFill)


    }

    fun drawPolygons(canvas: Canvas, numColumns: Int, numRows: Int, sideCount: Int, size: Float, paintFill: Paint)
    {
        var firstColorIndex: Int = 0
        var colorIndex: Int
        val numberOfColors = colors.size

        for (x in 0..numColumns) {

            if(firstColorIndex < numberOfColors-1) firstColorIndex++ else firstColorIndex = 0
            colorIndex = firstColorIndex

            for (y in 0..numRows) {

                paintFill.color = Color.parseColor(colors[colorIndex])

                PolygonDrawingUtil().drawPolygon(canvas, sideCount, (x+.5f)*size, (y+.5f)*size, size/2f, 0f, 90f, paintFill)

                if(colorIndex < numberOfColors-1) colorIndex++ else colorIndex = 0
            }
        }
    }

    fun drawTriangles(canvas: Canvas, numColumns: Int, numRows: Int, size: Float, paintFill: Paint)
    {
        var vertices : FloatArray
        var isOdd = true

        var firstColorIndex: Int = 0
        var colorIndex: Int
        val numberOfColors = colors.size

        for (x in 0..numColumns) {

            if(firstColorIndex < numberOfColors-1) firstColorIndex++ else firstColorIndex = 0
            colorIndex = firstColorIndex

            paintFill.color = Color.parseColor(colors[colorIndex])

            for (y in 0..numRows) {

                if(isOdd)
                {
                    vertices = floatArrayOf(
                            (x-.5f)*size, (y+1f)*size,
                            x*size, y*size,
                            (x+.5f)*size, (y+1f)*size
                    )
                }else{
                    vertices = floatArrayOf(
                            x*size, (y+1f)*size,
                            (x+.5f)*size, y*size,
                            (x+1f)*size, (y+1f)*size
                    )
                }

                isOdd = !isOdd

                drawTriangle(vertices, paintFill, canvas)

            }

            //if(colorIndex < numberOfColors-1) colorIndex++ else colorIndex = 0
        }
    }


    fun drawSquares(canvas: Canvas, backgroundColor: String, strokeWidth: Float, divider: Int) {

        var paintFill = Paint()
        paintFill.style = Paint.Style.FILL
        paintFill.isAntiAlias = true

        var paintStroke = Paint()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.color = Color.parseColor(backgroundColor)
        paintStroke.strokeWidth = strokeWidth
        paintStroke.isAntiAlias = true


        val numberOfColors = colors.size

        var size = (canvasWidth / divider).toFloat()
        var numColumns = Math.ceil((canvasWidth / size).toDouble()).toInt()
        var numRows = Math.ceil((canvasHeight / size).toDouble()).toInt()

        for (x in 0..numColumns) {
            for (y in 0..numRows) {
                paintFill.color = Color.parseColor(colors[Randomiser().randomiseInt(numberOfColors)])
                canvas.drawRoundRect(RectF(x * size, y * size, (x + 1) * size, (y + 1) * size), 10f, 10f, paintFill)
                canvas.drawRoundRect(RectF(x * size, y * size, (x + 1) * size, (y + 1) * size), 8f + strokeWidth, 8f + strokeWidth, paintStroke)
            }
        }

    }


    fun drawArcs(canvas: Canvas, strokeColor: String, minAngle: Int, maxAngle: Int, distance: Float) {

        var paint = Paint()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true


        var paintStroke = Paint()
        paintStroke.style = Paint.Style.STROKE
        paintStroke.strokeWidth = 10f
        paintStroke.color = Color.parseColor(strokeColor)
        paintStroke.isAntiAlias = true


        var nextAngle: Float
        var originAngle: Float

        var circleRadius = (Math.sqrt(Math.pow(canvasWidth / 2.0, 2.0) + Math.pow(canvasHeight / 2.0, 2.0))).toFloat()
        var container = RectF(
                (canvasWidth / 2f) - circleRadius,
                (canvasHeight / 2f) - circleRadius,
                canvasWidth.toFloat() + Math.abs((canvasWidth / 2f) - circleRadius),
                canvasHeight.toFloat() + (Math.abs((canvasHeight / 2f) - circleRadius))
        )

        while (circleRadius > distance) {

            originAngle = 0f
            nextAngle = 0f

            do {
                originAngle += nextAngle
                nextAngle = getRandomAngle(minAngle, maxAngle)

                paint.color = Color.parseColor(colors[Randomiser().randomiseInt(colors.size)])

                canvas.drawArc(container, originAngle, nextAngle, true, paint)
                canvas.drawArc(container, originAngle, nextAngle, true, paintStroke)

            } while (originAngle <= 360f)


            circleRadius -= distance
            container.left += distance
            container.top += distance
            container.right -= distance
            container.bottom -= distance

        }

        paint.color = Color.parseColor(colors[Randomiser().randomiseInt(colors.size)])
        canvas.drawCircle(canvasWidth / 2f, canvasHeight / 2f, distance / 2f, paint)

        paintStroke.color = Color.parseColor(strokeColor)
        canvas.drawCircle(canvasWidth / 2f, canvasHeight / 2f, distance / 2f, paintStroke)
    }

    fun getRandomAngle(minAngle: Int, maxAngle: Int): Float {
        return (minAngle + Randomiser().randomiseInt(maxAngle - minAngle)).toFloat()
    }

}