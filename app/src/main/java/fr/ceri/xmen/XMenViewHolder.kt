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
        val MENU_EDIT = 1
        val MENU_DELETE = 2
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

        ui.root.setOnCreateContextMenuListener(this::onCreateContextMenu)
    }

    /* écouteur des clics sur le view holder */
    private fun onClick(view: View?) {
        Log.i(XMenViewHolder.TAG
            ,
            "clic sur l'élément ${xmenId}")
        onItemClick(xmenId)
    }

    private fun onCreateContextMenu(menu: android.view.ContextMenu, v: android.view.View?, menuInfo: android.view.ContextMenu.ContextMenuInfo?) {
        // intent sans action pour fournir l'identifiant du XMen à MainActivity
        val intent = android.content.Intent().putExtra(EditActivity.EXTRA_ID, xmenId?.toHexString())

        // titre du menu = nom du XMen
        menu.setHeaderTitle(ui.itemNom.text)

        menu.add(0, MENU_EDIT, 0, "Edit").intent = intent
        menu.add(0, MENU_DELETE, 0, "Delete").intent = intent
    }
}
