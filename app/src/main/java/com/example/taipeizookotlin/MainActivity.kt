package com.example.taipeizookotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.Fragment.HomeFragment
import com.example.taipeizookotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var activityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        this.supportFragmentManager.beginTransaction()
            .add(R.id.mFragment, HomeFragment(), HomeFragment::class.java.simpleName)
            .commit()
    }
}