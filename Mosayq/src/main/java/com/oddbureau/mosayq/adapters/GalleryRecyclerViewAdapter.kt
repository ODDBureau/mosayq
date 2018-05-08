package com.oddbureau.mosayq.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.data.models.BackgroundImage
import com.oddbureau.mosayq.fragments.GalleryFragment
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

class GalleryRecyclerViewAdapter(
        mContext: Context,
        realmResults: RealmResults<BackgroundImage>,
        automaticUpdate: Boolean,
        animateIdType: Boolean,
        private val mListener: GalleryFragment.OnListFragmentInteractionListener?
        )
    : RealmBasedRecyclerViewAdapter<BackgroundImage, GalleryRecyclerViewAdapter.ViewHolder>(mContext, realmResults, automaticUpdate, animateIdType) {

    override fun onCreateRealmViewHolder(viewGroup: ViewGroup?, viewType: Int): ViewHolder {
        val v = inflater.inflate(R.layout.fragment_gallery_item, viewGroup, false)
        val vh = ViewHolder(v as RelativeLayout)
        return vh
    }

    override fun onBindRealmViewHolder(holder: ViewHolder?, position: Int) {
        val backgroundImage = realmResults[position]
        holder?.tvBackgroundTitle?.text = backgroundImage.title
        Glide.with(context)
            .load(backgroundImage.uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder?.ivBackgroundImage)

        holder?.btnBackground?.setOnClickListener {
            if (null != mListener) {
                mListener!!.onListFragmentInteraction(backgroundImage as BackgroundImage)
            }
        }
    }

    inner class ViewHolder( mView: View) : RealmViewHolder(mView) {

        val ivBackgroundImage: ImageView = mView.findViewById<ImageView>(R.id.iv_gallery_item_image)
        val tvBackgroundTitle: TextView = mView.findViewById<TextView>(R.id.tv_gallery_item_title)
        val btnBackground: Button = mView.findViewById<Button>(R.id.btn_gallery_item_action)

        override fun toString(): String {
            return super.toString() + " '" + tvBackgroundTitle.text + "'"
        }
    }
}
