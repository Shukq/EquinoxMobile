package com.quinox.domain.useCases

import com.quinox.domain.entities.ContentfulUnit
import com.quinox.domain.entities.Result
import io.reactivex.Observable

interface ContentfulUseCase {
    fun getHomePage() : Observable<Result<String>>
}