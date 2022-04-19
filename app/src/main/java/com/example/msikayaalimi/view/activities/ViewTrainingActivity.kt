package com.example.msikayaalimi.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.msikayaalimi.Firestore.FirestoreClass
import com.example.msikayaalimi.R
import com.example.msikayaalimi.models.TrainingInfo
import com.example.msikayaalimi.models.TrainingMenuItem
import com.example.msikayaalimi.view.adapters.QuizMenuAdapter
import com.example.msikayaalimi.controller.*

class ViewTrainingActivity : BaseActivity() {
    private lateinit var mMenuItemId: String
    private lateinit var mMenuItem: TrainingMenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_training)

        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_MENU_ITEM_ID)){
            mMenuItemId = intent.getStringExtra(Constants.EXTRA_MENU_ITEM_ID)!!
            Log.i("Menu Item Id", mMenuItemId)
        }

        getMenuItemDetails()

        val btnTakeQuiz:MYAButton = findViewById(R.id.btn_start_quiz)

        btnTakeQuiz.setOnClickListener {
            val intent = Intent(this, ViewQuizActivity::class.java)
            intent.putExtra(Constants.EXTRA_QUIZ_NAME, "Crop Rotation")
            startActivity(intent)
        }


    }

    fun getMenuItemDetails() {

        FirestoreClass().getCurrentMenuItem(this, mMenuItemId)
        showProgressDialog(resources.getString(R.string.please_wait))

    }

    fun successfullyLoadedMenuItem(menuItem:TrainingMenuItem) {
        hideProgressDialog()
        mMenuItem = menuItem

        val ivTrainingPhoto:ImageView = findViewById(R.id.iv_training_photo)
        val tvTrainingTitle:MYATextViewBold = findViewById(R.id.training_title)
        val toolbarTraining:Toolbar = findViewById(R.id.view_training_activity_toolbar)

        GlideLoader(this).loadProductImage(menuItem.image, ivTrainingPhoto)
        tvTrainingTitle.text = mMenuItem.title

        if (mMenuItem.title == "Farm Practices"){
            toolbarTraining.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.blue
                )
            )
        }

        if (mMenuItem.title == "Herd Management"){
            toolbarTraining.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        }

        if (mMenuItem.title == "Self-sufficiency"){
            toolbarTraining.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.pink
                )
            )
        }

        if (mMenuItem.title == "Quizzes"){
            toolbarTraining.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.burgundy
                )
            )
            val pageLayout:ConstraintLayout = findViewById(R.id.view_training_layout)

            pageLayout.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.ColorSecondary
                )
            )

            getQuizMenu()

        }
        if (mMenuItem.title != "Quizzes"){
            getTrainingInfo()
        }



    }

    private fun getQuizMenu(){
        FirestoreClass().getQuizMenuItems(this)
        showProgressDialog(resources.getString(R.string.please_wait))
    }

    fun successfullyLoadedQuizMenu(quizMenu:ArrayList<TrainingMenuItem>){
        hideProgressDialog()

        val tvTitle:MYATextViewBold = findViewById(R.id.tv_view_training_title)
        val tvIntro:MYATextView = findViewById(R.id.tv_view_training_introduction)
        val flTutorial:FrameLayout = findViewById(R.id.fl_view_training_tutorial)
        val tvInfo:MYATextView = findViewById(R.id.tv_view_training_information)
        val btnTakeQuiz:MYAButton = findViewById(R.id.btn_start_quiz)
        val rvQuizMenu:RecyclerView = findViewById(R.id.rv_quiz_menu)
        val tvEmptyMenu:MYATextView = findViewById(R.id.tv_empty_quiz_menu)

        if (quizMenu.size > 0) {
            tvInfo.visibility = View.GONE
            tvTitle.visibility = View.GONE
            tvIntro.visibility = View.GONE
            flTutorial.visibility = View.GONE
            btnTakeQuiz.visibility = View.GONE
            rvQuizMenu.visibility = View.VISIBLE

            val quizMenuAdapter = QuizMenuAdapter(this, quizMenu)
            rvQuizMenu.layoutManager = LinearLayoutManager(this)
            rvQuizMenu.adapter = quizMenuAdapter
            rvQuizMenu.setHasFixedSize(true)
        }else{
            tvInfo.visibility = View.GONE
            tvIntro.visibility = View.GONE
            flTutorial.visibility = View.GONE
            btnTakeQuiz.visibility = View.GONE
            tvTitle.visibility = View.GONE
            tvEmptyMenu.visibility = View.VISIBLE
        }


    }

    private fun setupActionBar() {
        val toolbarTraining = findViewById(R.id.view_training_activity_toolbar) as Toolbar
        setSupportActionBar(toolbarTraining)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbarTraining.setNavigationOnClickListener{ onBackPressed()}
    }

    private fun getTrainingInfo(){
        FirestoreClass().getTrainingInfo(this, mMenuItem.title)
    }

    fun successfullyLoadedTrainingInfo(trainingInfo: MutableList<TrainingInfo>){
        val title:MYATextViewBold = findViewById(R.id.tv_view_training_title)
        val ivFirstPhoto:ImageView = findViewById(R.id.iv_training_tutorial)
        val introText:MYATextView = findViewById(R.id.tv_view_training_introduction)
        val mainInfo:MYATextView = findViewById(R.id.tv_view_training_information)

        val train = trainingInfo[0]

        title.text = train.title
        introText.text = train.introInfo
        mainInfo.text = train.mainInfo

        if (train.image != ""){
            GlideLoader(this).loadProductImage(train.image, ivFirstPhoto)
        }
    }
}