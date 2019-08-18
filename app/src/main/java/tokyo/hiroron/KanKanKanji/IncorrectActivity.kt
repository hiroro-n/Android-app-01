package tokyo.hiroron.KanKanKanji

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_incorrect.*

class IncorrectActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incorrect)

        val QID = intent.getIntExtra("QID", 0)

        realm = Realm.getDefaultInstance()
        list.layoutManager = LinearLayoutManager(this)
        val incorrectQs = realm.where<IncorrectModel>().findAll()
        val adapter = IncorrectAdapter(incorrectQs)
        list.adapter = adapter
        adapter.setOnItemClickListener { id ->
            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("incorrectQid", QID)
            intent.putExtra("modelId", id)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
