package com.quinox.domain.entities

import java.io.Serializable


class ContentfulClass(
    var id:String,
    var title:String,
    var descripcion:String?,
    var imagenes:List<String>,
    var urlVideo:String?

) : Serializable