package tokyo.hiroron.KanKanKanji

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_answer.*

class answerFragment : Fragment() {

    private lateinit var answer: String

    companion object {
        private const val KEY_ANSWER = "answer"

        fun createInstance(answer: String) : answerFragment {
            val ansFragment = answerFragment()
            val args = Bundle()
            args.putString(KEY_ANSWER, answer)
            ansFragment.arguments = args
            return ansFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args == null) {
            answer = ""
        } else {
            answer = args.getString(KEY_ANSWER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        answerFragmentText.text = answer
    }
}
