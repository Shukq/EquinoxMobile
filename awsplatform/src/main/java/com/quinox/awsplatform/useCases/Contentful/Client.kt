package com.quinox.awsplatform.useCases.Contentful

import com.contentful.java.cda.CDAClient
import com.google.gson.GsonBuilder

object Client {
    lateinit var client : CDAClient


    fun getContentful():CDAClient{
        return this.client
    }
    fun initContentful(){
        this.client = CDAClient
            .builder()
            .setSpace("2hnq8godjkak")
            .setToken("ts8s9TA9Xsz1lEzKwJ9U46Yl2QaVRrFG81rAmhbr0Z8")
            .build()
    }
}