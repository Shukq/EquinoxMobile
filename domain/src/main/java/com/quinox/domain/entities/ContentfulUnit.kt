package com.quinox.domain.entities




class ContentfulUnit(
    var idUnit: String,
    var title: String,
    var description: String,
    var sections: MutableList<ContentfulSection> = mutableListOf()
)