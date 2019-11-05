package com.example.restapislibs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://jsonplaceholder.typicode.com/posts/"
    private lateinit var tvResponseFrom: TextView
    private lateinit var tvUserId: TextView
    private lateinit var tvId: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvBody: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResponseFrom = findViewById(R.id.tvResponseFrom)
        tvUserId = findViewById(R.id.tvUserId)
        tvId = findViewById(R.id.tvId)
        tvTitle = findViewById(R.id.tvTitle)
        tvBody = findViewById(R.id.tvBody)

        getVolleyResponse()
//        getRetrofitResponse()
    }

    private fun setTextViews(
        responseFrom: String,
        userId: String,
        id: String,
        title: String,
        body: String
    ) {
        tvResponseFrom.text = "Response via =  $responseFrom"
        tvUserId.text = "UserId =  $userId"
        tvId.text = "Id =  $id"
        tvTitle.text = "Title=  $title"
        tvBody.text = "Body =  $body"
    }

    private fun getVolleyResponse() {
        val queue = Volley.newRequestQueue(this)  //...1

        val stringRequest = StringRequest(
            Request.Method.GET,
            baseUrl+1,
            Response.Listener<String> { response ->
                Log.d("Response", response)
                tvResponseFrom.text = "Response From Volley"
                try {
                    val jsonOutPut = JSONObject(response)
                    setTextViews(
                        "Volley",
                        jsonOutPut.getString("userId"),
                        jsonOutPut.getString("id"),
                        jsonOutPut.getString("title"),
                        jsonOutPut.getString("body")
                    )

                } catch (e: JSONException) {
                    Log.d("Parsing Issue:", e.localizedMessage)
                    tvResponseFrom.text = "Parsing Issue"
                }
            },  //...3
            Response.ErrorListener { tvResponseFrom.text = "Response fail" })  //...4
        queue.add(stringRequest) //...3
    }


    private fun getRetrofitResponse() {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()  //...1

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)  //...2

        val call = jsonPlaceHolderApi.getPosts()  //...3

        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: retrofit2.Response<Post>) {
                if (!response.isSuccessful) {
                    tvResponseFrom.text = "Code " + response.code()
                    return
                }
                val post = response.body()
                if (post != null) {
                    setTextViews(
                        "Retrofit",
                        post.userId.toString(),
                        post.id.toString(),
                        post.title!!,
                        post.body!!
                    )
                }
            }  //...4

            override fun onFailure(call: Call<Post>, t: Throwable) {
                tvResponseFrom.text = "Response fail"
            }  //...5
        })
    }
}
