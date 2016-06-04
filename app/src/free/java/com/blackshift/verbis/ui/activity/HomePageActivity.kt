package com.blackshift.verbis.ui.activity

import android.content.Intent

import io.github.prashantsolanki3.shoot.Shoot
import io.github.prashantsolanki3.shoot.listener.OnShootListener

/**
 * Package com.blackshift.verbis.ui.activity
 *
 *
 * Created by Prashant on 5/28/2016.
 *
 *
 * Email: solankisrp2@gmail.com
 * Github: @prashantsolanki3
 */
class HomePageActivity : AbstractHomePageActivity() {

    override fun onStart() {
        super.onStart()
        promptPremium()
    }

    private fun promptPremium() {
        Shoot.repeatAfter(5, "prompt-home-screen", object : OnShootListener() {
            override fun onExecute(i: Int, s: String, i1: Int) {
                startActivity(Intent(this@HomePageActivity, FullVersionPromptActivity::class.java))
            }
        })
    }

}
