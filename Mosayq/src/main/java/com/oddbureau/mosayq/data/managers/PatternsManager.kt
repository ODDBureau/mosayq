package com.oddbureau.mosayq.data.managers

import com.oddbureau.mosayq.data.models.Pattern
import com.oddbureau.mosayq.utils.Randomiser
import io.realm.Realm
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

class PatternsManager {

    fun getPatterns(sorting: Sort): RealmResults<Pattern> {
        var realm = Realm.getDefaultInstance()
        var patterns= realm.where(Pattern::class.java).findAllSorted("id", sorting)
        return patterns
    }

    fun getRandomisedPattern(): Pattern {
        var realm = Realm.getDefaultInstance()
        var patterns= realm.where(Pattern::class.java).equalTo("isSelected", true).findAllSorted("id", Sort.DESCENDING)
        return patterns[Randomiser().randomiseInt(patterns.size)]
    }

}