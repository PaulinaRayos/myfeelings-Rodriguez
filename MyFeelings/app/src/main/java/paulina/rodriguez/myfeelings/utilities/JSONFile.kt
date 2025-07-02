package paulina.rodriguez.myfeelings.utilities

import android.content.Context
import android.util.Log
import java.io.IOException


//Clase utilitaria para guardar y leer un archivo JSON local con las emociones del usuario.
// Se usa como una especie de "base de datos" ligera para persistencia simple en almacenamiento interno.
class JSONFile {
    // Nombre del archivo JSON donde se almacenarán los datos
    val MY_FEELINGS = "data.json"

    constructor(){

    }
    // Metodo para guardar un string en el archivo JSON (sobrescribe el contenido anterior).
    fun saveData(context: Context, json: String){
        try{
            // Abre (o crea) el archivo en modo privado (solo la app puede acceder)
            context.openFileOutput(MY_FEELINGS, Context.MODE_PRIVATE).use {
                // Escribe el contenido en formato de bytes
                it.write(json.toByteArray())
            }
        } catch(e: IOException){
            // Si hay error, se registra en Logcat
            Log.e("GUARDAR", "Error in Writing: " + e.localizedMessage)
        }

    }
    //Metodo para leer el contenido del archivo JSON y devolverlo como string.
    fun getData(context: Context): String {
        try{
            // Abre el archivo y lee la primera línea completa todo el JSON está en una sola línea
            return context.openFileInput(MY_FEELINGS).bufferedReader().readLine()
        } catch (e: IOException){
            // Si ocurre error al leer, lo registra y devuelve string vacío
            Log.e("OBTENER", "Error in fetching data: " + e.localizedMessage)
            return ""
        }

    }

}