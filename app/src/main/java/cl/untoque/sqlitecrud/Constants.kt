package cl.untoque.sqlitecrud

object Constants {

    const val DB_NAME = "PERSON_INFO_DB"
    const val DB_VERSION = 1
    const val TABLE_NAME = "PERSON_INFO_TABLE"

    const val C_ID = "ID"
    const val C_IMAGE = "IMAGE"
    const val C_NAME = "NAME"
    const val C_AGE = "AGE"
    const val C_PHONE = "PHONE"
    const val C_ADD_TIMESTAMP = "ADD_TIMESTAMP"
    const val C_UPDATED_TIMESTAMP = "UPDATED_TIMESTAMP"

    const val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NAME + " TEXT, "
            + C_AGE + " TEXT, "
            + C_PHONE + " TEXT, "
            + C_ADD_TIMESTAMP + " TEXT, "
            + C_UPDATED_TIMESTAMP + " TEXT, "
            + ");" )
}