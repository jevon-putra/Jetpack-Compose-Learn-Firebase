package com.jop.jetpack.firebase.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import kotlin.math.cos

class CircleOverlay(private val lat: Double, private val lon: Double, private val radius: Float) : Overlay() {

    override fun draw(canvas: Canvas, mapView: MapView, shadow: Boolean) {

        val projection = mapView.projection
        val point = Point()
        val geoPoint = GeoPoint(lat, lon)

        projection.toPixels(geoPoint, point)
        val circleRadius: Float = projection.metersToEquatorPixels(radius) * (1 / cos(Math.toRadians(lat))).toFloat()

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)

        circlePaint.color = Color.BLUE
        circlePaint.alpha = 30
        circlePaint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), circleRadius, circlePaint)

        super.draw(canvas, mapView, shadow)
    }
}