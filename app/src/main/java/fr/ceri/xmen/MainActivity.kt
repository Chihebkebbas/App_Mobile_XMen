package fr.ceri.xmen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.ceri.xmen.databinding.ActivityMainBinding
import fr.ceri.xmen.model.XMen
import io.realm.Realm
import org.bson.types.ObjectId

class MainActivity : AppCompatActivity() {

    private lateinit var ui : ActivityMainBinding

    private lateinit var realm: Realm



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mise en place du layout par viewbinding
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)

        setSupportActionBar(ui.toolbar) // <- - BARRE D'ACTION

        // Obtention de realm
        realm = Realm.getDefaultInstance()

        // Obtenir la liste des xmens
        val xmens = realm.where(XMen::class.java).findAllAsync()

        // créer l'adaptateur
        val adapter = XMenAdapter(xmens)

        // fournir l'adaptateur au recycler
        ui.recycler.adapter = adapter

        // dimensions constantes
        ui.recycler.setHasFixedSize(true)

        // layout manager
        val lm: RecyclerView.LayoutManager = LinearLayoutManager(this)
        ui.recycler.layoutManager = lm

        // séparateur
        val dividerItemDecoration = DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL
        )
        ui.recycler.addItemDecoration(dividerItemDecoration)


        //scrollbar
        ui.title.movementMethod = ScrollingMovementMethod()

        adapter.onItemClick = this::onItemClick

    }

    private fun onItemClick(idXMen: ObjectId?) {
        // TODO démarrer une transaction asynchrone, voir TP5
        realm.executeTransactionAsync { it ->
            // TODO récupérer le XMen dont l'identifiant est idXMen
            val xmen = it.where(XMen::class.java).equalTo(XMen.ID, idXMen).findFirst()

            // remplacer son image par R.drawable.undef
            xmen?.idImage = R.drawable.undef
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // selon l'item sélectionné
        return when (item.itemId) {
            R.id.reinit -> {
                    // ré-initialiser la liste
                    val monApp = application as XMenApplication
                    monApp.initXMens(realm)
                    true
                    }
                    R.id.create -> {
                        onEdit(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onEdit(idXMen: ObjectId? = null) {
        val intent = android.content.Intent(this, EditActivity::class.java)

        intent.putExtra(EditActivity.EXTRA_ID, idXMen?.toHexString())
        startActivity(intent)
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        // récupérer l'identifiant de l'élément concerné
        val idstr = item.intent?.getStringExtra(EditActivity.EXTRA_ID)
        val idXMen = if (idstr != null) ObjectId(idstr) else null

        // selon le menu choisi
        return when (item.itemId) {
            XMenViewHolder.MENU_EDIT -> {
            onEdit(idXMen)
            true
        }
            XMenViewHolder.MENU_DELETE -> {
                onDelete(idXMen)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }


    private fun onReallyDelete(idXMen: ObjectId?) {

        realm.executeTransactionAsync { it ->

            val xmen = it.where(XMen::class.java).equalTo(XMen.ID, idXMen).findFirst()
            xmen?.deleteFromRealm()
        }

        Display.showToast(this, "Personnage supprimé avec succès")
    }

    private fun onDelete(idXMen: ObjectId?) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setMessage("Vous confirmez la suppression ?")
            .setPositiveButton(android.R.string.ok) { dialog, idbtn ->
                // Le bouton "oui" supprime vraiment
                onReallyDelete(idXMen)
            }
            // Le bouton "non" ne fait rien
            .setNegativeButton(android.R.string.cancel, null)
            .show() // Affichage du dialogue
    }



}