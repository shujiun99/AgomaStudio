package com.example.agomastudio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agomastudio.Data.Event
import com.example.agomastudio.Data.OKU
import com.example.agomastudio.Data.Provider
import com.example.agomastudio.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        binding.providerClick.setOnClickListener{
            val intent: Intent = Intent(this, RegisterProviderActivity::class.java)
            startActivity(intent)
        }

        binding.okuclick.setOnClickListener {
            val intent: Intent = Intent(this, RegisterOKUActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString().trim()
            val pass = binding.edtLoginPass.text.toString()

            if(email == ""){
                Toast.makeText(this@MainActivity,"Invalid Email", Toast.LENGTH_SHORT).show()
            }

            if(pass == ""){
                Toast.makeText(this@MainActivity,"Invalid Password", Toast.LENGTH_SHORT).show()
            }
            dbref = FirebaseDatabase.getInstance().getReference("Provider")
            dbref.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for(eventSnapshot in snapshot.children) {
                            val provider = eventSnapshot.getValue(Provider::class.java)
                            if(provider?.email.equals(email)){
                                if(provider?.password.equals(pass)){
                                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                                        OnCompleteListener<AuthResult> { task ->
                                            if(task.isSuccessful){
                                                Log.i("My","Login Success!!")
                                                goProvider()
                                            }else{
                                                Toast.makeText(this@MainActivity,"Failed to Login", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                }else{
                                    Toast.makeText(this@MainActivity,"Wrong Password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            dbref = FirebaseDatabase.getInstance().getReference("OKU")
            dbref.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for(eventSnapshot in snapshot.children) {
                            val oku = eventSnapshot.getValue(OKU::class.java)
                            if(oku?.email.equals(email)){
                                if(oku?.password.equals(pass)){
                                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                                        OnCompleteListener<AuthResult> { task ->
                                            if(task.isSuccessful){
                                                Log.i("My","Login Success!!")
                                                goOKU()
                                            }else{
                                                Toast.makeText(this@MainActivity,"Failed to Login", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                }else{
                                    Toast.makeText(this@MainActivity,"Wrong Password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            if(email.equals("admin123@gmail.com")){
                if(pass.equals("Mima1234!")){
                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener<AuthResult> { task ->
                            if(task.isSuccessful){
                                Log.i("My","Login Success!!")
                                goAdmin()
                            }else{
                                Toast.makeText(this@MainActivity,"Failed to Login", Toast.LENGTH_SHORT).show()
                            }
                        })
                }else{
                    Toast.makeText(this@MainActivity,"Wrong Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goAdmin() {
        val intent: Intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    private fun goOKU() {
        val intent: Intent = Intent(this, OKUActivity::class.java)
        startActivity(intent)
    }

    private fun goProvider() {
        val intent: Intent = Intent(this, ProviderActivity::class.java)
        startActivity(intent)
    }

}