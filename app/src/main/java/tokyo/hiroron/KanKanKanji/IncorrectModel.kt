package tokyo.hiroron.KanKanKanji

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class IncorrectModel : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var question: String = ""
    var answer: String = ""
}