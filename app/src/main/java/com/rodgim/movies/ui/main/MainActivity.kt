package com.rodgim.movies.ui.main

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.rodgim.movies.R
import com.rodgim.movies.databinding.ActivityMainBinding
import com.rodgim.movies.ui.discovery.HintViewItem
import com.rodgim.movies.ui.discovery.contentholder.DialogHintContentHolder
import com.rodgim.movies.ui.discovery.contentholder.ExtraTargetContentHolder
import com.rodgim.movies.ui.discovery.contentholder.SimpleIconContentHolder
import com.rodgim.movies.ui.discovery.hintcase.HintCase
import com.rodgim.movies.ui.discovery.hintcase.shapeanimators.RevealRectangularShapeAnimator
import com.rodgim.movies.ui.discovery.hintcase.shapeanimators.UnrevealRectangularShapeAnimator
import com.rodgim.movies.ui.discovery.hintcase.shapes.RectangularShape
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val hintList: ArrayList<HintViewItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_container)
        navHostFragment?.let {
            navController = it.findNavController()
            setupWithNavController(binding.navigationView, navController)
        }
        prepareHintList()
    }

    private fun prepareHintList() {
        hintList.add(
            HintViewItem(
                target1 = binding.navigationView.findViewById(R.id.menu_movie),
                text = "Menu movie"
            )
        )
        hintList.add(
            HintViewItem(
                target1 = binding.navigationView.findViewById(R.id.menu_favorite),
                text = "Menu favorite"
            )
        )
        hintList.add(
            HintViewItem(
                target1 = binding.navigationView.findViewById(R.id.menu_search),
                text = "Menu search"
            )
        )
        binding.root.postDelayed({
            showHints(0)
        }, 300)
    }

    private fun showHints(position: Int) {
        if (position < hintList.size) {
            val hintItem = hintList[position]
            if (hintItem.type == HintViewItem.Type.ONE_TARGET) {
                showHintOneTarget(hintItem.target1, hintItem.text, position)
            } else if (hintItem.type == HintViewItem.Type.TWO_TARGET){
                showHintTwoTargets(hintItem, position)
            }
        }
    }

    private fun showHintTwoTargets(hintViewItem: HintViewItem, position: Int) {
        hintViewItem.target1?.let { topView ->
            hintViewItem.target2?.let { bottomView ->
                var isCloseClicked = false
                val dialogHintContainerHolder = DialogHintContentHolder.Builder(this)
                    .setContentText(hintViewItem.text)
                    .setBorder(R.dimen.hint_bubble_border, R.color.delivered)
                    .setMarginByResourcesId(
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp
                    )
                    .setContentPaddingByResourcesId(
                        R.dimen.margin_8_dp,
                        R.dimen.margin_8_dp,
                        R.dimen.margin_8_dp,
                        R.dimen.margin_8_dp
                    )
                    .setContentStyle(R.style.ContentHint)
                    .setExtraTargetHeight(topView.height)
                    .build()

                val extraTarget = ExtraTargetContentHolder.Builder(topView.context)
                    .setTargetView(topView)
                    .setBackgroundResource(R.drawable.background_no_border)
                    .build()

                val iconContentHolder = SimpleIconContentHolder.Builder(bottomView.context)
                    .setRules(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT)
                    .setIconAdjustViewBounds(true)
                    .setScaleType(ImageView.ScaleType.CENTER_INSIDE)
                    .setImageResourceId(R.drawable.ic_cancel)
                    .setCloseHintCaseOnClick(true)
                    .setOnClick(object : SimpleIconContentHolder.OnClickIconListener {
                        override fun onClick() {
                            isCloseClicked = true
                        }
                    })
                    .setMarginByResourcesId(
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp,
                        R.dimen.margin_16_dp
                    )
                    .build()
                HintCase(window.decorView, this)
                    .setTarget(bottomView, RectangularShape(), R.dimen.margin_0_dp)
                    .setShapeAnimators(RevealRectangularShapeAnimator(), UnrevealRectangularShapeAnimator())
                    .setHintBlock(dialogHintContainerHolder)
                    .setExtraBlock(iconContentHolder)
                    .setExtraBlock(extraTarget)
                    .setOnClosedListener(object : HintCase.OnClosedListener {
                        override fun onClosed() {
                            if (!isCloseClicked) {
                                showHints(position + 1)
                            }
                        }
                    })
                    .show()
            }
        }
    }

    private fun showHintOneTarget(view: View?, text: String, position: Int) {
        if (view != null) {
            var isCloseClicked = false
            val dialogHintContentHolder = DialogHintContentHolder.Builder(this)
                .setContentText(text)
                .setBorder(R.dimen.hint_bubble_border, R.color.delivered)
                .setMarginByResourcesId(
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp
                )
                .setContentPaddingByResourcesId(
                    R.dimen.margin_8_dp,
                    R.dimen.margin_8_dp,
                    R.dimen.margin_8_dp,
                    R.dimen.margin_8_dp
                )
                .setContentStyle(R.style.ContentHint)
                .build()

            val iconContentHolder = SimpleIconContentHolder.Builder(view.context)
                .setRules(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_END)
                .setIconAdjustViewBounds(true)
                .setScaleType(ImageView.ScaleType.CENTER_INSIDE)
                .setImageResourceId(R.drawable.ic_cancel)
                .setCloseHintCaseOnClick(true)
                .setOnClick(object : SimpleIconContentHolder.OnClickIconListener{
                    override fun onClick() {
                        isCloseClicked = true
                    }

                })
                .setMarginByResourcesId(
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp,
                    R.dimen.margin_16_dp
                )
                .build()

            HintCase(window.decorView, this)
                .setTarget(view, RectangularShape(), R.dimen.margin_0_dp)
                .setShapeAnimators(RevealRectangularShapeAnimator(), UnrevealRectangularShapeAnimator())
                .setHintBlock(dialogHintContentHolder)
                .setExtraBlock(iconContentHolder)
                .setOnClosedListener(object : HintCase.OnClosedListener {
                    override fun onClosed() {
                        if (!isCloseClicked){
                            showHints(position + 1)
                        }
                    }
                })
                .show()
        }
    }
}