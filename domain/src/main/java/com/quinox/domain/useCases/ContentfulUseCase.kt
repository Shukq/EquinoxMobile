package com.quinox.domain.useCases

import com.quinox.domain.entities.ContentfulUnit
import com.quinox.domain.entities.Result
import io.reactivex.Observable

interface ContentfulUseCase {
    fun getUnits(): Observable<Result<List<ContentfulUnit>>>
}