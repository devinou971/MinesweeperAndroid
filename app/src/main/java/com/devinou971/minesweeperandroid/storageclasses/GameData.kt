package com.devinou971.minesweeperandroid.storageclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class GameData (
    @PrimaryKey(autoGenerate = true)
    var _id: Int?,

    @ColumnInfo(name="time")
    var time: Int,

    @ColumnInfo(name="game_type")
    val gameType: Int
)