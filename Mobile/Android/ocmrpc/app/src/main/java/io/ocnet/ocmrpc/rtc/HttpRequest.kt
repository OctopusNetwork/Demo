package io.ocnet.ocmrpc.rtc

import android.content.Context
import android.util.Log
import org.json.JSONObject


/**
 * Created by owen on 18-3-3.
 */

class HttpRequest(context: Context, listener: HttpRequestListener) {
    private var mRequestQueue: RequestQueue? = null
    private var mListener: HttpRequestListener = listener

    private val TAG = "HTTPREQ"

    init {
        if (null == mRequestQueue) {
            mRequestQueue = Volley.newRequestQueue(context)
        }
    }

    fun get(url: String) {
        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    mListener?.onStringResponse(response)
                }, Response.ErrorListener { error ->
                    mListener?.onError(error)
                })

        mRequestQueue?.add(stringRequest)
    }

    fun post(url: String, body: JSONObject) {
        val jsonRequest = JsonObjectRequest(Request.Method.POST, url, body,
                Response.Listener { response ->
                    Log.d(TAG, response.toString())
                }, Response.ErrorListener { error -> })

        mRequestQueue?.add(jsonRequest)
    }
}