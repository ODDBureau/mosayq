package com.oddbureau.mosayq.data.managers

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import com.commonsware.cwac.provider.StreamProvider
import com.oddbureau.mosayq.data.models.BackgroundImage
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.data.transactions.BackgroundImageTransaction
import com.oddbureau.mosayq.images.CanvasData
import com.oddbureau.mosayq.images.ImageManager
import com.oddbureau.mosayq.utils.Debugger
import com.oddbureau.mosayq.utils.Randomiser
import com.oddbureau.mosayq.utils.io.ImageIO
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
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
class BackgroundImagesManager {

    fun getBackgroundImagesForGallery(): RealmResults<BackgroundImage> {
        var realm = Realm.getDefaultInstance()
        var backgroundImages = realm.where(BackgroundImage::class.java).findAllSorted("createdDate", Sort.DESCENDING)
        return backgroundImages
    }

    fun getLastAddedImage(): BackgroundImage {
        var realm = Realm.getDefaultInstance()
        var backgroundImage = realm.where(BackgroundImage::class.java).findAllSorted("createdDate", Sort.DESCENDING).first()
        return backgroundImage
    }


    fun generateBackgroundImage(context: Context, pattern: Pattern, colors: ArrayList<String>){

        initializeCanvasData()

        var bitmap: Bitmap
        if(pattern == null)
            bitmap = ImageManager().getGeneratedImage()
        else
            bitmap = ImageManager().getGeneratedImage(pattern, colors)

        var imageName = Randomiser().generateUUID()
        var imageIO = ImageIO(context).apply {
            setFileName(imageName + ".png")
            //setExternal(true)
            save(bitmap)
        }

        var file = imageIO.getFile()

        Debugger.logWarning(file.toString() +", " + file.absolutePath)

        BackgroundImageTransaction(context).apply {
            addAndRotateBackground(pattern.category +" "+ Date().time, pattern.type+", Mosayq", Date(), StreamProvider.getUriForFile("com.oddbureau.mosayq.streamprovider", file).toString())
            //addAndRotateBackground(imageName, "Unknown Mosayq, c. 1984", Date(), Uri.fromFile(file).toString(), forGallery)
        }

    }

    fun initializeCanvasData(){
        val metrics = Resources.getSystem().displayMetrics
        CanvasData.height =  metrics.heightPixels
        CanvasData.width = metrics.widthPixels
        CanvasData.multiplier = SettingsManager().getSettings().screenSizeMultiplier!!

        CanvasData.orientation = Resources.getSystem().configuration.orientation
    }

}