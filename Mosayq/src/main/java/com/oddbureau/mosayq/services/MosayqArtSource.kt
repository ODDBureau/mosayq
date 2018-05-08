package com.oddbureau.mosayq.services

import android.content.Intent
import android.net.Uri
import com.google.android.apps.muzei.api.Artwork
import com.google.android.apps.muzei.api.MuzeiArtSource
import com.oddbureau.mosayq.data.managers.BackgroundImagesManager
import com.oddbureau.mosayq.data.managers.PalettesManager
import com.oddbureau.mosayq.data.managers.PatternsManager


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
class MosayqArtSource : MuzeiArtSource("Mosayq") {

    private val ROTATE_TIME_MILLIS = 30 * 60 * 1000 // 30 minutes

    override fun onCreate() {
        super.onCreate()
        setUserCommands(MuzeiArtSource.BUILTIN_COMMAND_ID_NEXT_ARTWORK)
    }


    override fun onUpdate(reason: Int) {

        BackgroundImagesManager().generateBackgroundImage(applicationContext, PatternsManager().getRandomisedPattern(), PalettesManager().getRandomisedColors())
        val bg = BackgroundImagesManager().getLastAddedImage()

        publishArtwork(Artwork.Builder()
                .imageUri(Uri.parse(bg.uri))
                .title(bg.title)
                .byline(bg.byLine)
                .viewIntent(Intent(Intent.ACTION_VIEW, Uri.parse(bg.uri)))
                .build())

        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS)

    }



}