package com.easyapps.jetutils.database

import com.easyapps.jetutils.utils.*

enum class Folder {
    All, Law, Language, History, Random, Marked
}


fun String.getFolder(): Folder {
    return when (this) {
        ALL -> Folder.All
        LAW -> Folder.Law
        HISTORY -> Folder.History
        LANGUAGE -> Folder.Language
        else -> Folder.Random
    }
}