package tokyo.hiroron.KanKanKanji

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AlertDialog : DialogFragment() {

    interface Listener {
        fun pressDialogMenu(dialogMenuId: Int)
    }

    private var listener : Listener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        when (context) {
            is Listener -> listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialogMenu = arrayOf("　　→　すぐにチャレンジする", "　　→　あとでチャレンジする")

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("まちがえた漢字を追加しました")
               .setItems(dialogMenu) { dialog, which -> listener?.pressDialogMenu(which) }

        return builder.create()
    }
}
