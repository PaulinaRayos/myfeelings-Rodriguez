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

//Clase que dibuja una gráfica circular (tipo dona) con diferentes segmentos,
// cada uno representando una emoción con su respectivo porcentaje.
class CustomCircleDrawable : Drawable {
    // Área que se usará para dibujar el círculo
    var coordenadas: RectF? = null
    // Variables para controlar el ángulo de dibujo
    var anguloBarrido: Float = 0.0F
    var anguloInicio: Float = 0.0F
    // Grosor del fondo gris y del trazo de cada emoción
    var grosorMetrica: Int = 0
    var grosorFondo: Int = 0
    // Contexto y lista de emociones a graficar
    var context: Context? = null
    var emociones = ArrayList<Emociones>()
    //Constructor que recibe contexto y lista de emociones a graficar.
    constructor(context: Context, emociones: ArrayList<Emociones>){

        this.context = context
        grosorMetrica = 10
        grosorFondo = 15 // Grosor fijo para el fondo gris
        this.emociones = emociones
        // Se obtiene el grosor del trazo desde dimens.xml
        grosorMetrica = context.resources.getDimensionPixelSize(R.dimen.graphWith)

    }
    //Metodo que dibuja la gráfica circular en el canvas.
    override fun draw(p0: Canvas) {
        // Pintura para el fondo gris circular
        val fondo: Paint = Paint()
        fondo.style = Paint.Style.STROKE
        fondo.strokeWidth = (this.grosorFondo).toFloat()
        fondo.isAntiAlias = true
        fondo.strokeCap = Paint.Cap.ROUND
        fondo.color = context?.resources?.getColor(R.color.gray) ?: R.color.gray

        // Define el área donde se dibujará el arco (dejando márgenes)
        val ancho: Float = (p0.width -25).toFloat()
        val alto: Float = (p0.height - 25).toFloat()

        coordenadas = RectF(25.0F, 25.0F, ancho, alto)

        // Dibuja el círculo gris de fondo
        p0.drawArc(coordenadas!!, 0.0F, 360.0F, false, fondo)
        // Si hay emociones, se dibujan cada una como un segmento circular
        if (emociones.size != 0){
            for (e in emociones){
                // Calcula cuántos grados representa la emoción
                val degree: Float = (e.porcentaje*360)/100
                this.anguloBarrido = degree
                // Pintura para cada segmento de emoción
                var seccion : Paint = Paint()
                seccion.style = Paint.Style.STROKE
                seccion.isAntiAlias = true
                seccion.strokeWidth = (this.grosorMetrica).toFloat()
                seccion.strokeCap = Paint.Cap.SQUARE
                seccion.color = ContextCompat.getColor(this.context!!, e.color)
                // Dibuja el segmento correspondiente a esta emoción
                p0.drawArc(coordenadas!!, this.anguloInicio, this.anguloBarrido, false, seccion)
                // Suma el ángulo actual para que el siguiente segmento empiece después
                this.anguloInicio += this.anguloBarrido
            }
        }
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(p0: ColorFilter?) {

    }


}