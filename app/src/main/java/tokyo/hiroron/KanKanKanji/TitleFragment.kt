package tokyo.hiroron.KanKanKanji


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_title.*

class TitleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_title, container, false)
    }

    fun setTitle1 (title1: String) {
        titleText1.text = title1
    }

    fun setTitle2 (title2: String) {
        titleText2.text = title2
    }



}
