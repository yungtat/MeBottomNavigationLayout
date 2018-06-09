package xt.yungtat

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import xt.yungtat.bottomnavigationlayout.R

/**
 * Created by F.X.Tong on 2017/8/28.
 * 底部导航栏
 */

class BottomNavigationLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr) {


    //点击到哪一项
    var position = 0
        private set

    //菜单栏高度
    private var mTabHeight: Int = 0
    //标题与图标的距离
    private var mTabInterval: Int = 0
    //底部背景
    private var mTabBG: Int = 0

    //菜单栏标题大小
    private var mTabTitleSize: Int = 0
    //菜单栏标题颜色
    var tabTitleColor: Int = 0
    //点击后标题颜色
    var tabTitleSelectColor: Int = 0
    //角标文字颜色
    private var mTabBadgeTxtColor: Int = 0
    //角标文字大小
    private var mTabBadgeTxtSize: Int = 0
    //角标颜色
    private var mTabBadgeBackgroundColor: Int = 0
    //大图标的高度
    private var mTabBigHeight: Int = 0
    //大图标距离圆环高度
    private var mTabBigMargin: Int = 0
    //图标，文字距离
    private var mIconTitleMargin: Int = 0


    private var mTabSelectedListener: OnTabSelectedListener? = null


    init {
        parseAttrs(context, attrs)
        clipChildren = false
    }


    private fun parseAttrs(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.BottomNavigationLayout, 0, 0)
            mTabBG = typedArray.getColor(R.styleable.BottomNavigationLayout_barBackground, Color.WHITE)
            mTabInterval = typedArray.getDimension(R.styleable.BottomNavigationLayout_barInterval, DensityUtils.dp2px(context, 2f).toFloat()).toInt()
            mTabHeight = typedArray.getDimension(R.styleable.BottomNavigationLayout_barHeight, DensityUtils.dp2px(context, 50f).toFloat()).toInt()
            mTabBigHeight = typedArray.getDimension(R.styleable.BottomNavigationLayout_barBigHeight, DensityUtils.dp2px(context, 60f).toFloat()).toInt()
            mTabTitleSize = DensityUtils.px2sp(context, typedArray.getDimension(R.styleable.BottomNavigationLayout_barTitleSize, DensityUtils.sp2px(context, 11f).toFloat()))
            tabTitleColor = typedArray.getColor(R.styleable.BottomNavigationLayout_barTitleColor, Color.argb(205, 105, 105, 105))
            tabTitleSelectColor = typedArray.getColor(R.styleable.BottomNavigationLayout_barTitleSelectColor, Color.argb(255, 24, 157, 255))
            mTabBadgeTxtColor = typedArray.getColor(R.styleable.BottomNavigationLayout_barBadgeTxtColor, Color.WHITE)
            mTabBadgeTxtSize = DensityUtils.px2sp(context, typedArray.getDimension(R.styleable.BottomNavigationLayout_barBadgeTxtSize, DensityUtils.sp2px(context, 8f).toFloat()))
            mTabBadgeBackgroundColor = typedArray.getColor(R.styleable.BottomNavigationLayout_barBadgeBackgroundColor, Color.TRANSPARENT)
            mTabBigMargin = typedArray.getDimension(R.styleable.BottomNavigationLayout_barBigMargin, DensityUtils.dp2px(context, 6f).toFloat()).toInt()
            mIconTitleMargin = typedArray.getDimension(R.styleable.BottomNavigationLayout_barIconTitleMargin, DensityUtils.dp2px(context, 1f).toFloat()).toInt()
            typedArray.recycle()
        }
    }


    fun setTabSelectedListener(tabSelectedListener: OnTabSelectedListener): BottomNavigationLayout {
        this.mTabSelectedListener = tabSelectedListener
        initListener()
        return this
    }


    /**
     * 默认点击
     *
     * @param position 选项
     */
    fun defaultClickPosition(position: Int): BottomNavigationLayout {
        changeSelect(position)
        return this
    }


    /**
     * 带角标的按钮
     * @param selIcon 选中时的图标
     * @param norIcon 取消时的图标
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     */
    fun addBtnBadge(mContext: Context, title: String, tag: Int, selIcon: Int, norIcon: Int): BottomNavigationLayout {
        addBtnBadge(mContext, title, tag, selIcon, norIcon)
        return this
    }

    /**
     * 带角标的按钮,按钮背景来源于网络
     *
     * @param selIcon 选中时的图标Url
     * @param norIcon 取消时的图标Url
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     */
    fun addBtnBadge(mContext: Context, title: String, tag: Int, selIcon: String, norIcon: String): BottomNavigationLayout {

        addBtnBadge(mContext, title, tag, arrayOf(selIcon, norIcon))
        return this
    }


    /**
     * 大图标
     * 点击时图标变大
     * @param selIcon 选中时的图标
     * @param norIcon 取消时的图标
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     */
    fun addBiggerBtn(mContext: Context, title: String, tag: Int, selIcon: Int, norIcon: Int): BottomNavigationLayout {
        addBiggerBtn(mContext, title, tag, null, selIcon, norIcon)
        return this
    }

    /**
     * 大图标,图标来源于网络
     * 点击时图标变大
     * @param selIcon 选中时的图标Url
     * @param norIcon 取消时的图标Url
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     */
    fun addBiggerBtn(mContext: Context, title: String, tag: Int, selIcon: String, norIcon: String): BottomNavigationLayout {
        addBiggerBtn(mContext, title, tag, arrayOf(selIcon, norIcon))
        return this

    }

    /**
     * 特别的按钮
     * @param selIcon 选中时的图标
     * @param norIcon 取消时的图标
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     * @param titleBoolean 是否显示标题
     */
    fun addEspeciallyBtn(mContext: Context, title: String, tag: Int, selIcon: Int, norIcon: Int, titleBoolean: Boolean): BottomNavigationLayout {
        addEspeciallyBtn(mContext, title, tag, titleBoolean, null, selIcon, norIcon)
        return this
    }

    /**
     * 特别的按钮
     * @param selIcon 选中时的图标URl
     * @param norIcon 取消时的图标URl
     * @param title   标题
     * @param tag     标志 0表示点击有效果，1表示无效果
     * @param titleBoolean 是否显示标题
     */
    fun addEspeciallyBtn(mContext: Context, title: String, tag: Int, selIcon: String, norIcon: String, titleBoolean: Boolean): BottomNavigationLayout {
        addEspeciallyBtn(mContext, title, tag, titleBoolean, arrayOf(selIcon, norIcon))
        return this
    }


    /***
     * @param title 按钮标题
     * @param tag 标志 0表示点击有效果，1表示无效果
     * @param urlIcon 网络图标
     * @param mIcon 本地图标
     */
    private fun addBtnBadge(mContext: Context, title: String, tag: Int, urlIcon: Array<String>, vararg mIcon: Int) {

        val constraintLayout = ConstraintLayout(context)
        constraintLayout.setBackgroundColor(mTabBG)

        //=======设置标题=========
        val tabTv = TextView(context)
        tabTv.id = R.id.tab_tv
        tabTv.text = title
        tabTv.textSize = mTabTitleSize.toFloat()
        tabTv.setTextColor(tabTitleColor)
        tabTv.gravity = Gravity.CENTER

        //=======设置图标======
        val tabSelIv = ImageView(context)
        tabSelIv.id = R.id.tab_iv
        tabSelIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabSelIv.visibility = View.INVISIBLE
        val tabNorIv = ImageView(context)
        tabNorIv.id = R.id.tab_iv_nor
        tabNorIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabNorIv.visibility = View.VISIBLE
        if (urlIcon.size > 2) {
            Glide.with(mContext).load(urlIcon[0]).apply(RECYCLER_OPTIONS).into(tabSelIv)
            Glide.with(mContext).load(urlIcon[1]).apply(RECYCLER_OPTIONS).into(tabNorIv)
        } else if (mIcon.size > 2) {
            tabSelIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[0]))
            tabNorIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[1]))
        }

        constraintLayout.addView(tabTv)
        constraintLayout.addView(tabSelIv)
        constraintLayout.addView(tabNorIv)
        val mIvTvSet = ConstraintSet()
        mIvTvSet.clone(constraintLayout)
        //点击后的图标
        mIvTvSet.constrainWidth(tabSelIv.id, ConstraintSet.WRAP_CONTENT)
        mIvTvSet.constrainWidth(tabNorIv.id, ConstraintSet.WRAP_CONTENT)
        mIvTvSet.constrainWidth(tabTv.id, ConstraintSet.WRAP_CONTENT)
        mIvTvSet.constrainHeight(tabSelIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        mIvTvSet.constrainHeight(tabNorIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        mIvTvSet.constrainHeight(tabTv.id, ConstraintSet.WRAP_CONTENT)

        //标题
        mIvTvSet.connect(tabTv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        mIvTvSet.connect(tabTv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        mIvTvSet.connect(tabTv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, mTabInterval)

        mIvTvSet.connect(tabSelIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, mTabInterval)
        mIvTvSet.connect(tabSelIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        mIvTvSet.connect(tabSelIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        mIvTvSet.connect(tabSelIv.id, ConstraintSet.BOTTOM, tabTv.id, ConstraintSet.TOP, mIconTitleMargin)


        //失去焦点的图标
        mIvTvSet.connect(tabNorIv.id, ConstraintSet.LEFT, tabSelIv.id, ConstraintSet.LEFT)
        mIvTvSet.connect(tabNorIv.id, ConstraintSet.RIGHT, tabSelIv.id, ConstraintSet.RIGHT)
        mIvTvSet.connect(tabNorIv.id, ConstraintSet.TOP, tabSelIv.id, ConstraintSet.TOP)
        mIvTvSet.connect(tabNorIv.id, ConstraintSet.BOTTOM, tabSelIv.id, ConstraintSet.BOTTOM)

        mIvTvSet.applyTo(constraintLayout)

        //=======设置图标、标题END===========

        //=======角标======
        val tabBadgeTv = MsgView(context, mTabBadgeBackgroundColor, 0, 0, Color.WHITE)
        tabBadgeTv.id = R.id.badge_tv
        tabBadgeTv.setTextColor(mTabBadgeTxtColor)
        tabBadgeTv.textSize = mTabBadgeTxtSize.toFloat()
        constraintLayout.addView(tabBadgeTv)
        val set = ConstraintSet()
        set.clone(constraintLayout)
        set.constrainWidth(tabBadgeTv.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)
        set.constrainHeight(tabBadgeTv.id, ConstraintSet.MATCH_CONSTRAINT_WRAP)
        set.connect(tabBadgeTv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(tabBadgeTv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabBadgeTv.id, ConstraintSet.LEFT, tabNorIv.id, ConstraintSet.LEFT)

        set.applyTo(constraintLayout)
        //=======角标END===
        //把linearLayout添加到底部
        val llLP = LinearLayoutCompat.LayoutParams(0, MATCH_PARENT, 1f)
        llLP.gravity = Gravity.BOTTOM
        constraintLayout.tag = tag
        addView(constraintLayout, llLP)

    }

    /**
     * 大图标
     * 点击时图标变大
     */
    private fun addBiggerBtn(mContext: Context, title: String, tag: Int, urlIcon: Array<String>?, vararg mIcon: Int) {


        val fl = ConstraintLayout(context)
        val set = ConstraintSet()

        //=======设置标题=========
        val tabTv = TextView(context)
        tabTv.id = R.id.tab_bigger_tv
        tabTv.text = title
        tabTv.textSize = mTabTitleSize.toFloat()
        tabTv.setTextColor(tabTitleColor)
        tabTv.gravity = Gravity.CENTER
        tabTv.visibility = View.GONE


        //=======设置图标======
        //保持底色一样
        val bg = View(context)
        bg.id = R.id.tab_bg
        bg.setBackgroundColor(mTabBG)
        //背景
        val tabBgIv = ImageView(context)
        tabBgIv.id = R.id.tab_big_bg
        tabBgIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabBgIv.setImageResource(R.drawable.tab_big_bg)

        val tabSelIv = ImageView(context)
        tabSelIv.id = R.id.tab_iv
        tabSelIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabSelIv.visibility = View.INVISIBLE
        val tabNorIv = ImageView(context)
        tabNorIv.id = R.id.tab_iv_nor
        tabNorIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabNorIv.visibility = View.INVISIBLE


        if (urlIcon != null && urlIcon.size > 2) {
            Glide.with(mContext).load(urlIcon[0]).apply(RECYCLER_OPTIONS).into(tabSelIv)
            Glide.with(mContext).load(urlIcon[1]).apply(RECYCLER_OPTIONS).into(tabNorIv)
        } else if (mIcon.size > 2) {
            tabSelIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[0]))
            tabNorIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[1]))
        }


        fl.addView(tabBgIv)
        fl.addView(bg)
        fl.addView(tabTv)
        fl.addView(tabNorIv)
        fl.addView(tabSelIv)


        set.clone(fl)
        set.constrainWidth(tabSelIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabNorIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabTv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabBgIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainHeight(tabBgIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabSelIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabNorIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabTv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(bg.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(bg.id, mTabHeight)
        //背景
        set.connect(tabBgIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(tabBgIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabBgIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabBgIv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        //底色
        set.connect(bg.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(bg.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(bg.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        //点击后的图标
        val bigTabH = mTabBigHeight - mTabHeight + mTabBigMargin
        set.connect(tabNorIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, bigTabH)
        set.connect(tabNorIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabNorIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabNorIv.id, ConstraintSet.BOTTOM, tabTv.id, ConstraintSet.TOP, mIconTitleMargin)
        //标题
        set.connect(tabTv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabTv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabTv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, mTabInterval)
        //失去焦点的图标
        set.connect(tabSelIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabSelIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabSelIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, DensityUtils.dp2px(context, 8f))
        set.connect(tabSelIv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.applyTo(fl)

        //=======设置图标、标题END===========

        //把ConstraintLayout添加到底部
        val clLP = LinearLayoutCompat.LayoutParams(0, mTabBigHeight, 1f)
        clLP.gravity = Gravity.BOTTOM
        fl.tag = tag
        addView(fl, clLP)
    }

    /**
     * 特别的按钮
     * @param urlIcon      网络图标
     * @param mIcon         本地图标
     * @param title        标题
     * @param titleBoolean 是否显示标题
     * @param tag          标志
     */
    private fun addEspeciallyBtn(mContext: Context, title: String, tag: Int, titleBoolean: Boolean, urlIcon: Array<String>?, vararg mIcon: Int) {

        val fl = ConstraintLayout(context)
        val set = ConstraintSet()

        //=======设置标题=========
        val tabTv = TextView(context)
        tabTv.id = R.id.tab_tv
        tabTv.text = title
        tabTv.textSize = mTabTitleSize.toFloat()
        tabTv.setTextColor(tabTitleColor)
        tabTv.gravity = Gravity.CENTER

        if (!titleBoolean) {
            tabTv.visibility = View.GONE
        }
        //=======设置图标======
        //保持底色一样
        val bg = View(context)
        bg.id = R.id.tab_bg
        bg.setBackgroundColor(mTabBG)
        //背景
        val tabBgIv = ImageView(context)
        tabBgIv.id = R.id.tab_big_bg
        tabBgIv.scaleType = ImageView.ScaleType.FIT_XY
        tabBgIv.setImageResource(R.drawable.tab_big_bg)

        val tabSelIv = ImageView(context)
        tabSelIv.id = R.id.tab_iv
        tabSelIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabSelIv.visibility = View.INVISIBLE
        val tabNorIv = ImageView(context)
        tabNorIv.id = R.id.tab_iv_nor
        tabNorIv.scaleType = ImageView.ScaleType.FIT_CENTER
        tabNorIv.visibility = View.INVISIBLE

        if (urlIcon != null && urlIcon.size > 2) {
            Glide.with(mContext).load(urlIcon[0]).apply(RECYCLER_OPTIONS).into(tabSelIv)
            Glide.with(mContext).load(urlIcon[1]).apply(RECYCLER_OPTIONS).into(tabNorIv)
        } else if (mIcon.size > 2) {
            tabSelIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[0]))
            tabNorIv.setImageDrawable(ContextCompat.getDrawable(mContext, mIcon[1]))
        }

        fl.addView(tabBgIv)
        fl.addView(bg)
        fl.addView(tabTv)
        fl.addView(tabSelIv)
        fl.addView(tabNorIv)

        set.clone(fl)
        set.constrainWidth(tabSelIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabNorIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabTv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(tabBgIv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainHeight(tabBgIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabSelIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabNorIv.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(tabTv.id, ConstraintSet.WRAP_CONTENT)
        set.constrainWidth(bg.id, ConstraintSet.MATCH_CONSTRAINT_SPREAD)
        set.constrainHeight(bg.id, mTabHeight)
        //背景
        set.connect(tabBgIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(tabBgIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabBgIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabBgIv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        //底色
        set.connect(bg.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(bg.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(bg.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        //点击的图标
        set.connect(tabSelIv.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, mTabBigMargin)
        set.connect(tabSelIv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabSelIv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabSelIv.id, ConstraintSet.BOTTOM, tabTv.id, ConstraintSet.TOP, mIconTitleMargin)
        //标题
        set.connect(tabTv.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT)
        set.connect(tabTv.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT)
        set.connect(tabTv.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, mTabInterval)
        //失去焦点的图标
        set.connect(tabNorIv.id, ConstraintSet.LEFT, tabSelIv.id, ConstraintSet.LEFT)
        set.connect(tabNorIv.id, ConstraintSet.RIGHT, tabSelIv.id, ConstraintSet.RIGHT)
        set.connect(tabNorIv.id, ConstraintSet.TOP, tabSelIv.id, ConstraintSet.TOP)
        set.connect(tabNorIv.id, ConstraintSet.BOTTOM, tabSelIv.id, ConstraintSet.BOTTOM)
        set.applyTo(fl)

        //=======设置图标、标题END===========

        //把ConstraintLayout添加到底部
        val clLP = LinearLayoutCompat.LayoutParams(0, mTabBigHeight, 1f)
        clLP.gravity = Gravity.BOTTOM
        fl.tag = tag
        addView(fl, clLP)
    }


    /**
     * 点击事件
     */
    private fun initListener() {
        val size = childCount
        for (i in 0 until size) {
            getChildAt(i).setOnClickListener { view ->
                if (view.tag as Int == 0) {
                    changeSelect(i)
                }

                mTabSelectedListener!!.onTabSelected(i, view.findViewById(R.id.tab_iv))
            }
        }

    }

    /**
     * 更新图标状态，更新标题状态
     */
    private fun changeSelect(position: Int) {
        this.position = position
        val size = childCount
        for (i in 0 until size) {
            if (this.position == i) {
                //处理当前点击
                if (null != getChildAt(i).findViewById(R.id.tab_iv_nor)) {
                    val iv = getChildAt(i).findViewById<ImageView>(R.id.tab_iv_nor)
                    iv.visibility = View.INVISIBLE
                    if (getChildAt(i).findViewById<View>(R.id.tab_bigger_tv) != null) {
                        getChildAt(i).findViewById<View>(R.id.tab_bigger_tv).visibility = View.GONE
                    }
                }
                getChildAt(i).findViewById<View>(R.id.tab_iv).visibility = View.VISIBLE

                if (getChildAt(i).findViewById<View>(R.id.tab_tv) != null) {
                    (getChildAt(i).findViewById<View>(R.id.tab_tv) as TextView).setTextColor(tabTitleSelectColor)
                }


            } else {
                if (getChildAt(i).findViewById<View>(R.id.tab_tv) != null) {
                    (getChildAt(i).findViewById<View>(R.id.tab_tv) as TextView).setTextColor(tabTitleColor)
                } else if (getChildAt(i).findViewById<View>(R.id.tab_bigger_tv) != null) {
                    getChildAt(i).findViewById<View>(R.id.tab_bigger_tv).visibility = View.VISIBLE
                }
                getChildAt(i).findViewById<View>(R.id.tab_iv).visibility = View.INVISIBLE
                getChildAt(i).findViewById<View>(R.id.tab_iv_nor).visibility = View.VISIBLE

            }
        }


    }


    /**
     * 给角标赋值
     */
    fun setBadgeText(p: Int, `val`: Int): BottomNavigationLayout {
        post {
            showBadge(p)
            if (getChildAt(p).findViewById<View>(R.id.badge_tv) != null) {
                val mv = getChildAt(p).findViewById<MsgView>(R.id.badge_tv)
                val badgeLP = mv.layoutParams as ConstraintLayout.LayoutParams
                val dm = mv.resources.displayMetrics

                if (`val` == 0) {//圆点,设置默认宽高
                    mv.visibility = View.VISIBLE
                    mv.text = ""
                    badgeLP.width = (8 * dm.density).toInt()
                    badgeLP.height = (8 * dm.density).toInt()
                    badgeLP.topMargin = (4 * dm.density).toInt()
                    badgeLP.leftToLeft = R.id.tab_iv_nor
                    mv.layoutParams = badgeLP
                } else if (`val` <= -1) {
                    mv.visibility = View.GONE
                } else {
                    mv.visibility = View.VISIBLE
                    badgeLP.height = (18 * dm.density).toInt()
                    if (`val` < 10) {//圆
                        badgeLP.width = (18 * dm.density).toInt()
                        mv.text = `val`.toString()
                    } else if (`val` < 100) {//圆角矩形,圆角是高度的一半,设置默认padding
                        badgeLP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        mv.setPadding((6 * dm.density).toInt(), 0, (6 * dm.density).toInt(), 0)
                        mv.text = `val`.toString()
                    } else {//数字超过两位,显示99+
                        badgeLP.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                        mv.setPadding((3 * dm.density).toInt(), 0, (3 * dm.density).toInt(), 0)
                        val val2 = "99+"
                        mv.text = val2
                    }
                    mv.layoutParams = badgeLP
                }
                mv.gravity = Gravity.CENTER

            }
        }



        return this
    }

    /**
     * 角标显示
     */
    fun showBadge(position: Int): BottomNavigationLayout {
        if (getChildAt(position).findViewById<View>(R.id.badge_tv) != null) {
            getChildAt(position).findViewById<View>(R.id.badge_tv).visibility = View.VISIBLE
        }

        return this
    }

    /**
     * 角标隐藏
     */
    fun hideBadge(position: Int): BottomNavigationLayout {
        if (getChildAt(position).findViewById<View>(R.id.badge_tv) != null) {
            getChildAt(position).findViewById<View>(R.id.badge_tv).visibility = View.GONE
        }

        return this
    }

    /**
     * 点击事件接口
     */
    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, v: View)
    }


    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, position)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        changeSelect(ss.position)
        if (getChildAt(ss.position) != null && mTabSelectedListener != null) {
            mTabSelectedListener!!.onTabSelected(ss.position, getChildAt(ss.position).findViewById(R.id.tab_iv))
        }

    }


    /**
     * 保存点击的项
     */
    internal class SavedState : View.BaseSavedState {
        var position: Int = 0

        constructor(source: Parcel) : super(source) {
            position = source.readInt()
        }

        constructor(superState: Parcelable, position: Int) : super(superState) {
            this.position = position
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    companion object {

        //角标样式,0代表空心，1代表带数字
        private val BADGE_TYPE_HOLLOW = 0
        private val BADGE_TYPE_SOLID = 1


        private val RECYCLER_OPTIONS = RequestOptions()
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
    }

}
