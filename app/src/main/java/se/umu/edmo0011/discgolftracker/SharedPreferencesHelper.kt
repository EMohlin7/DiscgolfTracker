package se.umu.edmo0011.discgolftracker

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val THROWS_KEY = "throws_key"
const val MATCHES_KEY = "matches_key"

object SharedPreferencesHelper
{
    fun <T> save(context: Context, item: T, key: String)
    {
        val gson = Gson()
        val json = gson.toJson(item)

        val editor = getSharedPreferences(context).edit()
        editor.putString(key, json)
        editor.apply()
    }
    fun save(context: Context, value: String, key: String)
    {
        val editor = getSharedPreferences(context).edit()
        editor.putString(key, value)
        editor.apply()
    }
    inline fun <reified T> get(context: Context, key: String): T?
    {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(key, null)

        val itemType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, itemType) ?: null
    }

    fun <T> saveList(context: Context, list: List<T>, key: String)
    {
        val gson = Gson()
        val json = gson.toJson(list)

        val editor = getSharedPreferences(context).edit()
        editor.putString(key, json)
        editor.apply()
    }

    inline fun <reified T> getList(context: Context, key: String): List<T>
    {
        val gson = Gson()
        val json = getSharedPreferences(context).getString(key, null)

        val itemType = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, itemType) ?: emptyList()
    }

    fun getSharedPreferences(context: Context): SharedPreferences
    {
        return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }
}