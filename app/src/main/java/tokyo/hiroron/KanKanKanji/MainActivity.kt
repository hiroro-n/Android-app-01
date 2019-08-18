package tokyo.hiroron.KanKanKanji

import android.app.Activity
import android.content.Intent
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
                      tokyo.hiroron.KanKanKanji.AlertDialog.Listener {

    private lateinit var a1Answer: String
    private lateinit var a2Answer: String
    private lateinit var a3Answer: String
    private lateinit var a4Answer: String

    private lateinit var aF: Fragment
    private var containerId = 0

    private lateinit var soundPool: SoundPool
    private var soundCorrect = 0
    private var soundIncorrect = 0
    private lateinit var judgeCorrect: String

    private lateinit var realm: Realm
    private var modelId: Long =0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realm = Realm.getDefaultInstance()

        val fragment = titleFragment as? TitleFragment
        fragment?.setTitle1("この漢字、　　")
        fragment?.setTitle2("　かけるひと～？")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text1.setText(Html.fromHtml(
                "<big><big><big><b>①</big></big></big></b>" + " もんだいをつくる（<big><b>↓</big>おしてね</b>）", 1))
        } else {
            text1.setText(Html.fromHtml(
                "<big><big><big><b>①</big></big></big></b> もんだいをつくる（<big><b>↓</big>おしてね</b>）"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text2.setText(Html.fromHtml(
                "<big><big><big><b>②</big></big></big></b> わかった漢字のもんだい<big><b>↑</big>おしてね</b>", 2))
        } else {
            text2.setText(Html.fromHtml(
                "<big><big><big><b>②</big></big></big></b> わかった漢字のもんだい<big><b>↑</big>おしてね</b>"))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text3.setText(Html.fromHtml(
                "<big><b>←</big></b> <big><big><big><b>③</big></big></big></b><b>おしてね</b> <big><b>→</big></b>", 3))
        } else {
            text3.setText(Html.fromHtml(
                "<big><b>←</big></b> <big><big><big><b>③</big></big></big></b><b>おしてね</b> <big><b>→</big></b>"))
        }


        //他のアクティビティから戻ってきた時、設定済の問題をセットする
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val Q1Text = pref.getString("Q1", "")
        val A1Text = pref.getString("A1", "")
        val Q2Text = pref.getString("Q2", "")
        val A2Text = pref.getString("A2", "")
        val Q3Text = pref.getString("Q3", "")
        val A3Text = pref.getString("A3", "")
        val Q4Text = pref.getString("Q4", "")
        val A4Text = pref.getString("A4", "")

        if (Q1Text != null) {
            q1Button.text = Q1Text
            a1Answer = A1Text!!
        }
        if (Q2Text != null) {
            q2Button.text = Q2Text
            a2Answer = A2Text!!
        }
        if (Q3Text != null) {
            q3Button.text = Q3Text
            a3Answer = A3Text!!
        }
        if (Q4Text != null) {
            q4Button.text = Q4Text
            a4Answer = A4Text!!
        }


        //①　空のボタンへ問題・答えをセット
        q1Button.setOnClickListener { questionButtonClick(q1Button, 1) }
        q2Button.setOnClickListener { questionButtonClick(q2Button, 2) }
        q3Button.setOnClickListener { questionButtonClick(q3Button, 3) }
        q4Button.setOnClickListener { questionButtonClick(q4Button, 4) }

        //②　正解・不正解ボタンが押されたら
        correctButton.setOnClickListener {
            correctButtonClick(correctButton, R.drawable.ic_correct2, soundCorrect, "correct")
        }
        incorrectButton.setOnClickListener {
            correctButtonClick(incorrectButton, R.drawable.ic_incorrect2, soundIncorrect, "incorrect")
        }
    }


    override fun onResume() {
        super.onResume()

        //②-0　音の準備
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()
        soundCorrect = soundPool.load(this, R.raw.correct_sound, 1)
        soundIncorrect = soundPool.load(this, R.raw.incorrect_sound, 1)

        //③　「IncorrectActivity」で選ばれた問題をセットする
        realm = Realm.getDefaultInstance()
        val incorrectQid = intent.getIntExtra("incorrectQid", 0)
        modelId = intent.getLongExtra("modelId", -1L)

        if (modelId != -1L) {
            val model = realm.where<IncorrectModel>().equalTo("id", modelId).findFirst()

            when(incorrectQid) {
                R.id.q1Button -> { q1Button.text = model?.question
                    a1Answer = model?.answer!! }
                R.id.q2Button -> { q2Button.text = model?.question
                    a2Answer = model?.answer!! }
                R.id.q3Button -> { q3Button.text = model?.question
                    a3Answer = model?.answer!! }
                R.id.q4Button -> { q4Button.text = model?.question
                    a4Answer = model?.answer!! }
            }
        }
    }


    //→①
    private fun questionButtonClick (qButton:Button, cId:Int) {
        correctButton.setImageResource(R.drawable.correct)

          //①-1　問題が入ってなければ作成画面へ
        if (qButton.text.isEmpty()) {
            val intent = Intent(this, SubActivity::class.java)
            intent.putExtra("Q", qButton.id)
            startActivityForResult(intent, 1)

          //①-3　問題が入っていれば答えを表示する
        } else {
            when (cId) {
                1 -> { aF = answerFragment.createInstance(a1Answer) }
                2 -> { aF = answerFragment.createInstance(a2Answer) }
                3 -> { aF = answerFragment.createInstance(a3Answer) }
                4 -> { aF = answerFragment.createInstance(a4Answer) }
            }
            supportFragmentManager.beginTransaction()
                                  .replace(R.id.container, aF, "F")
                                  .commit()
            containerId = cId
        }
    }

    //①-2　作成画面から問題と答えを受け取ってセットする
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1   &&   resultCode == Activity.RESULT_OK   &&   data != null) {

            val id = data.getIntExtra("returnQ", 0)
            val Question = data.getStringExtra("question")
            val Answer = data.getStringExtra("answer")

            when(id) {
                R.id.q1Button -> { q1Button.text = Question
                                   a1Answer = Answer }
                R.id.q2Button -> { q2Button.text = Question
                                   a2Answer = Answer }
                R.id.q3Button -> { q3Button.text = Question
                                   a3Answer = Answer }
                R.id.q4Button -> { q4Button.text = Question
                                   a4Answer = Answer }
            }
        }
    }


    //→②
    private fun correctButtonClick (cButton:ImageButton, cBId:Int, sId:Int, jC:String) {
        if (supportFragmentManager.findFragmentByTag("F") == null) {
            Toast.makeText(this, "①→②→③だよ", Toast.LENGTH_SHORT).show()

         //②-1　正解・不正解ボタンが押された時の共通処理
        } else {
            cButton.setImageResource(cBId)
            soundPool.play(sId, 1.0f, 1.0f, 0, 0, 1.0f)

            realm.executeTransaction { db: Realm ->
                db.where<IncorrectModel>().equalTo("id", modelId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }

            judgeCorrect = jC


            //②-1-1　正解ボタンの時は問題・答えを消す
            if (judgeCorrect == "correct") {
                when (containerId) {
                    1 -> q1Button.text = null
                    2 -> q2Button.text = null
                    3 -> q3Button.text = null
                    4 -> q4Button.text = null
                }
                supportFragmentManager.beginTransaction().remove(aF).commit()
            }

            //②-1-2　不正解ボタンの時はダイアログを表示
            if (judgeCorrect == "incorrect") {
                val dialog = tokyo.hiroron.KanKanKanji.AlertDialog()
                dialog.show(supportFragmentManager, "alertDialog")
            }
        }
    }

    //②-1-2　ダイアログで「あとで」が選ばれたら、保存してから問題・答えを消す
    override fun pressDialogMenu(dialogMenuId: Int) {
        if (dialogMenuId == 1) {
            realm.executeTransaction { db: Realm ->
                val maxId = db.where<IncorrectModel>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) +1
                val cell = db.createObject<IncorrectModel>(nextId)

                when(containerId) {
                    1 -> { cell.question = q1Button.text.toString()
                           cell.answer = a1Answer }
                    2 -> { cell.question = q2Button.text.toString()
                           cell.answer = a2Answer }
                    3 -> { cell.question = q3Button.text.toString()
                           cell.answer = a3Answer }
                    4 -> { cell.question = q4Button.text.toString()
                           cell.answer = a4Answer }
                }
            }

            when (containerId) {
                1 -> q1Button.text = null
                2 -> q2Button.text = null
                3 -> q3Button.text = null
                4 -> q4Button.text = null
            }
            supportFragmentManager.beginTransaction().remove(aF).commit()

        } else {
            supportFragmentManager.beginTransaction().remove(aF).commit()
        }

        incorrectButton.setImageResource(R.drawable.incorrect)
    }


    override fun onPause() {
        super.onPause()
        soundPool.release()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            putString("Q1", q1Button.text.toString())
            putString("A1", a1Answer)
            putString("Q2", q2Button.text.toString())
            putString("A2", a2Answer)
            putString("Q3", q3Button.text.toString())
            putString("A3", a3Answer)
            putString("Q4", q4Button.text.toString())
            putString("A4", a4Answer)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.edit {
            clear()
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
        finish()
    }
}



