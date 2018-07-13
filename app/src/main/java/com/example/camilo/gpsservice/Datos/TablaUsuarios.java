package com.example.camilo.gpsservice.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.camilo.gpsservice.Entidades.EnUsuario;

import java.util.ArrayList;



public final class TablaUsuarios {

    private db_Helper helper;

    public TablaUsuarios(Context context) {
        helper = new db_Helper(context);
    }


    public static final String TABLE_NAME="usuarios";
    public static final String _ID ="Id";
    public static final String USUARIO ="Usuario";
    public static final String NOMBRE ="Nombre";
    public static final String BASE ="Base";
    public static final String CLAVE ="Clave";
    public static final String TOKEN ="Token";
    public static final String CODE_USER ="CodeUser";
    public static final String ESTADO ="Estado";


    private static final String TEXT_TYPE = " TEXT";
    private static final String BLOB_TYPE = " BLOB";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_USUARIOS =
            "CREATE TABLE " + TablaUsuarios.TABLE_NAME + " (" +
                    TablaUsuarios._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TablaUsuarios.USUARIO + TEXT_TYPE+" UNIQUE NOT NULL " + COMMA_SEP +
                    TablaUsuarios.NOMBRE + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.BASE + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.CLAVE + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.TOKEN + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.CODE_USER + TEXT_TYPE +COMMA_SEP +
                    TablaUsuarios.ESTADO + TEXT_TYPE +" )";

    public static final String SQL_DELETE_USUARIOS ="DROP TABLE IF EXISTS " + TablaUsuarios.TABLE_NAME;



    public boolean GuardarUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getWritableDatabase();

        try{
            ContentValues values = new ContentValues();
            values.put(USUARIO, usuario.getNombreDeUsuario());
            values.put(NOMBRE, usuario.getNombres());
            values.put(BASE, usuario.getBase());
            values.put(CLAVE, usuario.getClave());
            values.put(TOKEN, usuario.getToken());
            values.put(CODE_USER, usuario.getCodeUser());
            //inserta los datos y devuelve la clave primaria del registro insertado
            long newRowId = db.insert(TABLE_NAME, null, values);
            if(newRowId==-1){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }

    }
    public boolean ActualizarUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getReadableDatabase();
        try{
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(USUARIO, usuario.getNombreDeUsuario());
            values.put(NOMBRE, usuario.getNombres());
            values.put(BASE, usuario.getBase());
            values.put(CLAVE, usuario.getClave());
            values.put(TOKEN, usuario.getToken());
            values.put(CODE_USER, usuario.getCodeUser());

            // Which row to update, based on the title
            String selection = USUARIO + " LIKE ?";
            String[] selectionArgs = { usuario.getNombreDeUsuario() };

            int count = db.update(
                    TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }
    public boolean EliminarUsuario(String usuario){
        try{
            SQLiteDatabase db = helper.getWritableDatabase();
            // Define 'where' part of query.
            String selection = USUARIO + " LIKE ?";
            // Specify arguments in placeholder order.
            String[] selectionArgs = { usuario };
            // Issue SQL statement.
            db.delete(TABLE_NAME, selection,selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    public EnUsuario ValidarUsuario(String Username,String Clave){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();
        // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                NOMBRE,
                USUARIO,
                CLAVE,
                BASE,
                TOKEN,
                CODE_USER,
                ESTADO
        };
        // Filtrar resultados DONDE "USUARIO" = "Mi USUARIO"
        String selection = TablaUsuarios.USUARIO + " = ?";
        String[] selectionArgs = { Username };

// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    selection,                                // Las columnas para la cláusula WHERE
                    selectionArgs,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );

            c.moveToFirst();//mueve el cursor al primer registro del resultSet
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setNombres(c.getString(1));
            usuario.setNombreDeUsuario(c.getString(2));
            usuario.setClave(c.getString(3));
            usuario.setBase(c.getString(4));
            usuario.setToken(c.getString(5));
            usuario.setCodeUser(c.getString(6));
            usuario.setEstado(c.getString(7));

            if(usuario.getClave().equals(Clave)){
                return  usuario;
            }else{
                return null;
            }


        }catch(Exception e){
            return null;
        }finally {
            db.close();
        }
    }
    public EnUsuario BuscarUsuarioId(String Id){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();

// Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                NOMBRE,
                USUARIO,
                CLAVE,
                BASE,
                TOKEN,
                CODE_USER,
                ESTADO

        };

// Filtrar resultados DONDE "título" = "Mi título"
        String selection = TablaUsuarios._ID + " = ?";
        String[] selectionArgs = { Id };

// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    selection,                                // Las columnas para la cláusula WHERE
                    selectionArgs,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );

            c.moveToFirst();//mueve el cursor al primer registro del resultSet
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setNombres(c.getString(1));
            usuario.setNombreDeUsuario(c.getString(2));
            usuario.setClave(c.getString(3));
            usuario.setBase(c.getString(4));
            usuario.setToken(c.getString(5));
            usuario.setCodeUser(c.getString(6));
            usuario.setEstado(c.getString(7));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }
    public EnUsuario BuscarUsuarioUserName(String userName){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();

// Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                NOMBRE,
                USUARIO,
                CLAVE,
                BASE,
                TOKEN,
                CODE_USER,
                ESTADO

        };

// Filtrar resultados DONDE "título" = "Mi título"
        String selection = USUARIO + " = ?";
        String[] selectionArgs = { userName };

// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    selection,                                // Las columnas para la cláusula WHERE
                    selectionArgs,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );

            c.moveToFirst();//mueve el cursor al primer registro del resultSet
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setNombres(c.getString(1));
            usuario.setNombreDeUsuario(c.getString(2));
            usuario.setClave(c.getString(3));
            usuario.setBase(c.getString(4));
            usuario.setToken(c.getString(5));
            usuario.setCodeUser(c.getString(6));
            usuario.setEstado(c.getString(7));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }
    public boolean ActualizarEstadoUsuario(EnUsuario usuario){
        SQLiteDatabase db = helper.getReadableDatabase();
        try{

            // New value for one column
            ContentValues values = new ContentValues();
            values.put(ESTADO, usuario.getEstado());

            // Which row to update, based on the title
            String selection = USUARIO + " LIKE ?";
            String[] selectionArgs = { usuario.getNombreDeUsuario() };

            int count = db.update(
                    TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            db.close();
        }
    }
    public EnUsuario BuscarUsuarioActivo(){
        EnUsuario usuario=new EnUsuario();
        SQLiteDatabase db = helper.getReadableDatabase();
        // Definir una proyección que especifique qué columnas de la base de datos
// lo usarás después de esta consulta.
        String[] projection = {
                _ID,
                NOMBRE,
                USUARIO,
                CLAVE,
                BASE,
                TOKEN,
                CODE_USER,
                ESTADO
        };
// Filtrar resultados DONDE "título" = "Mi título"
        String selection = TablaUsuarios.ESTADO + " = ?";
        String[] selectionArgs = { "Activo" };
// Cómo desea que se clasifiquen los resultados en el Cursor resultante
        // String sortOrder =TablaUsuarios._ID + " DESC";
        try {
            Cursor c = db.query(
                    TABLE_NAME,
                    projection,                               // Las columnas para regresar
                    selection,                                // Las columnas para la cláusula WHERE
                    selectionArgs,                            // Los valores para la cláusula WHERE
                    null,                                     // no agrupe las filas
                    null,                                     // no filtrar por grupos de filas
                    null                                 // El orden de clasificación (sortOrder)
            );

            c.moveToFirst();//mueve el cursor al primer registro del resultSet
            usuario.set_id(Integer.parseInt(c.getString(0)));
            usuario.setNombres(c.getString(1));
            usuario.setNombreDeUsuario(c.getString(2));
            usuario.setClave(c.getString(3));
            usuario.setBase(c.getString(4));
            usuario.setToken(c.getString(5));
            usuario.setCodeUser(c.getString(6));
            usuario.setEstado(c.getString(7));

            return usuario;


        }catch(Exception e){
            return null;
        }
    }

}


