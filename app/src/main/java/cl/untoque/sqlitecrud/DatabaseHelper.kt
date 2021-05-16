package cl.untoque.sqlitecrud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?): SQLiteOpenHelper(
    context,
    Constants.DB_NAME,
    null,
    Constants.DB_VERSION
) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(Constants.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + Constants.CREATE_TABLE)
        onCreate(db)
    }

    fun insertInfo(
        image: String?,
        name: String?,
        age: String?,
        phone: String?,
        addTimeStamp: String?,
        updatedTimeStamp: String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(Constants.C_IMAGE, image)
        values.put(Constants.C_NAME, name)
        values.put(Constants.C_AGE, age)
        values.put(Constants.C_PHONE, phone)
        values.put(Constants.C_ADD_TIMESTAMP, addTimeStamp)
        values.put(Constants.C_UPDATED_TIMESTAMP, updatedTimeStamp)

        val id = db.insert(Constants.TABLE_NAME, null, values)
        db.close()
        return id
    }
}