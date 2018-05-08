package com.oddbureau.mosayq.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.oddbureau.mosayq.R


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
class PaletteEditorAdapter(context: Context?, resource: Int, objects: MutableList<String>?) : ArrayAdapter<String>(context, resource, objects) {

    var colors = objects
    var updated: Boolean = false

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        val inflater = (context as Activity).layoutInflater
        var barColorContainer = inflater.inflate(R.layout.element_palette_color, parent, false)
        val color = android.graphics.Color.parseColor(colors?.get(position))

        barColorContainer.findViewById<View>(R.id.v_bar_color).setBackgroundColor(color)
        barColorContainer.layoutParams?.height = parent?.height?.div(count) as Int

        //Dirty-hack for heights of items that do not add up to the parent height due to rounding integer values after divisions
        if(position == colors?.size?.minus(1))
            parent?.setBackgroundColor(color)

        return barColorContainer

    }

    override fun getCount(): Int {
        return colors?.size as Int
    }

    /*
    open fun setHexColors(hexes: MutableList<String>?) {
        colors = hexes
        notifyDataSetChanged()
    }
    */

    open fun addColor(color: String) {
        colors?.add(color)
        updated = true
        notifyDataSetChanged()
    }

    open fun getColorAt(index: Int): String?{
       return colors?.get(index)
    }

    open fun updateColorAt(index: Int, color: String)
    {
        colors?.set(index, color)
        updated = true
        notifyDataSetChanged()
    }

    open fun removeColorAt(index: Int) {
        colors?.removeAt(index)
        updated = true
        notifyDataSetChanged()
    }

}