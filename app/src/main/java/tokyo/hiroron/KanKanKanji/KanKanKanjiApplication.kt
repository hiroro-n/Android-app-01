package tokyo.hiroron.KanKanKanji

import android.app.Application
import io.realm.Realm

class KankankanjiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}