package com.blackshift.verbis.ui.activity

import android.os.Bundle
import android.widget.Toast
import com.blackshift.verbis.R;
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.free.activity_full_version_prompt.*

class FullVersionPromptActivity : VerbisActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_version_prompt)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        Glide.with(this).load(R.drawable.go_premium_prompt)
                .into(premium_prompt_image)

        premium_prompt_image.setOnClickListener {
            Toast.makeText(this,"This will open Play Store",Toast.LENGTH_SHORT).show()
        }
    }

}
