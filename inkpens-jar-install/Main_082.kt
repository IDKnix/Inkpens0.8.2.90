
import Inkpacks.inkcloud_080
import java.io.File
import java.io.FileWriter
import kotlin.random.Random
import kotlin.system.exitProcess


private const val MAX_NOTES = 9999
var STARTER = "!"

fun main() {
    var session = "inkpens"
    val userHome = System.getProperty("user.home")
    val filePath = "$userHome/InkPens/note_INKPENS.txt"
    val file = File(filePath)
    val inkCloud = inkcloud_080("0.8.0", session)
    var note = 0
    val packages = "Install PushSTR? "
    var pushStr: Int
    var username: String
    val lightBlue = "\u001B[94m"

    // Reset ANSI escape code to default color
    val resetColor = "\u001B[0m"

    println("Version 0.8.2.90")
    print("Initiate InkPens? yY/Nn ")
    val initiate = readlnOrNull() ?: ""

    if (initiate.contains('y')) {
        println("Selected yes")
        print(packages)
        val installed = readlnOrNull() ?: ""
        pushStr = if (installed == "y") {
            1
        } else {
            0
        }
        print("Select a username for this session. ")
        username = readln().toString()
        Thread.sleep(1000)
        do {
            print("Note $note start: ")
            val noteValue = readlnOrNull() ?: ""
            if (noteValue == "#import PushSTR") {
                println("Installing PushSTR..")
                Thread.sleep(3500)
                val accept = Random.nextInt(1, 3)
                if (accept == 1) {
                    pushStr = 1
                    println("Successfully installed PushSTR.")
                } else {
                    println("Failed! Try again.")
                }
            }

            if (noteValue == "#unport PushSTR") {
                Thread.sleep(750)
                pushStr = 0
                println("Uninstalled PushSTR.")
            }

            if (noteValue == "${STARTER}export" && note > 0) {
                try {
                    val fileWriter = FileWriter(file)
                    fileWriter.write(inkCloud.getNote("note${note - 1}"))
                    fileWriter.close()
                    println("Successfully transported note to $filePath")
                } catch (e: Exception) {
                    println("Error: ${e.message}")
                }
            }
            if (noteValue == "${STARTER}version") {
                println("0.8.2, with InkCloud for 0.8.0, version 1")
            }
            if (noteValue == "${STARTER}edit LATEST") {
                val latest = "note${note - 1}"
                print("Edit $latest: ")
                val edited = readlnOrNull()
                if (edited != null) {
                    inkCloud.saveNote(latest, edited)
                    println("Successfully edited $latest.")
                }
            } else if (noteValue.startsWith("${STARTER}edit ")) {
                val noteNumberString = noteValue.substringAfterLast(" ")
                val noteNumber = if (noteNumberString.equals("LATEST", ignoreCase = true)) {
                    note - 1 // Set noteNumber to the latest note index
                } else {
                    noteNumberString.toIntOrNull() ?: 0 // Default to 0 if it's not a valid number
                }
                if (noteNumber in 0..<note) {
                    val specific = "note$noteNumber"
                    print("Edit $specific: ")
                    val edited = readlnOrNull()
                    if (edited != null) {
                        inkCloud.saveNote(specific, edited)
                        println("Successfully edited $specific.")
                    }
                }
            }
            if (noteValue.startsWith("#push ")) {
                val toWrite = noteValue.substringAfter("#push ").trim()
                if (pushStr != 1) {
                    println("Please install PushSTR using #import PushSTR to use this function.")
                }
                else {
                    println(toWrite)
                }
            }

            if (noteValue.startsWith("${STARTER}sls ")) {
                session = noteValue.substringAfterLast("!sls ").trim()
                if (session.isNotEmpty()) {
                    println("Switching to session: $session")
                    println("Switch back with !sls inkpens")
                    inkCloud.switchSession(session)
                    val fl = FileWriter("$userHome/InkPens/logs.slss")
                    fl.write("Entered session '$session'.")
                } else {
                    println("Please provide a valid session name.")
                }
            }
            if (noteValue.startsWith("${STARTER}sls inkpens")) {
                println("Switching back to default session InkPens")
                inkCloud.switchSession("inkpens")
                val fl = FileWriter("$userHome/InkPens/logs.slss")
                fl.write("Exited session.")
            }

            if (noteValue.startsWith("${STARTER}delete ")) {
                val noteNumberString = noteValue.substringAfterLast(" ")
                val noteNum = noteNumberString.toIntOrNull() ?: 0
                inkCloud.deleteNote(noteNum)
            }

            if (noteValue == "${STARTER}list") {
                var i = 0
                println("You have ${note+1} notes currently. [INCLUDING COMMANDS]")
                while (i <= note) {
                    val listedNote = inkCloud.getNote("note$i")
                    println("Note$i: '$listedNote'\n")
                    i++
                }
            }

            if (noteValue == "${STARTER}help") {
                val helpCom = """                   
                                                       ▀█▀ ▒█▄░▒█ ▒█░▄▀ ▒█▀▀█ ▒█▀▀▀ ▒█▄░▒█ ▒█▀▀▀█ 
                                                       ▒█░ ▒█▒█▒█ ▒█▀▄░ ▒█▄▄█ ▒█▀▀▀ ▒█▒█▒█ ░▀▀▀▄▄ 
                                                       ▄█▄ ▒█░░▀█ ▒█░▒█ ▒█░░░ ▒█▄▄▄ ▒█░░▀█ ▒█▄▄▄█
                ----------------------------------------------------------------------------------------------
                NOTE: 0.7.9.8 is not listed to add any commands as this update was a subsequent update to 0.8.
                ----------------------------------------------------------------------------------------------
                
                |----------------------------|
                | InkPens 0.8.2.70 Commands: |
                |----------------------------|
    
                Available Commands: (Showing updated 0.7.9.8+ syntax.)
                ____________________________________________________________________________________________________________________________________________________________
                | - !note view LATEST          : View the latest note you typed. [uncharted release]                                                                        |
                | - !note view <notenumber>    : View a specific note. [uncharted release, Revision 2]                                                                      |
                | - !note view START           : View the first note you typed. [0.8 release, no revision]                                                                  |
                | - !export                    : Exports the latest note to C:/Users/metre/OneDrive/Desktop. [0.6 release, no revision]                                     |
                | - !help                      : Current interface, shows all commands and syntax, along with summary [0.7 release, no revision]                            |
                | - !list                      : Lists all notes, including commands. Also tells you how many notes you have. [0.7 release, no revision]                    |
                | - !delete LATEST             : Deletes the last note you typed. [0.7 release, no revision]                                                                |
                | - !delete <notenumber>       : Deletes a specific note. [0.7 release, no revision]                                                                        |
                | - !chars LATEST              : Tells you how many characters your latest note has. [~0.7 release, no revision]                                            |
                | - !chars <notenumber>        : Tells you how many characters a specific note has. [~0.7 release, no revision]                                             |
                | - !edit LATEST               : Lets you edit the latest note. [0.7 release, unknown revision]                                                             |
                | - !edit <notenumber>         : Lets you edit a note of your choosing. [0.7 release, unknown revision]                                                     |
                | - ?quit                      : Quits InkPens with prior checking. [0.8 release, no revision]                                                              |
                | - ?quit!                     : Forcefully quits InkPens. [0.8 release, no revision]                                                                       |
                | - #push <message>            : Prints a message. (please note this command only works if you have installed PushSTR. [0.7.9 patch 5 release, revision 1]  |
                | - #import <package>          : Imports a package. [0.8 release, no revision]                                                                              |
                | - #unport <package>          : Removes the specified package. [0.8 release, no revision]                                                                  |
                | - :A                         : Starts SIGMA/MiNtY terminal. [0.8 releasse, no revision]                                                                   |
                | - !sls <session>             : Creates a local session. (switch back to default with !sls inkpens) [0.8 release, revision 1]                              |
                 ------------------------------------------------------------------------------------------------------------------------------------------------------------
                """.trimIndent()
                println(helpCom)
            }


            if (noteValue.trim() == "${STARTER}delete LATEST") {
                inkCloud.deleteNote(note-1)
                if (inkCloud.getNote("note${note - 1}") == "") {
                    println("Successfully deleted the latest note.")
                } else {
                    println("Couldn't delete the latest note.")
                }
            }

            if (noteValue == ":A") {
                var MSTARTER = ":"
                var aNote = 0
                var perms = false
                println("Entered Advanced Command Mode (ACM/MiNtY). Go back with :A-")
                print("MiNtY [$lightBlue$username@$session$resetColor]:\n")
                while (aNote <= 100) {
                    print("$ ")
                    val adValue = readlnOrNull() ?: ""
                    if (adValue == "${MSTARTER}qt") {
                        inkCloud.saveNote("adnote$aNote", adValue)
                        exitProcess(1)
                    }
                    else if (adValue == "${MSTARTER}csk") {
                        inkCloud.saveNote("adnote$aNote", adValue)
                        print("Enter your new starter keyword: ")
                        STARTER = readln().toString()
                    }
                    else if (adValue == "${MSTARTER}A-") {
                        break
                    }
                    else if (adValue == "${MSTARTER}rq perms") {
                        inkCloud.saveNote("adnote$aNote", adValue)
                        perms = true
                        println("Elevated perms.")
                    }
                    else if (adValue.startsWith("${MSTARTER}psh ")) {
                        inkCloud.saveNote("adnote$aNote", adValue)
                        if (perms) {
                            val toWrite = adValue.substringAfter(":psh! ")
                            println(toWrite)
                        }
                        else {
                            println("You do not have the permissions to use this command.")
                        }
                    }
                    else if (adValue == "${MSTARTER}numnte") {
                        println("Number of notes currently: ${note+1}")
                    }
                    else if (adValue.startsWith("${MSTARTER}user")) {
                        println("Username: $username")
                        val arg = adValue.substringAfter("${MSTARTER}user ")
                        if (arg == "-ch") {
                            print("Change your username: ")
                            username = readln()
                        }
                    }
                    else if (adValue == "${MSTARTER}session") {
                        println("Session: $session")
                    }
                    else if (adValue == "enix") {
                        println("Freeing commands from : requirements.")
                        MSTARTER = ""
                    }
                    else {
                        println("Couldn't find command: '$adValue'")
                    }
                    aNote++
                }
            }

            if (noteValue == "${STARTER}chars LATEST") {
                val latest = "note${note - 1}"
                val latestNote = inkCloud.getNote(latest)
                val chars = latestNote.length
                println("Characters in the latest note: $chars")
            }
            if (noteValue.startsWith("${STARTER}chars ")) {
                val noteNumber = noteValue.substringAfterLast(" ").toInt()
                if (noteNumber in 0..<note) {
                    val selectedNote = inkCloud.getNote("note$noteNumber")
                    val chars = selectedNote.length
                    println("Characters in note$noteNumber: $chars")
                }
            }
            if (noteValue == "?quit") {
                print("Are you sure you want to quit? n/y: ")
                val quit = readlnOrNull() ?: "n"
                if (quit == "n") {
                    note++
                    continue
                }
                else if (quit == "y") {
                    exitProcess(1)
                }
            }
            if (noteValue == "?quit!") {
                exitProcess(1)
            }

            if (noteValue == "${STARTER}note view LATEST" && note > 0) {
                inkCloud.latestNote(note, note - 1)
            }

            else if (noteValue == "${STARTER}note view START") {
                val starting = inkCloud.getNote("note0")
                println(starting)
            }

            else if (noteValue.startsWith("${STARTER}note view ")) {
                val noteNumber = noteValue.substringAfterLast(" ").toInt()
                if (noteNumber in 0..<note) {
                    inkCloud.specificNote(noteNumber, note)
                }
            } else {
                inkCloud.saveNote("note$note", noteValue)
            }
            note++
        } while (note <= MAX_NOTES)
        if (initiate.equals('n') || initiate.equals('N')) {
            println("Selected no")
            exitProcess(0)
        } else {
            println("Invalid answer")
            exitProcess(1)
        }
    }
}
