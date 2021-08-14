package com.kenyrim.fbapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.container
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var displayName: TextView
    private lateinit var status: TextView
    private lateinit var logout: Button
    private lateinit var btnSend: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var etMessage: EditText
    private var user:User? = null
    private var message:Message? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        logout = findViewById(R.id.singoutButton)
        etMessage = findViewById(R.id.et_message)
        btnSend = findViewById(R.id.btn_send)

        val database = Firebase.database
        val myRef =

        logout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        isLogin()

        btnSend.setOnClickListener {
            val message  = etMessage.text.toString()
            if (message.isNotBlank()) {
                val time = System.currentTimeMillis()
                val ref = database.getReference("messages/${time}")
                val data =
                    Message(
                        user?.displayName.toString(),
                        Date(time).toString().replace(" ", ""),
                        message)

                ref.setValue(data)
                etMessage.setText("")
                getMessages()
            }else{
                Snack.snackBar("Input your message",container)
            }
        }
    }

    private fun isLogin() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        auth.currentUser?.uid?.let { loadData(it) } ?: startActivity(intent)
    }

    private fun loadData(userId: String) {

        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User::class.java)
                    title = user?.displayName
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        database.reference.child("user").child(userId).addListenerForSingleValueEvent(dataListener)

     //   getMessages()

    }

    private fun getMessages(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val products: ArrayList<Message?> = ArrayList<Message?>()
                    for (productSnapshot in dataSnapshot.children) {
                        val product: Message? = productSnapshot.getValue(Message::class.java)
                        products.add(product)
                        products.forEach{//TOODO
                            Log.e("aaaaaa",it?.displayName+" -> "+ it?.message.toString())
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        }

        database.reference.child("messages").addValueEventListener(postListener)
    }

}
