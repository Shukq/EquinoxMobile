package com.quinox.domain.entities

import java.io.Serializable

class ContentfulNews (
    var idNews: String,
    var title: String,
    var header: String,
    var description: String?
) : Serializable