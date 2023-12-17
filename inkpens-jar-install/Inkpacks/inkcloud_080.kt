package Inkpacks

import java.util.prefs.Preferences

class inkcloud_080(val version: String, private var nodeName: String) {
    private var pref = Preferences.userRoot().node(nodeName)

    fun switchSession(newNodeName: String) {
        pref = Preferences.userRoot().node(nodeName)
        nodeName = newNodeName
        pref = Preferences.userRoot().node(newNodeName)
    }

    fun saveNote(key: String, value: String) {
            pref.put(key, value)
        }

    fun getNote(key: String): String {
        return pref.get(key, "Deleted note")
    }

    fun latestNote(current: Int, key: Any) {
        if (current > 0) {
            saveNote("note$current", "Command RUN note")
            val latest = getNote("note$key")
            println(latest)
        }
    }

    fun deleteNote(notenum: Int) {
        pref.remove("note$notenum")
    }

    fun specificNote(desired: Any, current: Int) {
        saveNote("note$current", "Command RUN note")
        val exactNote = getNote("note$desired")
        println(exactNote)
    }

    class invalidNoteE(err: String) : Exception(err)
}
