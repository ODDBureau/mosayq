package com.oddbureau.mosayq.data.transactions

import android.content.Context
import com.oddbureau.mosayq.data.models.BackgroundImage
import com.oddbureau.mosayq.data.models.Settings
import com.oddbureau.mosayq.utils.io.ImageIO
import io.realm.Realm
import io.realm.RealmResults
import java.util.*


/*
 * Copyright 2017 Joel Oliveira
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 *
 */
class BackgroundImageTransaction(private val context: Context) : DataTransaction() {

    fun addAndRotateBackground(title: String?, byline: String?, createdDate: Date?, uri: String?){

        var transaction = Realm.Transaction {

            realm ->

                var maxBackground: Int = (realm.where(Settings::class.java).findFirst()).maxBackground

                var backgroundImages: RealmResults<BackgroundImage> = realm.where(BackgroundImage::class.java).findAllSorted("createdDate")

                if(backgroundImages.size >= maxBackground)
                    for(i in 0..(backgroundImages.size - maxBackground)) {
                        ImageIO(context).deleteFile(backgroundImages[i].uri)
                        backgroundImages[i].deleteFromRealm()
                    }

                var bg: BackgroundImage = realm.createObject(BackgroundImage::class.java, getNextKey(realm, "BackgroundImage"))
                bg.title = title
                bg.byLine = byline
                bg.createdDate = createdDate
                bg.uri = uri
        }

        super.execute(transaction)
    }

    fun addBackground(title: String?, byline: String?, createdDate: Date?, uri: String?){

        var transaction = Realm.Transaction {

            realm ->

            var bg: BackgroundImage = realm.createObject(BackgroundImage::class.java, getNextKey(realm, "BackgroundImage"))
            bg.title = title
            bg.byLine = byline
            bg.createdDate = createdDate
            bg.uri = uri
        }

        super.execute(transaction)
    }
}