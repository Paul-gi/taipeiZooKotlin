package com.example.taipeizookotlin.textviewSet

import android.R
import android.animation.FloatEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.text.style.ImageSpan
import android.util.Property
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.taipeizookotlin.textviewSet.Emoji.IconFontSpan
import com.example.taipeizookotlin.textviewSet.Emoji.SpeedSignDrawable
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.graphics.drawable.Drawable
import com.example.taipeizookotlin.databinding.TextviewsettingMainBinding


/**
 * 參考來源：https://www.itread01.com/articles/1476858370.html
 *         https://blog.csdn.net/zhaizu/article/details/52740642
 *         git: https://github.com/chiuki/advanced-textview/tree/master/app/src/main/java/com/sqisland/android/advanced_textview
 */
class TextViewSetting : AppCompatActivity() {

    private lateinit var mDataBinding: TextviewsettingMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(
            this,
            com.example.taipeizookotlin.R.layout.textviewsetting_main
        )
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        mDataBinding.mTitleBar.mToolbar.title = "TextView變形"
        mDataBinding.mTitleBar.mChange.visibility = View.GONE
        // mCompoundDrawable()
        mCustomFont()
        mGradient()
        mCheetah()
        mFromHtml()
        mLinkTwo()
        //todo 分數不太對
        mFractionSpan()
        //todo 其中一個沒有動作
        mSpanArticle()
        funRainbowOneText(this)
        funRainbowTwoText()
        funAnimationRainbowText()
        funReplacementSpan()
        funEmojiSpan()
        mLowArticle()
        mLeadingMarginSpan()
        mQuoteSpan()
        mAlignmentSpan()
        mAlignmentSpanOPPOSITE()
    }

    private enum class Operation {
        START, STOP
    }


    /**
     * 動畫的部分
     * Operation
     * mCompoundDrawable
     * onStart
     * onStop
     * startAnimation：clock
     *                 face: https://github.com/chiuki/animated-vector-drawable/blob/master/app/src/main/res/values/strings.xml
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mCompoundDrawable(operation: Operation) {
        val iAnimatedClock =
            resources.getDrawable(com.example.taipeizookotlin.R.drawable.dogdrawablelist, null)
        val iAnimatedWifi =
            resources.getDrawable(com.example.taipeizookotlin.R.drawable.wifidrawalbelist, null)
        val iClockAnimation =
            resources.getDrawable(com.example.taipeizookotlin.R.drawable.time_animation, null)
        val iSmileFaceAnimation =
            resources.getDrawable(com.example.taipeizookotlin.R.drawable.smileface,null)

        iAnimatedClock.setBounds(0, 0, 200, 200)
        iAnimatedWifi.setBounds(0, 0, 200, 200)
        iClockAnimation.setBounds(0, 0, 200, 200)
        iSmileFaceAnimation.setBounds(0,0,200,200)
        mDataBinding.mCompoundDrawable.setCompoundDrawables(
            iClockAnimation,
            iSmileFaceAnimation,
            iAnimatedWifi,
            iAnimatedClock
        )
        startAnimation(mDataBinding.mCompoundDrawable)


        val drawables: Array<Drawable> = mDataBinding.mCompoundDrawable.compoundDrawables
        for (drawable in drawables) {
            if (drawable is Animatable) {
                val animatable = drawable as Animatable
                when (operation) {
                    Operation.START -> animatable.start()
                    Operation.STOP -> animatable.stop()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mCompoundDrawable(Operation.START)
    }

    override fun onStop() {
        super.onStop()
        mCompoundDrawable(Operation.STOP)
    }

    private fun startAnimation(
        textView: TextView
    ) {
        val drawables = textView.compoundDrawables
        for (drawable in drawables) {
            if (drawable != null &&
                drawable is Animatable
            ) {
                (drawable as Animatable).start()
            }
        }
    }


    /**
     * Typeface.createFromAsset 無法使用 使用另一個先取代
     */
    @SuppressLint("SetTextI18n")
    private fun mCustomFont() {
        mDataBinding.mCustomFont.text = "CustomFont"
//        val typeface: android.graphics.Typeface? =
//            android.graphics.Typeface.createFromAsset(assets,"font/nutso2.otf")
        val typeface: Typeface? =
            ResourcesCompat.getFont(this, com.example.taipeizookotlin.R.font.dancing_script_regular)
        mDataBinding.mCustomFont.typeface = typeface
    }

    /**
    参数说明：
    (x0,y0)：渐变起始点坐标
    (x1,y1):渐变结束点坐标
    color0:渐变开始点颜色,16进制的颜色表示，必须要带有透明度
    color1:渐变结束颜色
    colors:渐变数组
    positions:位置数组，position的取值范围[0,1],作用是指定某个位置的颜色值，如果传null，渐变就线性变化。
    tile:用于指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法。
    ————————————————
    原文链接：https://blog.csdn.net/u010126792/article/details/85237085
     */
    @SuppressLint("SetTextI18n")
    private fun mGradient() {
        mDataBinding.mGradient.text = "Gradient"
        val shader: Shader = LinearGradient(
            0f, 0f, 0f, mDataBinding.mGradient.textSize,
            Color.RED, Color.BLUE, Shader.TileMode.MIRROR
        )
        mDataBinding.mGradient.paint.shader = shader
    }


    /**
     * 圖片填充
     */
    @SuppressLint("SetTextI18n")
    private fun mCheetah() {
        mDataBinding.mCheetah.text = "Cheetah"
        val bitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.alert_dark_frame
        )

        val shader: Shader = BitmapShader(
            bitmap,
            Shader.TileMode.REPEAT,
            Shader.TileMode.REPEAT
        )
        mDataBinding.mCheetah.paint.shader = shader
    }


    /**
     * Html跳轉
     */
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun mFromHtml() {
        val htmlString = "<h1>Hello World</h1>\n" +
                "Here is an\n" +
                "<img src=\"animated_clock\"><i>octopus</i>.<br>\n" +
                "And here is a\n" +
                "<a href=\"http://d.android.com\">\n" +
                "link</a>."
        mDataBinding.mFromHtml.movementMethod = LinkMovementMethod.getInstance()
        mDataBinding.mFromHtml.text = Html.fromHtml(
            htmlString, Html.FROM_HTML_MODE_LEGACY, ResourceImageGetter(this), null
        )
    }

    private fun mLinkTwo() {
        val iString: Spannable = SpannableString("This is linkTwo")
        iString.setSpan(GoToSettingsSpan(), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDataBinding.mlinkTwo.text = iString
        mDataBinding.mlinkTwo.movementMethod = LinkMovementMethod.getInstance()
    }


    /**
     * 呈現分數
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun mFractionSpan() {
        val iTypeface =
            ResourcesCompat.getFont(this, com.example.taipeizookotlin.R.font.icomoon)
        mDataBinding.mFractionSpan.typeface = iTypeface
        mDataBinding.mFractionSpanTwo.typeface = ResourcesCompat.getFont(this,com.example.taipeizookotlin.R.font.nutso2)
        val iString =
            "3<sup>1</sup>/<sub>2</sub>+</sub>8<sup>4</sup>/<sub>5</sub>=</sub>我懶得算XD</sub>\n" +
                    "777</sup>/<sub>8888</sub> </sub>6666</sup>/<sub>1111111</sub>"

        val iHtmlStringTwo = getString(com.example.taipeizookotlin.R.string.fraction_text)
        mDataBinding.mFractionSpan.text =
            Html.fromHtml(iString, Html.FROM_HTML_MODE_LEGACY, null, FractionTagHandler())

        mDataBinding.mFractionSpanTwo.text =
            Html.fromHtml(iHtmlStringTwo, Html.FROM_HTML_MODE_LEGACY, null, FractionTagHandler())
    }


    /**
     * UnderlineSpan()下劃線 底線
     * StrikethroughSpan()刪除線
     * SubscriptSpan()字符下沉
     * SuperscriptSpan()字符上浮
     * BackgroundColorSpan()文字背景色
     * ForegroundColorSpan()文本顏色
     * ImageSpan()插入圖片 // createScaledBitmap()增加圖片大小
     * StyleSpan()簡單樣式
     * Typeface()自定義字體
     * TextAppearanceSpan()字體樣式
     * AbsoluteSizeSpan()絕對尺寸
     * RelativeSizeSpan()相對尺寸
     * ScaleXSpan()字體橫向縮放
     * MaskFilterSpan(BlurMaskFilter)字體矇版/模糊
     * MaskFilterSpan(EmbossMaskFilter)字體矇版/前景加粗樣式
     */
    @SuppressLint("ResourceAsColor")
    private fun mSpanArticle() {
        val iTypeface: Typeface? =
            ResourcesCompat.getFont(this, com.example.taipeizookotlin.R.font.dancing_script_regular)
        val iString: Spannable =
            SpannableString(
                "Knowledge is one thing, virtue is another " +
                        "good sense is not conscience, refinement is not humility nor is largeness and justness of view faith. " +
                        "Philosophy,however enlightened, however profound, gives no command over the passions, no influential motives, no vivifying principles."
            )


        iString.setSpan(UnderlineSpan(), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(StrikethroughSpan(), 10, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(SubscriptSpan(), 24, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(SuperscriptSpan(), 60, 70, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(BackgroundColorSpan(Color.RED), 72, 82, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(ForegroundColorSpan(Color.BLUE), 89, 99, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        // 取得 Resource 圖片的 Bitmap
        val iBitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_btn_speak_now)
        // Bitmap 縮放
        val iB2 = Bitmap.createScaledBitmap(iBitmap, iBitmap.width * 3, iBitmap.height * 2, true)
        iString.setSpan(
            ImageSpan(this, iB2),
            115,
            120,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        iString.setSpan(
            StyleSpan(Typeface.BOLD or Typeface.ITALIC),
            132,
            137,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        iString.setSpan(iTypeface, 145, 153, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(
            TextAppearanceSpan(this, R.style.TextAppearance),
            155,
            162,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        iString.setSpan(AbsoluteSizeSpan(48, true), 176, 183, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(RelativeSizeSpan(3.0f), 184, 192, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(ScaleXSpan(3.0f), 200, 211, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        iString.setSpan(
            MaskFilterSpan(BlurMaskFilter(2.0f * 2, BlurMaskFilter.Blur.NORMAL)),
            211,
            215,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        //todo 藍色前景色+加粗樣式 沒動作 over the的the
//        iString.setSpan(
//            MaskFilterSpan(EmbossMaskFilter(floatArrayOf(2f, 2f, 2f), 0.4f, 6F, 3.5f)),
//            217,
//            220,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        mDataBinding.mSpanArticle.text = iString
    }


    @SuppressLint("ResourceType", "SetTextI18n")
    private fun funRainbowOneText(mContext: Context) {
        mDataBinding.mRainBowText.text = "RainBow!!"
        val textShader: Shader = LinearGradient(
            1f,
            1f,
            20f,
            20f,
            mContext.resources.getIntArray(com.example.taipeizookotlin.R.array.rainbow),
            null,
            Shader.TileMode.MIRROR
        )
        mDataBinding.mRainBowText.paint.shader = textShader
    }


    /**
     * 彩虹樣式
     * https://github.com/chiuki/advanced-textview/blob/master/app/src/main/java/com/sqisland/android/advanced_textview/RainbowSpanActivity.java
     */
    private fun funRainbowTwoText() {

//        mDataBinding.mRainBow.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable) {
//                highlight(s.toString())
//            }
//        })
        highlight(mDataBinding.mRainBow.text.toString())
    }

    private fun highlight(mQuery: String) {
        val iText = "It is a RainBow "
        val iSpannableText: Spannable = SpannableString(iText)
        val pattern = Pattern.compile(mQuery.lowercase(Locale.getDefault()))
        val matcher = pattern.matcher(iText.lowercase(Locale.getDefault()))
        while (matcher.find()) {
            iSpannableText.setSpan(
                StyleSpan(Typeface.BOLD),
                matcher.start(),
                matcher.end(),
                0
            )
            iSpannableText.setSpan(
                RainbowSpan(this),
                matcher.start(),
                matcher.end(),
                0
            )
        }
        mDataBinding.mRainBow.text = iSpannableText
    }


    /**
     * 會動的彩虹
     */
    @SuppressLint("ResourceType")
    private fun funAnimationRainbowText() {
        val iString = mDataBinding.mAnimationRainbow.text.toString()
        val pAnimationRainbowSpan = AnimatedColorSpan(this)

        val spannableString = SpannableString(iString)
        val substring =
            getString(com.example.taipeizookotlin.R.string.AnimationRainbow).lowercase(Locale.getDefault())
        val start: Int = iString.lowercase(Locale.getDefault()).indexOf(substring)
        val end = start + substring.length
        spannableString.setSpan(pAnimationRainbowSpan, start, end, 0)

        val objectAnimator = ObjectAnimator.ofFloat(
            pAnimationRainbowSpan, ANIMATED_COLOR_SPAN_FLOAT_PROPERTY, 0f, 100f
        )

        objectAnimator.setEvaluator(FloatEvaluator())
        objectAnimator.addUpdateListener { mDataBinding.mAnimationRainbow.text = spannableString }
        objectAnimator.interpolator = LinearInterpolator()
        objectAnimator.duration = DateUtils.MINUTE_IN_MILLIS * 3
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.start()
    }

    private var ANIMATED_COLOR_SPAN_FLOAT_PROPERTY: Property<AnimatedColorSpan, Float> =
        object : Property<AnimatedColorSpan, Float>(
            Float::class.java, "ANIMATED_COLOR_SPAN_FLOAT_PROPERTY"
        ) {
//            operator fun set(span: AnimatedColorSpan, value: Float) {
//                span.translateXPercentage = value
//            }
//
//            override fun get(span: AnimatedColorSpan): Float {
//                return span.translateXPercentage
//            }


            override fun get(span: AnimatedColorSpan): Float {
                return span.translateXPercentage
            }

            override fun set(span: AnimatedColorSpan, value: Float) {
//                super.set(span, value)
                span.translateXPercentage = value
            }
        }


    /**
     * 自定義矇版 屏障
     */
    private fun funReplacementSpan() {
        val iSpannableText: Spannable = SpannableString(
            "一如站在一面鏡子前，不管是正對或背對，呈現的只是個人的鏡象。\n" +
                    "\n" +
                    "你面對或逃避，終究只是自已。"
        )
        iSpannableText.setSpan(RePlacementSpan(), 5, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDataBinding.mReplacementSpan.text = iSpannableText
    }


    /**
     * 插入符號
     */
    @SuppressLint("ResourceType", "SetTextI18n")
    private fun funEmojiSpan() {
        mDataBinding.mEmoji.text =
            "By training your thoughts to concentrate ⛷ ⛷ on the bright :octopus: " +
                    "side of things, :octopus: you are more likely to have the incentive to :octopus: follow through on your goals. " +
                    "You are less likely :octopus: to be held back by negative :octopus: ideas that might limit your performance."
        val text: String = mDataBinding.mEmoji.text.toString()
        val spannableString = SpannableString(text)

        // Icon font
        var pattern = Pattern.compile("\u26F7") // skier

        var matcher: Matcher = pattern.matcher(text)
        while (matcher.find()) {
            val foregroundColorSpan = ForegroundColorSpan(
                resources.getColor(com.example.taipeizookotlin.R.color.blue, null)
            )
            val iconFontSpan = IconFontSpan(mDataBinding.mEmoji.context)
            spannableString.setSpan(iconFontSpan, matcher.start(), matcher.end(), 0)
            spannableString.setSpan(foregroundColorSpan, matcher.start(), matcher.end(), 0)
        }

        // ImageSpan from resource
        pattern = Pattern.compile(":octopus:")
        matcher = pattern.matcher(text)

        var octopus: Bitmap? = null
        val size = -mDataBinding.mEmoji.paint.ascent()
        while (matcher.find()) {
            if (octopus == null) {
                val bitmap = BitmapFactory.decodeResource(
                    resources,
                    com.example.taipeizookotlin.R.drawable.animated_clock
                )
                octopus = Bitmap.createScaledBitmap(bitmap, size.toInt(), size.toInt(), true)
                bitmap.recycle()
            }
            val span = ImageSpan(this, octopus!!, ImageSpan.ALIGN_BASELINE)
            spannableString.setSpan(span, matcher.start(), matcher.end(), 0)
        }

        // ImageSpan with dynamic drawable
        pattern = Pattern.compile(":speed_(\\d\\d\\d?):")
        matcher = pattern.matcher(text)

        while (matcher.find()) {
            val drawable = SpeedSignDrawable(mDataBinding.mEmoji, matcher.group(1))
            val span = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
            spannableString.setSpan(span, matcher.start(), matcher.end(), 0)
        }

        mDataBinding.mEmoji.text = spannableString
    }


    /**
     * 簡單項目符號
     */
    private fun mLowArticle() {
        val iSpannableText: Spannable = SpannableString(
            "This question is designed to seed a comprehensive Q&A about these common compilation errors in Java."
        )
        iSpannableText.setSpan(BulletSpan(5, Color.BLACK), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDataBinding.mLowArticle.text = iSpannableText
    }


    /**
     * 項目符號
     */
    private fun mLeadingMarginSpan() {
        val bullets = arrayOf("1.", "2.", "3.", "4.")
        val itemContents = arrayOf(
            "那一天，閉目在經殿香霧中，驀然聽見，你誦經中的真言；",
            "那一月，我搖動所有的經筒，不為超度，只為觸摸你的指尖；",
            "那一年，磕長頭匍匐在山路，不為覲見，只為貼著你的溫暖；",
            "那一世，轉山轉水轉佛塔呀，不為修來生，只為途中與你相見。"
        )
        var allText: CharSequence = ""
        for (i in bullets.indices) {
            val aBullet = bullets[i]
            val t = itemContents[i].trim()

            // 註意此處的換行, 如果沒有換行符, 則系統當做只有一個項目處理
            val spannableString = SpannableString(t + "\n")
            spannableString.setSpan(object : LeadingMarginSpan {
                override fun getLeadingMargin(first: Boolean): Int {
                    // 項目符號和正文的縮進距離, 單位 px
                    // 我們可以根據 first 來改變第1行和其余行的縮進距離
                    return 50
                }

                override fun drawLeadingMargin(
                    c: Canvas,
                    p: Paint,
                    x: Int,
                    dir: Int,
                    top: Int,
                    baseline: Int,
                    bottom: Int,
                    text: CharSequence?,
                    start: Int,
                    end: Int,
                    first: Boolean,
                    layout: Layout?
                ) {

                    // 只對第1行文本添加項添加符號
                    //if (first) {
                    val orgStyle = p.style
                    p.style = Paint.Style.FILL
                    c.drawText(aBullet, 0F, bottom - p.descent(), p)
                    p.style = orgStyle
                    // }
                }
            }, 0, t.length, 0)
            allText = TextUtils.concat(allText, spannableString)
        }
        mDataBinding.mLeadingMarginSpan.textSize = 13F
        mDataBinding.mLeadingMarginSpan.text = allText
    }


    /**
     *引用 線
     */
    private fun mQuoteSpan() {
        val iString: Spannable =
            SpannableString("Knowledge is one thing, virtue is another good sense is not conscience, refinement is not humility nor is largeness and justness of view faith. ")
        iString.setSpan(QuoteSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mDataBinding.mQuoteSpan.text = iString
    }


    /**
     * 對齊方式
     * 正常，Layout.Alignment.ALIGN_NORMAL;
     * 居中對齊，Layout.Alignment.ALIGN_CENTER;
     * 反向對齊，Layout.Alignment.ALIGN_OPPOSITE;
     */
    @SuppressLint("SetTextI18n")
    private fun mAlignmentSpan() {
        val iStringNORMAL: Spannable =
            SpannableString("Life comes in a package. This package includes happiness and sorrow")
        val iStringCENTER: Spannable =
            SpannableString("failure and success\n, hope and despair. Life is a learning process.")
        val iStringOPPOSITE: Spannable =
            SpannableString("Experiences in life\n teach us  new lessons\n and make us a better person.\n With each passing day we \nlearn to handle various\n situations.")
        iStringNORMAL.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
            0,
            50,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        iStringCENTER.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            50,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        iStringOPPOSITE.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            0,
            50,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        mDataBinding.mAlignmentSpanA.text = iStringNORMAL
        mDataBinding.mAlignmentSpanB.text = iStringCENTER
        mDataBinding.mAlignmentSpanC.text = iStringOPPOSITE

    }

    /**
     *更完整的反向對齊，Layout.Alignment.ALIGN_OPPOSITE;
     */
    private fun mAlignmentSpanOPPOSITE() {
        appendText(
            "Knock knock",
            Layout.Alignment.ALIGN_NORMAL,
            mDataBinding.mAlignmentSpanOPPOSITE
        )
        appendText(
            "Who's there?",
            Layout.Alignment.ALIGN_OPPOSITE,
            mDataBinding.mAlignmentSpanOPPOSITE
        )
        appendText(
            "Who are you?",
            Layout.Alignment.ALIGN_NORMAL,
            mDataBinding.mAlignmentSpanOPPOSITE
        )
        appendText(
            "I am your mon.",
            Layout.Alignment.ALIGN_OPPOSITE,
            mDataBinding.mAlignmentSpanOPPOSITE
        )
    }

    private fun appendText(text: CharSequence?, align: Layout.Alignment, textView: TextView) {
        if (text == null || text.toString().trim { it <= ' ' }.isEmpty()) {
            return
        }
        val span: AlignmentSpan = AlignmentSpan.Standard(align)
        val spannableString = SpannableString(text)
        spannableString.setSpan(span, 0, text.length, 0)
        if (textView.length() > 0) {

            // 該行很重要，如果沒有換行，那麽反對齊效果失效
            textView.append("\n\n")
        }
        textView.append(spannableString)
    }

}