package com.albertribas_ericcaballero_albertmarlet.proyecto_final.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *  //Open 'DBUsuarios' db in write mode
 QuotesReaderDbHelper usdbh =
 new QuotesReaderDbHelper(this, "Usuarios", null, 1);

 SQLiteDatabase db = usdbh.getWritableDatabase();

 // //If we open correctly db
 if (db != null) {

 if(hayDatos){
 db.execSQL("UPDATE Usuarios SET phone = '"+ numero1 +"',name='"+nombre1+"',ruta='"+Tab3.picturePath[1]+"' WHERE id=1");
 db.execSQL("UPDATE Usuarios SET phone = '"+ numero2 +"',name='"+nombre2+"',ruta='"+Tab3.picturePath[2]+"' WHERE id=2");
 db.execSQL("UPDATE Usuarios SET phone = '"+ numero3 +"',name='"+nombre3+"',ruta='"+Tab3.picturePath[3]+"' WHERE id=3");
 db.execSQL("UPDATE Usuarios SET phone = '"+ numero4 +"',name='"+nombre4+"',ruta='"+Tab3.picturePath[4]+"' WHERE id=4");
 db.execSQL("UPDATE Usuarios SET phone = '"+ numero5 +"',name='"+nombre5+"',ruta='"+Tab3.picturePath[5]+"' WHERE id=5");
 }else{
 db.execSQL("INSERT INTO Usuarios (id, phone, name, ruta) " + "VALUES (" + "1, " + numero1 + ", '" + nombre1 + "', '" + Tab3.picturePath[1] + "')");
 db.execSQL("INSERT INTO Usuarios (id, phone, name, ruta) " + "VALUES (" + "2, " + numero2 + ", '" + nombre2 + "', '" + Tab3.picturePath[2] + "')");
 db.execSQL("INSERT INTO Usuarios (id, phone, name, ruta) " + "VALUES (" + "3, " + numero3 + ", '" + nombre3 + "', '" + Tab3.picturePath[3] + "')");
 db.execSQL("INSERT INTO Usuarios (id, phone, name, ruta) " + "VALUES (" + "4, " + numero4 + ", '" + nombre4 + "', '" + Tab3.picturePath[4] + "')");
 db.execSQL("INSERT INTO Usuarios (id, phone, name, ruta) " + "VALUES (" + "5, " + numero5 + ", '" + nombre5 + "', '" + Tab3.picturePath[5] + "')");
 hayDatos = true;
 }


 //Close db
 db.close();
 */
public class QuotesReaderDbHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE incidencias (id integer primary key autoincrement not null, lat VARCHAR2, lon VARCHAR2, url VARCHAR2)";

    public QuotesReaderDbHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Usuarios");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}