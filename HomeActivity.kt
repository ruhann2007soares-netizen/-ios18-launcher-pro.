package com.ruhann.ios18launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class HomeActivity : Activity() {
    private val bgTop = Color.rgb(20, 31, 65)
    private val bgBottom = Color.rgb(105, 75, 145)
    private lateinit var appsGrid: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(22, 31, 61)
        window.navigationBarColor = Color.rgb(22, 31, 61)
        window.decorView.systemUiVisibility = 0
        buildHome()
    }

    private fun buildHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(12))
            background = GradientDrawable(GradientDrawable.Orientation.TL_BR,
                intArrayOf(bgTop, Color.rgb(55, 66, 120), bgBottom))
        }

        val top = TextView(this).apply {
            text = "9:41                                      ●  ▮  ▰"
            setTextColor(Color.WHITE); textSize = 14f
            setPadding(dp(4), dp(4), dp(4), dp(8))
        }
        root.addView(top)

        val date = TextView(this).apply {
            text = "SÁBADO, 21 DE NOVEMBRO"
            setTextColor(Color.WHITE); textSize = 12f
            gravity = Gravity.CENTER; setPadding(0, dp(16), 0, dp(4))
        }
        root.addView(date)

        val clock = TextView(this).apply {
            text = "9:41"
            setTextColor(Color.WHITE); textSize = 54f
            typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            gravity = Gravity.CENTER
        }
        root.addView(clock)

        val search = EditText(this).apply {
            hint = "Pesquisar"
            setSingleLine(true); textSize = 15f
            setTextColor(Color.WHITE); setHintTextColor(0xCCFFFFFF.toInt())
            setPadding(dp(16), 0, dp(16), 0)
            background = rounded(0x44FFFFFF, 28)
        }
        val searchLp = LinearLayout.LayoutParams(-1, dp(44)).apply {
            setMargins(0, dp(22), 0, dp(14))
        }
        root.addView(search, searchLp)

        val scroll = ScrollView(this)
        appsGrid = GridLayout(this).apply { columnCount = 4; useDefaultMargins = false }
        scroll.addView(appsGrid)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val dock = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(8), dp(8), dp(8))
            background = rounded(0x55FFFFFF, 30)
        }
        root.addView(dock, LinearLayout.LayoutParams(-1, dp(82)).apply {
            setMargins(dp(4), dp(10), dp(4), dp(4))
        })
        setContentView(root)

        search.addTextChangedListener(SimpleWatcher { filter ->
            populateApps(filter)
        })
        populateApps("")
    }

    private fun populateApps(filter: String) {
        appsGrid.removeAllViews()
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val list = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { it.activityInfo }
            .distinctBy { it.packageName }
            .filter { pm.getApplicationLabel(pm.getApplicationInfo(it.packageName, 0))
                .toString().contains(filter, ignoreCase = true) }
            .sortedBy { pm.getApplicationLabel(pm.getApplicationInfo(it.packageName, 0)).toString() }

        list.forEach { info ->
            val appInfo = pm.getApplicationInfo(info.packageName, 0)
            val label = pm.getApplicationLabel(appInfo).toString()
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(3), dp(8), dp(3), dp(8))
                isClickable = true
                setOnClickListener {
                    val launch = pm.getLaunchIntentForPackage(info.packageName)
                    if (launch != null) startActivity(launch)
                }
            }
            val icon = ImageView(this).apply {
                setImageDrawable(pm.getApplicationIcon(appInfo))
                scaleType = ImageView.ScaleType.FIT_CENTER
                background = rounded(Color.WHITE, 18)
                setPadding(dp(10), dp(10), dp(10), dp(10))
            }
            item.addView(icon, LinearLayout.LayoutParams(dp(56), dp(56)))
            val name = TextView(this).apply {
                text = label; textSize = 10.5f; setTextColor(Color.WHITE)
                gravity = Gravity.CENTER; maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END
                setPadding(0, dp(5), 0, 0)
            }
            item.addView(name, LinearLayout.LayoutParams(dp(78), dp(24)))
            appsGrid.addView(item, GridLayout.LayoutParams().apply {
                width = 0; height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            })
        }
    }

    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply {
        setColor(color); cornerRadius = dp(radius).toFloat()
    }
    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private class SimpleWatcher(val changed: (String) -> Unit) : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { changed(s?.toString() ?: "") }
        override fun afterTextChanged(s: android.text.Editable?) {}
    }
}
