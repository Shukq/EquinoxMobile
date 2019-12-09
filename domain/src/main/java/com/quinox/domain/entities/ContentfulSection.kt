package com.quinox.domain.entities

class ContentfulSection(
    var idSection: String,
    var title: String,
    var idUnit: String,
    var classes: MutableList<ContentfulClass> = mutableListOf()
)