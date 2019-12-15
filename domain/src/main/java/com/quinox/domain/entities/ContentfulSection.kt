package com.quinox.domain.entities

import java.io.Serializable

class ContentfulSection(
    var idSection: String,
    var title: String,
    var idUnit: String,
    var classes: MutableList<ContentfulClass> = mutableListOf()
) : Serializable