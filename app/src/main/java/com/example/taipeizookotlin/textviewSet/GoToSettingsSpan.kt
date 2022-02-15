package com.example.taipeizookotlin.textviewSet

import android.content.Intent
import android.provider.Settings

import android.text.style.ClickableSpan
import android.view.View


class GoToSettingsSpan : ClickableSpan() {
    override fun onClick(view: View) {
        view.context.startActivity(
            Intent(Settings.ACTION_SETTINGS))
    }
}