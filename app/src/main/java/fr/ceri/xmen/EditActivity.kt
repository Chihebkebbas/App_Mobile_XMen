package fr.ceri.xmen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import fr.ceri.xmen.databinding.ActivityEditBinding
import fr.ceri.xmen.model.XMen
import io.realm.Realm
import org.bson.types.ObjectId

class EditActivity : AppCompatActivity() {

    private lateinit var ui: ActivityEditBinding
    private lateinit var realm: Realm

    companion object {
        const val EXTRA_ID = "id" //
    }

    private var idXMen: ObjectId? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        ui = ActivityEditBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar)
        realm = Realm.getDefaultInstance()


        val idstr = intent.getStringExtra(EXTRA_ID)
        idXMen = if (idstr != null) ObjectId(idstr) else null
        Log.i(XMenViewHolder.TAG, "EditActivity on $idXMen")

        if (idXMen != null) {
            realm.executeTransactionAsync { it ->
                val xmen = it.where(XMen::class.java).equalTo(XMen.ID, idXMen).findFirst()


                setXMen(xmen)
            }
        }
    }

    // Afficher le menu de validation
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // clique valider
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.accept -> {
                onAccept()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // créer / éditer XMen en base de données
    private fun onAccept() {
        realm.executeTransactionAsync { it ->
            if (idXMen == null) {
                // création
                XMen.create(it, ui.nom.text.toString(), ui.alias.text.toString(), ui.description.text.toString(), ui.pouvoirs.text.toString(), R.drawable.undef)
            } else {
                // édition
                val xmenEdit = it.where(XMen::class.java).equalTo(XMen.ID, idXMen).findFirst()
                xmenEdit?.apply {
                    nom = ui.nom.text.toString()
                    alias = ui.alias.text.toString()
                    description = ui.description.text.toString()
                    pouvoirs = ui.pouvoirs.text.toString()
                }
            }
        }
        finish()
    }

    private fun setXMen(xmen: XMen?) {
        if (xmen == null) return
        ui.nom.setText(xmen.nom)
        ui.alias.setText(xmen.alias)
        ui.description.setText(xmen.description)
        ui.pouvoirs.setText(xmen.pouvoirs)
    }


    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}