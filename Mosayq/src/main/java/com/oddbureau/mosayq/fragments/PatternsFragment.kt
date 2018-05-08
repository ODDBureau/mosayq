package com.oddbureau.mosayq.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.adapters.PatternsRecyclerViewAdapter
import com.oddbureau.mosayq.base.RealmFragment
import com.oddbureau.mosayq.data.managers.PatternsManager
import com.oddbureau.mosayq.data.models.Pattern
import io.realm.RealmResults
import io.realm.Sort

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

class PatternsFragment : RealmFragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private var patterns: RealmResults<Pattern>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_patterns, container, false) as RealmRecyclerView

        patterns = PatternsManager().getPatterns(Sort.DESCENDING)

        // Set the adapter
        if (view is RealmRecyclerView) {
            view.setAdapter(PatternsRecyclerViewAdapter(activity as Context, patterns!!, true, true, mListener))
        }

        return view
    }

    open fun datasetChanged(){
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
        fun onListFragmentInteraction(item: Pattern)
    }
    
}
