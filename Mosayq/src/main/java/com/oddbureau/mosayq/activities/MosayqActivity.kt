package com.oddbureau.mosayq.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.chootdev.csnackbar.Duration
import com.chootdev.csnackbar.Snackbar
import com.chootdev.csnackbar.Snackbar.*
import com.chootdev.csnackbar.Type
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.base.Constants
import com.oddbureau.mosayq.base.RealmActivity
import com.oddbureau.mosayq.data.managers.BackgroundImagesManager
import com.oddbureau.mosayq.data.managers.PalettesManager
import com.oddbureau.mosayq.data.managers.PatternsManager
import com.oddbureau.mosayq.data.models.BackgroundImage
import com.oddbureau.mosayq.data.models.Palette
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.fragments.GalleryFragment
import com.oddbureau.mosayq.fragments.PalettesFragment
import com.oddbureau.mosayq.fragments.PatternsFragment

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

class MosayqActivity :
        RealmActivity(),
        GalleryFragment.OnListFragmentInteractionListener,
        PalettesFragment.OnListFragmentInteractionListener,
        PatternsFragment.OnListFragmentInteractionListener {

    private val FRAG_INDEX_GALLERY: Int = 0
    private val FRAG_INDEX_PALETTES: Int = 1
    private val FRAG_INDEX_PATTERNS: Int = 2

    private var selectedFragmentIndex: Int = FRAG_INDEX_GALLERY
    private var selectedFragment: Fragment? = null

    private var loadingContainer: ViewGroup? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var fabButton: FloatingActionButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mosayq)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        loadingContainer = findViewById(R.id.rl_loading_container)
        fabButton = findViewById<View>(R.id.fab_add) as FloatingActionButton?

        fabButton?.setOnClickListener { onMenuAddSelected(selectedFragmentIndex) }

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            when {
                item.itemId == R.id.nav_gallery -> selectedFragmentIndex = FRAG_INDEX_GALLERY
                item.itemId == R.id.nav_palettes -> selectedFragmentIndex = FRAG_INDEX_PALETTES
                item.itemId == R.id.nav_patterns -> selectedFragmentIndex = FRAG_INDEX_PATTERNS
            }

            selectItem()

            true
        }


        selectItem()

        if (selectedFragment is GalleryFragment) {
            val backgroundImages = BackgroundImagesManager().getBackgroundImagesForGallery()

            if (backgroundImages.size == 0) {
                addBackground()
            }
        }


    }

    private fun addBackground() {
        updateLoadingVisibility(true, false)

        Thread(Runnable {
            BackgroundImagesManager().generateBackgroundImage(this, PatternsManager().getRandomisedPattern(), PalettesManager().getRandomisedColors())

            //if(selectedFragment is GalleryFragment)
            //    (selectedFragment as GalleryFragment).datasetChanged()

            updateLoadingVisibility(false, true)


        }).start()
    }


    private fun updateLoadingVisibility(show: Boolean, selectItem: Boolean) {
        runOnUiThread {

            fabButton?.isEnabled = !show

            /*
            if (show) {
                loadingContainer?.visibility = View.VISIBLE
                fabButton?.isEnabled = false

            } else {
                loadingContainer?.visibility = View.GONE
                fabButton?.isEnabled = true

            }
            */

            if (selectItem)
                selectItem()
        }
    }


    private fun selectItem() {

        invalidateOptionsMenu()

        selectedFragment = when (selectedFragmentIndex) {
            0 -> GalleryFragment()
            1 -> PalettesFragment()
            2 -> PatternsFragment()
            else -> null
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_mosayq, selectedFragment)
                .commit()

        if (selectedFragmentIndex == FRAG_INDEX_GALLERY || selectedFragmentIndex == FRAG_INDEX_PALETTES)
            fabButton?.visibility = View.VISIBLE
        else
            fabButton?.visibility = View.GONE


    }

    private fun openSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        when (id) {
            R.id.action_settings -> {
                openSettingsActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.mosayq, menu)
        return true

    }

    private fun onMenuAddSelected(index: Int) {

        if (index == FRAG_INDEX_PALETTES) {
            val intent = Intent(this, PaletteEditorActivity::class.java)
            intent.putExtra(Constants.PALETTE_ACTION_TYPE, PaletteEditorActivity().PALETTE_ACTION_ADD)
            startActivityForResult(intent, PaletteEditorActivity().PALETTE_REQUEST)
        }

        if (index == FRAG_INDEX_GALLERY) {
            addBackground()
        }
    }

    override fun onListFragmentInteraction(item: BackgroundImage) {
        val intent = Intent(this, GalleryItemDetailActivity::class.java)
        intent.putExtra(Constants.GALLERY_ITEM_IMAGE_URI, item.uri)
        startActivity(intent)
    }

    override fun onListFragmentInteraction(item: Palette, count: Int) {
        val intent = Intent(this, PaletteEditorActivity::class.java)
        intent.putExtra(Constants.PALETTE_ACTION_TYPE, PaletteEditorActivity().PALETTE_ACTION_EDIT)
        intent.putExtra(Constants.PALETTE_ITEM_ID, item.id)
        intent.putExtra(Constants.PALETTE_ITEM_COLORS, item.getColorHexes())
        intent.putExtra(Constants.PALETTE_ITEM_COUNT, count)
        startActivityForResult(intent, PaletteEditorActivity().PALETTE_REQUEST)
    }

    override fun onListFragmentInteraction(item: Pattern) {
        /*
        val intent = Intent(this, GalleryItemDetailActivity::class.java)
        intent.putExtra(Constants().GALLERY_ITEM_IMAGE_URI, item.uri)
        startActivity(intent)
        */
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == PaletteEditorActivity().PALETTE_REQUEST) {

            if (resultCode == PaletteEditorActivity().PALETTE_RESULT_ADDED || resultCode == PaletteEditorActivity().PALETTE_RESULT_REMOVED || resultCode == PaletteEditorActivity().PALETTE_RESULT_EDITED) {

                var messageId = 0

                when (resultCode) {
                    PaletteEditorActivity().PALETTE_RESULT_REMOVED -> {
                        if (selectedFragment is PalettesFragment)
                            (selectedFragment as PalettesFragment).datasetChanged(true)

                        messageId = R.string.snack_palette_removed
                    }
                    PaletteEditorActivity().PALETTE_RESULT_ADDED -> {
                        if (selectedFragment is PalettesFragment)
                            (selectedFragment as PalettesFragment).datasetChanged(true)

                        messageId = R.string.snack_palette_created
                    }
                    PaletteEditorActivity().PALETTE_RESULT_EDITED -> {

                        if (selectedFragment is PalettesFragment)
                            (selectedFragment as PalettesFragment).datasetChanged(false)

                        messageId = R.string.snack_palette_updated
                    }
                    else -> {
                    }
                }

                Snackbar.with(this, null).apply {
                    type(Type.SUCCESS)
                    message(resources.getString(messageId))
                    duration(Duration.SHORT)
                    show()
                }

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
