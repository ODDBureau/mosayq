package com.oddbureau.mosayq.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.adapters.PalettesRecyclerViewAdapter
import com.oddbureau.mosayq.base.RealmFragment
import com.oddbureau.mosayq.data.managers.PalettesManager
import com.oddbureau.mosayq.data.models.Palette

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

class PalettesFragment : RealmFragment() {

    private var mListener: OnListFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_palettes, container, false) as RealmRecyclerView

        var adapter = PalettesRecyclerViewAdapter(activity as Context, PalettesManager().getPalettes(), true, false, mListener)

        // Set the adapter
        if (view is RealmRecyclerView) {
            view.setAdapter(adapter)
        }

        return view
    }


    open fun datasetChanged(scroll: Boolean){
        if(scroll)
            (view as RealmRecyclerView).scrollToPosition(0)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context as OnListFragmentInteractionListener?
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Palette, count: Int)
    }
    
}
