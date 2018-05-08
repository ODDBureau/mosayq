package com.oddbureau.mosayq.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.data.models.Color
import com.oddbureau.mosayq.data.models.Palette
import com.oddbureau.mosayq.fragments.PalettesFragment
import io.realm.RealmBasedRecyclerViewAdapter
import io.realm.RealmResults
import io.realm.RealmViewHolder

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

class PalettesRecyclerViewAdapter(
        mContext: Context,
        realmResults: RealmResults<Palette>,
        automaticUpdate: Boolean,
        animateIdType: Boolean,
        private val mListener: PalettesFragment.OnListFragmentInteractionListener?
        )
    : RealmBasedRecyclerViewAdapter<Palette, PalettesRecyclerViewAdapter.ViewHolder>(mContext, realmResults, automaticUpdate, animateIdType) {

    override fun onCreateRealmViewHolder(viewGroup: ViewGroup?, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.fragment_palettes_item, viewGroup, false)
        val vh = ViewHolder(v as RelativeLayout)
        return vh
    }

    override fun onBindRealmViewHolder(holder: ViewHolder?, position: Int) {

        val palette = realmResults[position]

        holder?.llColors?.removeAllViews()
        for(i in 0 until palette.colors?.size!!) {
            addColorToContainer(holder?.llColors, palette.colors?.get(i))
        }

        holder?.itemView?.setOnClickListener {
            if (null != mListener) {
                mListener!!.onListFragmentInteraction(palette as Palette, realmResults.size )
            }
        }

    }

    private fun addColorToContainer(container: ViewGroup?, color: Color?){

        val colorBarContainer = inflater.inflate(R.layout.element_palette_color, container, false)

        val colorBar = colorBarContainer.findViewById<View>(R.id.v_bar_color)
        colorBar.setBackgroundColor(android.graphics.Color.parseColor(color?.hex))

        val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f)
        colorBarContainer.layoutParams = param
        container?.addView(colorBarContainer)

    }



    inner class ViewHolder(mView: View) : RealmViewHolder(mView){
        val llColors: LinearLayout = mView.findViewById(R.id.ll_palette_item_colors)

    }
}
