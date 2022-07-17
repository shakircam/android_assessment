package com.shakircam.android_assessment_test.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shakircam.android_assessment_test.utils.Constants.TABLE_NAME
import kotlinx.parcelize.Parcelize


data class Repository(
    val items: List<Item>,
) {

    @Entity(tableName = TABLE_NAME)
    @Parcelize
    data class Item(

        val description: String?,
        val full_name: String,
        val name: String,
        @Embedded
        val owner: Owner,
        val updated_at: String
    ) : Parcelable{
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
    }

    @Parcelize
    data class Owner(
        val login: String,
        val avatar_url: String
    ): Parcelable

}
