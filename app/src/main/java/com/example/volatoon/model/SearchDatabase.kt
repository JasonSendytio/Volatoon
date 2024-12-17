package com.example.volatoon.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class SearchDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "search_history.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "recent_searches"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SEARCH_TEXT = "search_text"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SEARCH_TEXT TEXT UNIQUE,
                $COLUMN_TIMESTAMP INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addSearch(searchText: String) {
        val db = this.writableDatabase
        Log.d("SearchDatabase", "Adding search: $searchText")
        val values = ContentValues().apply {
            put(COLUMN_SEARCH_TEXT, searchText)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }
        val id = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        Log.d("SearchDatabase", "Added search with id: $id")

    }

    fun getRecentSearches(): List<RecentSearch> {
        val searches = mutableListOf<RecentSearch>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC",
            "10" // Limit to 10 recent searches
        )

        with(cursor) {
            while (moveToNext()) {
                searches.add(
                    RecentSearch(
                        id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                        searchText = getString(getColumnIndexOrThrow(COLUMN_SEARCH_TEXT)),
                        timestamp = getLong(getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                    )
                )
            }
        }
        cursor.close()
        return searches
    }

    fun deleteSearch(searchText: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_SEARCH_TEXT = ?", arrayOf(searchText))
    }
}