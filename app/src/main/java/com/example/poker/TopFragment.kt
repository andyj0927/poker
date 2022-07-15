package com.example.poker

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.telecom.Call
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.poker.Data.ResponseType
import retrofit2.Callback
import retrofit2.Response

class TopFragment: Fragment() {
    val TAG: String = TopFragment::class.java.simpleName
    lateinit var loginButton: Button
    lateinit var registerButton: Button
    lateinit var myInfoButton: Button
    lateinit var logoutButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater.inflate(R.layout.fragment_top, container, false)

        initViews(view)
        initListeners()
        setTopButtons()

        return view
    }

    private fun initViews(view: View) {
        loginButton = view.findViewById(R.id.toLoginButton)
        registerButton = view.findViewById(R.id.toRegisterButton)
        myInfoButton = view.findViewById(R.id.toMyInfoButton)
        logoutButton = view.findViewById(R.id.toLogoutButton)
    }

    private fun initListeners(){
        loginButton.setOnClickListener{
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        registerButton.setOnClickListener {
            val intent = Intent(activity, SignupActivity::class.java)
            startActivity(intent)
        }

        myInfoButton.setOnClickListener {
            val intent = Intent(activity, MyInfoActivity::class.java)
            intent.putExtra("id", Global.currentUserId)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val call: Call<ResponseType<Unit>> =
                ServiceCreator.userService.getLogout(Global.headers)

            call.enqueue(object : Callback<ResponseType<Unit>> {
                override fun onResponse(
                    call: Call<ResponseType<Unit>>,
                    response: Response<ResponseType<Unit>>
                ) {
                    if (response.code() == 200) {
                        activity?.let { it1 -> PreferenceManager.removeKey(it1, "JWT") }
                        Global.currentUserId = null
                        Global.headers["token"] = ""

                        // googleSignOut()
                        setTopButtons()

                        Global.socket?.disconnect()
                        Log.d(TAG, Global.currentUserId.toString())
                        Toast.makeText(activity, "Logout Success", Toast.LENGTH_LONG).show()

                        val intent = Intent(activity, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                    else if (response.code() == 401) {
                        Toast.makeText(activity, "Failed to Logout", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(activity, "${response.code()}", Toast.LENGTH_LONG).show()
                    }
                    activity?.finish()
                }

                override fun onFailure(call: Call<ResponseType<Unit>>, t: Throwable) {
                    Log.e(TAG, "error:$t")
                }
            })
        }
    }

    private fun setTopButtons() {
        if(Global.headers["token"] != "") {
            loginButton.visibility = View.GONE
            registerButton.visibility = View.GONE
            myInfoButton.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE
        }

        else {
            loginButton.visibility = View.VISIBLE
            registerButton.visibility = View.VISIBLE
            myInfoButton.visibility = View.GONE
            logoutButton.visibility = View.GONE
        }
    }
}