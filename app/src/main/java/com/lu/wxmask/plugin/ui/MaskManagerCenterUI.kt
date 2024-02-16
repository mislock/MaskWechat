package com.lu.wxmask.plugin.ui

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.google.gson.JsonObject
import com.lu.magic.util.ResUtil
import com.lu.magic.util.ToastUtil
import com.lu.magic.util.kxt.toElseString
import com.lu.wxmask.Constrant
import com.lu.wxmask.bean.QuickTemporaryBean
import com.lu.wxmask.plugin.ui.view.AttachUI
import com.lu.wxmask.ui.adapter.SpinnerListAdapter
import com.lu.wxmask.util.ActivityUtils
import com.lu.wxmask.util.BarUtils
import com.lu.wxmask.util.ConfigUtil
import com.lu.wxmask.util.ext.dp
import com.lu.wxmask.util.ext.setTextColorTheme
import com.lu.wxmask.util.ext.toIntElse
import com.lu.wxmask.util.ext.toJson


class MaskManagerCenterUI @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : AttachUI(context, attrs, defStyleAttr, defStyleRes) {

    var mQuickClickCountEdit: EditText? = null
    var mQuickClickDurationEdit: EditText? = null

    init {
        this.onShowListener = {
            BarUtils.setStatusBarLightMode(getActivity()!!, true)
        }
    }

    override fun onCreateView(container: ViewGroup): View {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            isForceDarkAllowed = true
        }
        container.setBackgroundColor(Theme.Color.bgPrimary(context))
        return LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                orientation = LinearLayout.VERTICAL
            }
            addView(getTitleBar())
            addView(getContent())
        }
    }

    private fun getTitleBar(): View {
        return FrameLayout(context).apply {
            layoutParams = MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            }

            addView(TextView(context).apply {
                layoutParams = FrameLayout.LayoutParams(44.dp, 44.dp).apply {
                    gravity = Gravity.START
                    marginStart = 24.dp
                    marginEnd = 24.dp
                }
                setTextColorTheme(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                text = "×"

                setOnClickListener {
                    dismiss()
                }
            })
            addView(TextView(context).apply {
                layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER
                }
                setTextColorTheme(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                text = "老年人配置中心"
            })
        }
    }

    private fun dismissWithCheck(check: Boolean = true) {
        if (!check) {
            super.dismiss()
            return
        }

        if (mQuickClickCountEdit?.text.isNullOrBlank() || mQuickClickDurationEdit?.text.isNullOrBlank()) {
            ToastUtil.show("不能为空")
            return
        }

        val quickTempLocal = QuickTemporaryBean(ConfigUtil.getTemporaryJson() ?: JsonObject())
        val uiQuickTemp = QuickTemporaryBean().apply {
            duration = mQuickClickDurationEdit?.text.toIntElse(duration)
            clickCount = mQuickClickCountEdit?.text.toIntElse(clickCount)
        }

        if (quickTempLocal.toJson() != (uiQuickTemp.toJson())) {
            //数据发生了变更
            AlertDialog.Builder(context)
                .setTitle("提示")
                .setIcon(context.applicationInfo.icon)
                .setMessage("是否保存修改？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定") { _, _ ->
                    ConfigUtil.setTemporary(uiQuickTemp)
                    super.dismiss()
                }.show()
        } else {
            super.dismiss()
        }
    }

    override fun dismiss() {
        dismissWithCheck(true)
    }

    private fun getContent(): View {
        val quickTemp = QuickTemporaryBean(ConfigUtil.getTemporaryJson() ?: JsonObject())

        return LinearLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                orientation = LinearLayout.VERTICAL
            }
            setPadding(24.dp, 12.dp, 24.dp, 12.dp)
            addView(
                ItemLayoutArrowRight("黑名单管理").apply {
                    setOnClickListener {
                        ConfigManagerUI(getActivity()!!).show()
                    }
                })
            addView(Divider())
            addView(ItemTitle("临时解除", onClick = {

            }))
            addView(Spinner(context).apply {
                gravity = Gravity.RIGHT
                val dataList = arrayListOf(
                    Constrant.CONFIG_TEMPORARY_MODE_QUICK_CLICK to "快速点击",
                    Constrant.CONFIG_TEMPORARY_MODE_LONG_PRESS to "长按解除",
                    Constrant.CONFIG_TEMPORARY_MODE_CIPHER to "长按解除"

                )
                adapter = SpinnerListAdapter(dataList)
                visibility = View.GONE
            })

            addView(LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    orientation = LinearLayout.HORIZONTAL
                }
                addView(ItemSubTitle("间隔时长/毫秒："))
                addView(ItemSubEdit(quickTemp.duration.toElseString("150")).apply {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    mQuickClickDurationEdit = this
                })
            })

            addView(LinearLayout(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    orientation = LinearLayout.HORIZONTAL
                    topMargin = 4.dp
                }
                addView(ItemSubTitle("点击次数/次："))
                addView(ItemSubEdit(quickTemp.clickCount.toElseString("5")).apply {
                    mQuickClickCountEdit = this
                    inputType = InputType.TYPE_CLASS_NUMBER
                })
            })

            addView(Divider())
            addView(ItemLayoutArrowRight("清空配置数据").apply {
                setOnClickListener {
                    AlertDialog.Builder(context)
                        .setTitle("警告")
                        .setMessage("该操作会清空所有配置数据")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定") { _, _ ->
                            ConfigUtil.clearData()
                            ToastUtil.show("配置已清空，请杀掉${context.resources.getString(context.applicationInfo.labelRes)}并重启")
                            getActivity()?.finish()
                        }
                        .show()
                }
            })
        }
    }

    private fun Divider() = View(context).apply {
        layoutParams = MarginLayoutParams(MATCH_PARENT, 1.dp).apply {
            topMargin = 8.dp
            bottomMargin = 8.dp
        }
        setBackgroundColor(Theme.Color.divider)
    }

    private fun ItemLayoutArrowRight(text: String) = FrameLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        addView(
            ItemTitle(text)
        )
        addView(TextView(context).apply {
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }
            this.text = ">"
            textSize = 18f
            scaleX = 0.6f
            setTextColorTheme(Color.GRAY)
        })
    }

    private fun ItemSubEdit(text: String) = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT).apply {
            weight = 1f
            gravity = Gravity.CENTER_VERTICAL
        }
        Theme.Style.itemSubTitleStyle(this)
        setText(text)
    }

    private fun ItemSubTitle(text: String, onClick: View.OnClickListener? = null) = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
        gravity = Gravity.CENTER_VERTICAL
        Theme.Style.itemSubTitleStyle(this)
        this.text = text
        this.setOnClickListener(onClick)
    }

    private fun ItemTitle(text: String, onClick: View.OnClickListener? = null) = TextView(context).apply {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            topMargin = 12.dp
            bottomMargin = 12.dp
        }
        gravity = Gravity.CENTER_VERTICAL
        Theme.Style.itemTitleStyle(this)
        this.text = text
        if (onClick != null) {
            this.setOnClickListener(onClick)
        }
    }

}

