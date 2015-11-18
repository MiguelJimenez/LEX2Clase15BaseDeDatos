package mijimweb.com.lex2clase15basedatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by Miguel on 18/11/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "data/data/mijimweb.com.lex2clase15basedatos/databases/";
    private static final String DB_NAME = "bdejemplo.sqlite";
    private SQLiteDatabase myDatabase;
    private Context myContext;

    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        SQLiteDatabase dbRead = null;
        if (dbExist) {

        } else {
            dbRead = this.getReadableDatabase();
            dbRead.close();

            try {
                copyDataBase();
            } catch (Exception e) {
                throw new Error("Error copiando la base de datos");
            }

        }
    }

    public void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) != -1) {
            if (length > 0) {
                myOutput.write(buffer, 0, length);
            }
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String myPath = DB_PATH + DB_NAME;

        try {
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            File dbFile = new File(DB_PATH + DB_NAME);
            return dbFile.exists();
        }
// Prueba de commit
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;


    }

    public void openDataBase() throws SQLiteException {
        String mypath = DB_PATH + DB_NAME;
        myDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor fetchTeacher(String palabra) throws SQLException {
        String[] campos = new String[]{"id", "nombre"};
        String[] argumentos = new String[]{palabra};

        Cursor qCursor = myDatabase.query("maestros", campos, "nombre=?", argumentos, null, null, null);
        if (qCursor != null) {
            qCursor.moveToFirst();
        }
        return qCursor;

    }
}
