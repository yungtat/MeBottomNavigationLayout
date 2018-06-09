package xt.yungtat.mebottomnavigationlayout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import xt.yungtat.BottomNavigationLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var layou =findViewById<BottomNavigationLayout>(R.id.m_layout)


    }
}
