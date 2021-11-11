package com.rickrip.quizapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_quiz_questions.*

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener {

    private var mCurrentPosition:Int = 1
    private var mQuestionList:ArrayList<Question>? = null
    private var mSelectedOptionPosition:Int = 0 // 0 - nothing selected
    private var mCorrectAnswers:Int = 0
    private var mUserName:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_questions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false) // for new android ver
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionList = Constants.getQuestions()
        // Log.i("'","There are ${questionsList.size} questions")

        mCurrentPosition = 1 //question numbers starts from 1
        setQuestion()

        tv_option_1.setOnClickListener(this)
        tv_option_2.setOnClickListener(this)
        tv_option_3.setOnClickListener(this)
        tv_option_4.setOnClickListener(this)
        btn_submit.setOnClickListener(this)


    }

    private fun setQuestion(){

        setDefaultOptionsView()

        if(mCurrentPosition == mQuestionList!!.size){
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        val question = mQuestionList!![mCurrentPosition-1]

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition / ${progressBar.max}"

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        tv_option_1.text = question.optionOne
        tv_option_2.text = question.optionTwo
        tv_option_3.text = question.optionThree
        tv_option_4.text = question.optionFour

    }


    private fun setDefaultOptionsView(){

        val options = ArrayList<TextView>()

        options.add(0,tv_option_1)
        options.add(1,tv_option_2)
        options.add(2,tv_option_3)
        options.add(3,tv_option_4)

        for(option in options){
            option.setTextColor(getColor(R.color.light_gray))
            //option.setTypeface(option.typeface, Typeface.DEFAULT) //text style
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }


    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.tv_option_1 ->{
                selectedOptionView(tv_option_1,1)
            }
            R.id.tv_option_2 ->{
                selectedOptionView(tv_option_2,2)
            }
            R.id.tv_option_3 ->{
                selectedOptionView(tv_option_3,3)
            }
            R.id.tv_option_4 ->{
                selectedOptionView(tv_option_4,4)
            }
            R.id.btn_submit->{

                if(mSelectedOptionPosition == 0){ // if nothing selected then just next answer

                    mCurrentPosition+=1

                    when{
                        mCurrentPosition <= mQuestionList!!.size ->{
                            setQuestion() // set next question
                        }else ->
                    {
                        val intent = Intent(this, ResultActivity::class.java)
                        intent.putExtra(Constants.USER_NAME,mUserName)
                        intent.putExtra(Constants.CORRECT_ANSWERS,mCorrectAnswers.toString())
                        intent.putExtra(Constants.TOTAL_QUESTIONS,mQuestionList!!.size.toString())
                        println(intent.getStringExtra(Constants.TOTAL_QUESTIONS))
                        startActivity(intent)
                        finish()
                        }
                    }

                }else{

                    if(mCurrentPosition>mQuestionList!!.size){mCurrentPosition=mQuestionList!!.size}

                        val question = mQuestionList?.get(mCurrentPosition-1)
                        if(question!!.correctAnswer != mSelectedOptionPosition){ // selected answer is wrong!
                            setAnswerColor(mSelectedOptionPosition,R.drawable.wrong_option_border_bg)
                        }else{mCorrectAnswers+=1}
                        setAnswerColor(question.correctAnswer,R.drawable.correct_option_border_bg)
                }

                if(mCurrentPosition >= mQuestionList!!.size){
                    btn_submit.text = "FINISH"
                }else{
                    btn_submit.text = "GO TO NEXT QUESTION"
                }

                mSelectedOptionPosition = 0 //reset elected option number

            }

        }

    }

    private fun setAnswerColor(answer:Int,drawableView:Int){

        when(answer){
            1->{
                tv_option_1.background = ContextCompat.getDrawable(this,drawableView)
            }
            2->{
                tv_option_2.background = ContextCompat.getDrawable(this,drawableView)
            }
            3->{
                tv_option_3.background = ContextCompat.getDrawable(this,drawableView)
            }
            4->{
                tv_option_4.background = ContextCompat.getDrawable(this,drawableView)
            }
        }
    }

    private fun selectedOptionView(tv:TextView,selectedOptionNum:Int){

        setDefaultOptionsView()
        mSelectedOptionPosition = selectedOptionNum
        tv.setTextColor(getColor(R.color.design_default_color_primary))
        tv.setTypeface(tv.typeface, Typeface.BOLD) //text style
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )

    }


}




















