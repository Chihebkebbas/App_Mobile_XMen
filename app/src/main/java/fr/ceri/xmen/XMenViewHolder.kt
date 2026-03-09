package fr.ceri.xmen

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.ceri.xmen.databinding.XmenBinding
import fr.ceri.xmen.model.XMen
import org.bson.types.ObjectId

class XMenViewHolder(val ui : XmenBinding) : RecyclerView.ViewHolder(ui.root){

    companion object{
        val TAG = "TP2"
    }

    var xmenId: ObjectId? = null
    lateinit var onItemClick: (ObjectId?) -> Unit

    var xmen: XMen?
        get() = null
        set(xmen) {
            if (xmen == null) return
            xmenId = xmen.id

            ui.itemImage.setImageResource(xmen.idImage!!)
            ui.itemNom.text = xmen.nom
            ui.itemAlias.text = xmen.alias
            ui.itemDesc.text = xmen.description
            ui.itemPouvoir.text = xmen.pouvoirs
        }

    init {
        ui.root.setOnClickListener(this::onClick)
    }

    /* écouteur des clics sur le view holder */
    private fun onClick(view: View?) {
        Log.i(XMenViewHolder.TAG
            ,
            "clic sur l'élément ${xmenId}")
        onItemClick(xmenId)
    }
}
