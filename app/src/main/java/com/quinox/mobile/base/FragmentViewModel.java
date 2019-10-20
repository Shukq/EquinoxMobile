package com.quinox.mobile.base;

import com.quinox.mobile.libs.Environment;

import io.reactivex.annotations.NonNull;

public class FragmentViewModel<ViewType> {
    protected final Environment context;

    public FragmentViewModel(final @NonNull Environment environment){this.context = environment; }
}
