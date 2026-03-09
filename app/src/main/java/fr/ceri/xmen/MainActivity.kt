package fr.ceri.xmen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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


}