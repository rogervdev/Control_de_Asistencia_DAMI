package edu.cibertec.controldeasistencia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "ControlAsistencia.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        // 🔹 Tabla empleados
        db.execSQL(
            """
            CREATE TABLE empleados (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dni TEXT,
                nombres TEXT,
                apellidos TEXT,
                telefono TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
    CREATE TABLE asistencia (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        empleado_id INTEGER,
        fecha TEXT,
        hora TEXT,
        tipo TEXT,
        FOREIGN KEY (empleado_id) REFERENCES empleados(id)
    )
    """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS empleados")
        onCreate(db)
    }

    // 🔥 Insertar empleado
    fun insertarEmpleado(
        dni: String,
        nombres: String,
        apellidos: String,
        telefono: String
    ): Boolean {

        val db = writableDatabase
        val values = ContentValues()

        values.put("dni", dni)
        values.put("nombres", nombres)
        values.put("apellidos", apellidos)
        values.put("telefono", telefono)

        val resultado = db.insert("empleados", null, values)

        return resultado != -1L
    }

    fun obtenerEmpleados(): MutableList<Empleado> {

        val lista = mutableListOf<Empleado>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM empleados", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val dni = cursor.getString(cursor.getColumnIndexOrThrow("dni"))
                val nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres"))
                val apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"))
                val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))

                lista.add(
                    Empleado(id, dni, nombres, apellidos, telefono)
                )

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return lista
    }

    fun insertarAsistencia(
        empleadoId: Int,
        fecha: String,
        hora: String,
        tipo: String
    ): Boolean {

        val db = writableDatabase
        val values = ContentValues()

        values.put("empleado_id", empleadoId)
        values.put("fecha", fecha)
        values.put("hora", hora)
        values.put("tipo", tipo)

        val resultado = db.insert("asistencia", null, values)

        return resultado != -1L
    }

    fun obtenerAsistenciaDelDia(fecha: String): List<AsistenciaItem> {
        val lista = mutableListOf<AsistenciaItem>()
        readableDatabase.use { db ->
            val query = """
            SELECT a.id, e.nombres, e.apellidos, a.fecha, a.hora, a.tipo
            FROM asistencia a
            INNER JOIN empleados e ON a.empleado_id = e.id
            WHERE a.fecha = ?
            ORDER BY a.hora ASC
        """.trimIndent()

            db.rawQuery(query, arrayOf(fecha)).use { cursor ->
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(0)
                    val nombres = cursor.getString(1)
                    val apellidos = cursor.getString(2)
                    val fechaRegistro = cursor.getString(3)
                    val hora = cursor.getString(4)
                    val tipo = cursor.getString(5)

                    lista.add(
                        AsistenciaItem(
                            id = id,
                            nombreCompleto = "$nombres $apellidos",
                            fecha = fechaRegistro,
                            hora = hora,
                            tipo = tipo
                        )
                    )
                }
            }
        }
        return lista
    }

    fun obtenerEmpleadoPorDni(dni: String): Empleado? {

        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM empleados WHERE dni = ?",
            arrayOf(dni)
        )

        var empleado: Empleado? = null

        if (cursor.moveToFirst()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres"))
            val apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"))
            val telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono"))

            empleado = Empleado(id, dni, nombres, apellidos, telefono)
        }

        cursor.close()
        db.close()

        return empleado
    }

    fun obtenerUltimoRegistro(dni: String): AsistenciaItem? {

        val db = readableDatabase

        val cursor = db.rawQuery(
            """
    SELECT a.id,
           e.nombres || ' ' || e.apellidos AS nombreCompleto,
           a.fecha,
           a.hora,
           a.tipo
    FROM asistencia a
    INNER JOIN empleados e ON a.empleadoId = e.id
    WHERE e.dni = ?
    ORDER BY a.id DESC
    LIMIT 1
    """.trimIndent(),
            arrayOf(dni)
        )

        var registro: AsistenciaItem? = null

        if (cursor.moveToFirst()) {

            registro = AsistenciaItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
            )
        }

        cursor.close()
        db.close()

        return registro
    }

    fun obtenerUltimosRegistros(dni: String): List<AsistenciaItem> {

        val lista = mutableListOf<AsistenciaItem>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            """
        SELECT a.id,
               e.nombres || ' ' || e.apellidos AS nombreCompleto,
               a.fecha,
               a.hora,
               a.tipo
        FROM asistencia a
        INNER JOIN empleados e ON a.empleadoId = e.id
        WHERE e.dni = ?
        ORDER BY a.id DESC
        LIMIT 5
        """.trimIndent(),
            arrayOf(dni)
        )

        while (cursor.moveToNext()) {

            val item = AsistenciaItem(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                nombreCompleto = cursor.getString(cursor.getColumnIndexOrThrow("nombreCompleto")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")),
                hora = cursor.getString(cursor.getColumnIndexOrThrow("hora")),
                tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"))
            )

            lista.add(item)
        }

        cursor.close()
        db.close()

        return lista
    }

}