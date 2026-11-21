 
package com.ruhann.ios18launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class HomeActivity : Activity() {
    private lateinit var grid: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(22, 31, 61)
        window.navigationBarColor = Color.rgb(22, 31, 61)
        buildHome()
    }

    private fun buildHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 18, 18, 12)
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(20, 31, 65),
                    Color.rgb(55, 66, 120),
                    Color.rgb(105, 75, 145)
                )
            )
        }

        val date = TextView(this).apply {
            text = "BEM-VINDO"
            setTextColor(Color.WHITE)
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, 25, 0, 5)
        }
        root.addView(date)

        val clock = TextView(this).apply {
            text = "9:41"
            setTextColor(Color.WHITE)
            textSize = 54f
            typeface = Typeface.create(
                "sans-serif-light", Typeface.NORMAL
            )
            gravity = Gravity.CENTER
        }
        root.addView(clock)

        val search = EditText(this).apply {
            hint = "Pesquisar aplicativos"
            setSingleLine(true)
            textSize = 15f
            setTextColor(Color.WHITE)
            setHintTextColor(0xCCFFFFFF.toInt())
            setPadding(16, 0, 16, 0)
            background = GradientDrawable().apply {
                setColor(0x44FFFFFF)
                cornerRadius = 28f
            }
        }

        root.addView(search, LinearLayout.LayoutParams(
            -1, 48
        ).apply {
            setMargins(0, 22, 0, 14)
        })

        val scroll = ScrollView(this)
        grid = GridLayout(this).apply {
            columnCount = 4
        }
        scroll.addView(grid)
        root.addView(scroll, LinearLayout.LayoutParams(
            -1, 0, 1f
        ))

        setContentView(root)

        search.addTextChangedListener(object :
            android.text.TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?, start: Int,
                count: Int, after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?, start: Int,
                before: Int, count: Int
            ) {
                loadApps(s?.toString() ?: "")
            }

            override fun afterTextChanged(
                s: android.text.Editable?
            ) {}
        })

        loadApps("")
    }

    private fun loadApps(filter: String) {
        grid.removeAllViews()
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN)
            .addCategory(Intent.CATEGORY_LAUNCHER)

        val apps = pm.queryIntentActivities(
            intent, PackageManager.MATCH_ALL
        ).distinctBy { it.activityInfo.packageName }
            .filter {
                pm.getApplicationLabel(
                    it.activityInfo.applicationInfo
                ).toString().contains(filter, true)
            }
            .sortedBy {
                pm.getApplicationLabel(
                    it.activityInfo.applicationInfo
                ).toString()
            }

        apps.forEach { result ->
            val info = result.activityInfo
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(3, 8, 3, 8)
                setOnClickListener {
                    val launch = pm.getLaunchIntentForPackage(
                        info.packageName
                    )
                    if (launch != null) startActivity(launch)
                }
            }

            val icon = ImageView(this).apply {
                setImageDrawable(
                    pm.getApplicationIcon(info.applicationInfo)
                )
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            item.addView(icon, LinearLayout.LayoutParams(56, 56))

            val name = TextView(this).apply {
                text = pm.getApplicationLabel(
                    info.applicationInfo
                )
                textSize = 10.5f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                maxLines = 1
            }

            item.addView(name, LinearLayout.LayoutParams(
                78, 30
            ))

            grid.addView(item, GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(
                    GridLayout.UNDEFINED, 1f
                )
            })
        }
    }
}
