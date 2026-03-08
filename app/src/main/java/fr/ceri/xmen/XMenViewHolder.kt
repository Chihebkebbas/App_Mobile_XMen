package fr.ceri.xmen

import androidx.recyclerview.widget.RecyclerView
import fr.ceri.xmen.databinding.XmenBinding
import fr.ceri.xmen.model.XMen

class XMenViewHolder(val ui : XmenBinding) : RecyclerView.ViewHolder(ui.root){

    var xmen: XMen?
        get() = null
        set(xmen) {
            if (xmen == null) return
            ui.itemImage.setImageResource(xmen.idImage!!)
            ui.itemNom.text = xmen.nom
            ui.itemAlias.text = xmen.alias
            ui.itemDesc.text = xmen.description
            ui.itemPouvoir.text = xmen.pouvoirs
        }

}