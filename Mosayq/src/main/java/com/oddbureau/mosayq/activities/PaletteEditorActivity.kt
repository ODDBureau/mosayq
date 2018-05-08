package com.oddbureau.mosayq.activities

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.adapters.PaletteEditorAdapter
import com.oddbureau.mosayq.base.Constants
import com.oddbureau.mosayq.base.RealmActivity
import com.oddbureau.mosayq.data.transactions.PaletteTransaction
import com.oddbureau.mosayq.utils.Randomiser
import me.priyesh.chroma.ChromaDialog
import me.priyesh.chroma.ColorMode
import me.priyesh.chroma.ColorSelectListener


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
class PaletteEditorActivity: RealmActivity()  {

    val PALETTE_ACTION_ADD: String = "palette.action.add"
    val PALETTE_ACTION_EDIT: String = "palette.action.edit"


    val PALETTE_REQUEST: Int = 3000

    val PALETTE_RESULT_NOTHING: Int = 5000
    val PALETTE_RESULT_ADDED: Int = 5001
    val PALETTE_RESULT_EDITED: Int = 5002
    val PALETTE_RESULT_REMOVED: Int = 5003

    private val MAX_SIZE: Int = 12

    private var lvColorsContainer: ListView? = null
    private var adapter: PaletteEditorAdapter? = null

    private val colorsHex: MutableList<String> = mutableListOf()

    private var isEdit: Boolean? = null
    private var paletteId: Long? = null
    private var paletteCount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette_editor)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        isEdit = intent?.getStringExtra(Constants.PALETTE_ACTION_TYPE).equals(PALETTE_ACTION_EDIT)
        //Initialize the colorsHex here if you're editing an existent Palette
        if(isEdit == true)
        {
            paletteId = intent.getLongExtra(Constants.PALETTE_ITEM_ID, 0)
            paletteCount = intent.getIntExtra(Constants.PALETTE_ITEM_COUNT, 0)

            for(hex in intent.getStringArrayListExtra(Constants.PALETTE_ITEM_COLORS))
                colorsHex.add(hex)
        }

        lvColorsContainer = findViewById<ListView>(R.id.lv_palette_colors)
        adapter = PaletteEditorAdapter(this, 0, colorsHex)
        lvColorsContainer?.adapter = adapter



        lvColorsContainer?.onItemClickListener = colorClickListener
        registerForContextMenu(lvColorsContainer)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener({ view ->
            addColorToContainer( Randomiser().randomiseColorHex())
        })
    }

    private fun addColorToContainer(color: String){

        if(adapter?.count == MAX_SIZE)
            return

        adapter?.addColor(color)

    }

    private val colorClickListener = OnItemClickListener{ containerView: AdapterView<*>?, view: View, position: Int, id: Long ->

            val paletteAdapter = containerView?.adapter as PaletteEditorAdapter
            val color = android.graphics.Color.parseColor(paletteAdapter.getColorAt(position))

            ChromaDialog.Builder()
                    .initialColor(color)
                    .colorMode(ColorMode.RGB)
                    .onPushedButton(object : ColorSelectListener {
                        override fun onColorSelected(selectedColor: Int) {
                            paletteAdapter.updateColorAt(position, String.format("#%06X", 0xFFFFFF and selectedColor))
                        }
                        override fun onColorDeleted() {
                            paletteAdapter.removeColorAt(position)
                        }
                    })
                    .create()
                    .show(supportFragmentManager, "dialog-color-picker")

    }

    override fun onBackPressed() {
        exitActivity()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        if(isEdit == true) {
            val inflater = menuInflater
            inflater.inflate(R.menu.context_menu, menu)
            return true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                exitActivity()
                return true
            }
            R.id.action_delete -> {

                if(paletteCount?.compareTo(1) == 0 )
                {
                    AlertDialog.Builder(this)
                            .setTitle(resources.getString(R.string.dialog_palette_limit_palette_title))
                            .setMessage(resources.getString(R.string.dialog_palette_limit_palette_desc))
                            .setPositiveButton(R.string.dialog_confirm_ok, null)
                            .show()
                    return true
                }else {
                    AlertDialog.Builder(this)
                            .setTitle(resources.getString(R.string.dialog_palette_remove_palette_title))
                            .setMessage(resources.getString(R.string.dialog_palette_remove_palette_desc))
                            .setPositiveButton(R.string.dialog_confirm_remove, { dialog, whichButton -> exitActivityPaletteRemoved() })
                            .setNegativeButton(R.string.dialog_confirm_cancel, null).show()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun exitActivity(){

        if(adapter?.updated == true)
            if(colorsHex.size < 3) {
                AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.dialog_palette_without_min_colors_title))
                        .setMessage(resources.getString(R.string.dialog_palette_without_min_colors_desc))
                        .setPositiveButton(R.string.dialog_confirm_discard, { dialog, whichButton -> exitActivityWithoutChanges() })
                        .setNegativeButton(R.string.dialog_confirm_cancel, null).show()

                return
            }

        if(isEdit == true)
            if(adapter?.updated == true)
                exitActivityPaletteUpdated()
            else
                exitActivityWithoutChanges()
        else
            exitActivityPaletteAdded()
    }

    private fun exitActivityPaletteUpdated(){
        PaletteTransaction(applicationContext).apply {
            updatePalette(paletteId, colorsHex)
        }
        setResult(PALETTE_RESULT_EDITED)
        finish()
    }

    private fun exitActivityPaletteRemoved(){
        PaletteTransaction(applicationContext).apply {
            removePalette(paletteId)
        }
        setResult(PALETTE_RESULT_REMOVED)
        finish()
    }

    private fun exitActivityPaletteAdded(){
        PaletteTransaction(applicationContext).apply {
            addPalette(colorsHex)
        }
        setResult(PALETTE_RESULT_ADDED)
        finish()
    }

    private fun exitActivityWithoutChanges(){
        setResult(PALETTE_RESULT_NOTHING)
        finish()
    }


}

