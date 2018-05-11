package com.oddbureau.mosayq.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.ShareActionProvider
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.base.Constants
import com.oddbureau.mosayq.base.RealmActivity

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

class GalleryItemDetailActivity : RealmActivity() {

    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_item_detail)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        imageUri = intent.getStringExtra(Constants.GALLERY_ITEM_IMAGE_URI)

        Glide.with(this)
                .load(imageUri)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL) //use this to cache
                .into(findViewById<ImageView>(R.id.iv_gallery_item_detail_image))


        val btnSetWallpaper = findViewById<Button>(R.id.btn_set_wallpaper)
        btnSetWallpaper.setOnClickListener({
            val intent = Intent(Intent.ACTION_ATTACH_DATA)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.setDataAndType(Uri.parse(imageUri), "image/*")
            intent.putExtra("mimeType", "image/*")
            this.startActivity(Intent.createChooser(intent, resources.getString(R.string.action_set_wallpaper)))
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_share, menu)

        val shareItem = menu?.findItem(R.id.action_share)
        val imageShareActionProvider = MenuItemCompat.getActionProvider(shareItem) as ShareActionProvider

        val imageShareIntent = Intent(Intent.ACTION_SEND)
        imageShareIntent.type = "image/jpeg"
        imageShareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri))

        imageShareActionProvider.setShareIntent(imageShareIntent)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
