package fr.ceri.xmen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.ceri.xmen.databinding.XmenBinding
import fr.ceri.xmen.model.XMen
import io.realm.Realm
import io.realm.RealmResults

class XMenAdapter (val xmens: RealmResults<XMen>) : RecyclerView.Adapter<XMenViewHolder>() {

    init {
        xmens.addChangeListener { _, changeSet ->
            for (change in changeSet.deletionRanges) {
                notifyItemRangeRemoved(change.startIndex, change.length)
            }
            for (change in changeSet.insertionRanges) {
                notifyItemRangeInserted(change.startIndex, change.length)
            }
            for (change in changeSet.changeRanges) {
                notifyItemRangeChanged(change.startIndex, change.length)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XMenViewHolder {
        val ui= XmenBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return XMenViewHolder(ui)
    }

    override fun onBindViewHolder(holder: XMenViewHolder, position: Int) {
        holder.xmen = xmens[position]
    }

    override fun getItemCount(): Int {
        return xmens.count()
    }

}