package tokyo.hiroron.KanKanKanji

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub.*
import kotlinx.android.synthetic.main.activity_sub.titleFragment


class SubActivity : AppCompatActivity() {

    private var Qid = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val fragment = titleFragment as? TitleFragment
        fragment?.setTitle1("もんだいを　　")
        fragment?.setTitle2("　　つくろう！")

        Qid = intent.getIntExtra("Q", 0)

        registerButton.setOnClickListener {
            var isValid = true

            if (question.text.isEmpty()) {
                question.error = getString(R.string.questionError)
                isValid = false
            }

            if (answer.text.isEmpty()) {
                answer.error = getString(R.string.answerError)
                isValid = false
            }

            if (isValid) {
                val result = Intent()
                result.putExtra("returnQ", Qid)
                result.putExtra("question", question.text.toString())
                result.putExtra("answer", answer.text.toString())
                setResult(Activity.RESULT_OK, result)

                finish()
            }
        }

        challengeButton.setOnClickListener {
            val intent = Intent(this, IncorrectActivity::class.java)
            intent.putExtra("QID", Qid )
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY)
        startActivity(intent)
    }
}

