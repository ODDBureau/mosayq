package com.oddbureau.mosayq.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker
import com.oddbureau.mosayq.R
import com.oddbureau.mosayq.data.managers.SettingsManager
import com.oddbureau.mosayq.data.transactions.SettingsTransaction

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

class SettingsActivity : AppCompatActivity() {

    var tvBackgroundHistoryDesc: TextView? = null
    var tvTimeIntervalDesc: TextView? = null
    var tvScreenMultiplierDesc: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setTitle(R.string.action_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        tvBackgroundHistoryDesc = findViewById<TextView>(R.id.tv_background_history_desc)
        tvTimeIntervalDesc = findViewById<TextView>(R.id.tv_time_interval_desc)
        tvScreenMultiplierDesc = findViewById<TextView>(R.id.tv_screen_multiplier_desc)

        var settings = SettingsManager().getSettings()

        tvBackgroundHistoryDesc?.text = getString(R.string.settings_background_history_desc, settings.maxBackground)
        tvTimeIntervalDesc?.text = getString(R.string.settings_time_interval_desc, settings.timeInterval)
        tvScreenMultiplierDesc?.text = getString(R.string.settings_screen_multiplier_desc, settings.screenSizeMultiplier)

        findViewById<LinearLayout>(R.id.ll_settings_container).visibility = View.VISIBLE

        tvBackgroundHistoryDesc?.setOnClickListener({ view ->
            openNumberPicker(true, 5, 25, settings.maxBackground, R.string.settings_background_history_dialog_title, "background_history")
        })

        tvTimeIntervalDesc?.setOnClickListener({ view ->
            openNumberPicker(true, 450, 3600, settings.timeInterval, R.string.settings_time_interval_dialog_title, "time_interval")
        })

        tvScreenMultiplierDesc?.setOnClickListener({ view ->
            openNumberPicker(true, 1, 3, settings.screenSizeMultiplier, R.string.settings_screen_multiplier_dialog_title, "screen_multiplier")
        })
    }

    fun openNumberPicker(editable: Boolean, minValue: Int, maxValue: Int, value: Int, title: Int, type: String){

        val numberPicker = MaterialNumberPicker(this, editable = editable, minValue = minValue, maxValue = maxValue)
        numberPicker.textColor = ContextCompat.getColor(this, R.color.white)
        numberPicker.value = value


        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(numberPicker)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(android.R.string.ok), {dialog, whichButton ->
                    when(type)
                    {
                        "background_history" -> {
                            SettingsTransaction(this).updateBackgroundHistory(numberPicker.value)
                            tvBackgroundHistoryDesc?.text = getString(R.string.settings_background_history_desc, numberPicker.value)
                        }
                        "time_interval" -> {
                            SettingsTransaction(this).updateTimeInterval(numberPicker.value)
                            tvTimeIntervalDesc?.text = getString(R.string.settings_time_interval_desc, numberPicker.value)
                        }
                        "screen_multiplier" -> {
                            SettingsTransaction(this).updateScreenMultiplier(numberPicker.value)
                            tvScreenMultiplierDesc?.text = getString(R.string.settings_screen_multiplier_desc, numberPicker.value)
                        }
                    }
                })
                .show()
    }

}
