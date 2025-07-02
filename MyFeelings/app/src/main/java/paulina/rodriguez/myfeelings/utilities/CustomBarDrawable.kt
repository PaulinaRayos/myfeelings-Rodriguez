package paulina.rodriguez.myfeelings.utilities

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import paulina.rodriguez.myfeelings.R


//Clase personalizada para dibujar una barra horizontal que representa el porcentaje de una emoción.
//Se utiliza como fondo de cada View para mostrar visualmente cuánto representa cada emoción.
class CustomBarDrawable : Drawable {
    // Coordenadas del rectángulo principal
    var coordenadas : RectF? = null
    var context : Context? = null
    // Emoción que se representa con esta barra
    var emocion : Emociones? = null
    //Constructor que recibe el contexto y el objeto emoción.
    constructor(context: Context, emocion: Emociones){

        this.context = context
        this.emocion = emocion

    }
    //Metodo principal que se llama automáticamente cuando se necesita dibujar la barra.
    //Aquí se pinta el fondo gris y luego la barra coloreada según el porcentaje de la emoción.
    override fun draw(p0: Canvas) {
        // Pintura para el fondo gris
        val fondo : Paint = Paint()
        fondo.style = Paint.Style.FILL
        fondo.isAntiAlias = true
        fondo.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray
        // Calculamos el ancho y alto disponibles para dibujar
        val ancho : Float = (p0.width - 10).toFloat()
        val alto : Float = (p0.height - 10).toFloat()
        // Rectángulo completo del fondo
        coordenadas = RectF(0.0F, 0.0F, ancho, alto)
        p0.drawRect(coordenadas!!, fondo)
        // Si la emoción está definida, dibujamos la sección coloreada proporcional
        if (this.emocion != null){
            // Convertimos el porcentaje en una longitud horizontal
            val porcentaje : Float = this.emocion!!.porcentaje * (p0.width -10) / 100
            // Rectángulo que representa el porcentaje de la emoción
            var coordenadas2 = RectF(0.0F, 0.0F, porcentaje, alto)
            // Pintura con el color correspondiente a la emoción
            var seccion : Paint = Paint()
            seccion.style = Paint.Style.FILL
            seccion.isAntiAlias = true
            seccion.color = ContextCompat.getColor(this.context!!, emocion!!.color)
            // Dibujamos la barra de color por encima del fondo
            p0.drawRect(coordenadas2!!, seccion)

        }

    }

    override fun setAlpha(p0: Int) {

    }

     override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

}