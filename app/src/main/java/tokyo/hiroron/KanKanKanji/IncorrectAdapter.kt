package tokyo.hiroron.KanKanKanji

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class IncorrectAdapter (data: OrderedRealmCollection<IncorrectModel>)
            : RealmRecyclerViewAdapter<IncorrectModel, IncorrectAdapter.ViewHolder> (data, true) {

    private var listener: ((Long?) -> Unit)? = null

    fun setOnItemClickListener (listener: (Long?) -> Unit) {
        this.listener = listener
    }

    init {
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        val question: TextView = cell.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncorrectAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncorrectAdapter.ViewHolder, position: Int) {
        val model: IncorrectModel? = getItem(position)
        holder.question.text = model?.question
        holder.itemView.setOnClickListener {
            listener?.invoke(model?.id)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0
    }
}