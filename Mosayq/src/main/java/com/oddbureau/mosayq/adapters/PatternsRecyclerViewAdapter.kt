package com.oddbureau.mosayq.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import cn.refactor.library.SmoothCheckBox
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.data.transactions.PatternTransaction
import com.oddbureau.mosayq.fragments.PatternsFragment
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

class PatternsRecyclerViewAdapter(
        mContext: Context,
        realmResults: RealmResults<Pattern>,
        automaticUpdate: Boolean,
        animateIdType: Boolean,
        private val mListener: PatternsFragment.OnListFragmentInteractionListener?
        )
    : RealmBasedRecyclerViewAdapter<Pattern, PatternsRecyclerViewAdapter.ViewHolder>(mContext, realmResults, automaticUpdate, animateIdType) {

    override fun onCreateRealmViewHolder(viewGroup: ViewGroup?, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.fragment_patterns_item, viewGroup, false)
        val vh = ViewHolder(v as RelativeLayout)
        return vh
    }


    override fun onBindRealmViewHolder(holder: ViewHolder?, position: Int) {

        val pattern = realmResults[position]
        val backgroundImage = context.resources.getIdentifier(pattern.image, "drawable", context.packageName)
        Glide.with(context)
                .load(backgroundImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder?.ivBackgroundImage)

        holder?.tvTitle?.text =  pattern.type + " - " + pattern.category
        holder?.cbSelected?.isChecked = pattern.isSelected!!

        holder?.cbSelected?.setOnClickListener {
            val checked = !holder?.cbSelected?.isChecked
            PatternTransaction(context).updatePatternSelectedById(pattern.id, checked)
            holder?.cbSelected?.setChecked(checked, true)
        }
    }


    inner class ViewHolder(mView: View) : RealmViewHolder(mView){
        val ivBackgroundImage: ImageView = mView.findViewById(R.id.iv_pattern_item_image)
        val tvTitle: TextView = mView.findViewById(R.id.tv_pattern_item_title)
        val cbSelected: SmoothCheckBox = mView.findViewById(R.id.cb_pattern_item_select)

    }
}
