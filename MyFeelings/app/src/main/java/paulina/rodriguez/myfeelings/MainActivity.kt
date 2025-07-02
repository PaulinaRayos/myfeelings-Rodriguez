package paulina.rodriguez.myfeelings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import paulina.rodriguez.myfeelings.utilities.CustomBarDrawable
import paulina.rodriguez.myfeelings.utilities.CustomCircleDrawable
import paulina.rodriguez.myfeelings.utilities.Emociones
import paulina.rodriguez.myfeelings.utilities.JSONFile

class MainActivity : AppCompatActivity() {


    var jsonFile: JSONFile? = null
    var veryhappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    lateinit var graphVeryHappy: View
    lateinit var graphHappy: View
    lateinit var graphNeutral: View
    lateinit var graphSad: View
    lateinit var graphVerySad: View
    lateinit var graph: View
    lateinit var icon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()
        graphVeryHappy = findViewById(R.id.graphVeryHappy)
        graphHappy = findViewById(R.id.graphHappy)
        graphNeutral = findViewById(R.id.graphNeutral)
        graphSad = findViewById(R.id.graphSad)
        graphVerySad = findViewById(R.id.graphVerySad)
        graph = findViewById(R.id.graph)
        icon = findViewById(R.id.icon)

        val veryHappyButton = findViewById<ImageButton>(R.id.veryHappyButton)
        val happyButton = findViewById<ImageButton>(R.id.happyButton)
        val neutralButton = findViewById<ImageButton>(R.id.neutralButton)
        val sadButton = findViewById<ImageButton>(R.id.sadButton)
        val verySadButton = findViewById<ImageButton>(R.id.verySadButton)
        val guardarButton = findViewById<Button>(R.id.guardarButton)


        // Carga los datos si existen previamente guardados
        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            // Si no hay datos, dibuja gráficas vacías
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo

            // Dibuja las barras con valores en 0
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("MuyFeliz", 0.0F, R.color.mustard, veryhappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, happy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, neutral))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, sad))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, verysad))
        } else{
            // Si hay datos previos, actualiza la gráfica y el ícono
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryhappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener {
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    //Lee los datos del archivo JSON (si existen) y actualiza las variables globales.
    fun fetchingData(){
        try{
            var json: String = jsonFile?.getData(this)?: ""
            if(json != ""){
                this.data = true
                var jsonArray: JSONArray = JSONArray(json)

                this.lista = parseJson(jsonArray)
                // Asigna cada emoción a su variable correspondiente
                for(i in lista){
                    when (i.nombre){
                        "Muy feliz" -> veryhappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else{
                this.data = false
            }
        } catch (exception: JSONException){
            exception.printStackTrace()
        }

    }

    //Convierte un JSONArray en una lista de objetos Emociones.
    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()

        for (i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception: JSONException){
                exception.printStackTrace()
            }
        }

        return lista
    }

    //Calcula porcentajes de emociones, actualiza la lista y redibuja gráficas.
    fun actualizarGrafica(){

        val total = veryhappy+happy+neutral+verysad+sad

        // Cálculo de porcentajes
        var pVH: Float = (veryhappy*100/total).toFloat()
        var pH: Float = (happy*100/total).toFloat()
        var pN: Float = (neutral*100/total).toFloat()
        var pS: Float = (sad*100/total).toFloat()
        var pVS: Float = (verysad*100/total).toFloat()
        // Registro para debug
        Log.d("porcentajes", "very happy "+pVH)
        Log.d("porcentajes", "happy "+pVH)
        Log.d("porcentajes", "neutral "+pVH)
        Log.d("porcentajes", "sad "+pVH)
        Log.d("porcentajes", "very sad "+pVH)
        // Se reinicia y reconstruye la lista con nuevos valores
        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryhappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verysad))
        // Actualización de gráficas
        val fondo = CustomCircleDrawable(this, lista)

        graphVeryHappy.background = CustomBarDrawable(this, Emociones("MuyFeliz", pVH, R.color.mustard, veryhappy))
        graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this, Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", pVS, R.color.deepBlue, verysad))

        graph.background = fondo

    }

    //Cambia el ícono principal según la emoción con mayor número de votos.
    fun iconoMayoria(){
        // Compara valores de emociones y actualiza el ícono visual
        if(happy>veryhappy && happy>neutral && happy>sad && happy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(veryhappy>happy && veryhappy>neutral && veryhappy>sad && veryhappy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }
        if(neutral>veryhappy && neutral>happy && neutral>sad && neutral>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if(sad>veryhappy && sad>neutral && sad>happy && sad>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if(verysad>veryhappy && verysad>neutral && verysad>sad && verysad>veryhappy){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }

    }


    //Guarda los datos actuales de emociones en formato JSON.
    fun guardar(){
        var jsonArray = JSONArray()
        var o: Int = 0
        // Convierte cada emoción en un objeto JSON
        for(i in lista){
            Log.d("objetos", i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }
        // Guarda el contenido en un archivo
        jsonFile?.saveData(this, jsonArray.toString())

        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }

}