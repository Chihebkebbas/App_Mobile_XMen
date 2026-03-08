package fr.ceri.xmen

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import fr.ceri.xmen.model.XMen

class XMenApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("my-realm")
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .build()
        Realm.setDefaultConfiguration(config)

        val realm = Realm.getDefaultInstance()
        initXMens(realm)
        realm.close()
    }

    fun initXMens(realm: Realm) {
        realm.executeTransactionAsync {
            if (it.where(XMen::class.java).count() == 0.toLong()) {
                // accès aux ressources
                val res = getResources()
                val noms = res.getStringArray(R.array.noms)
                val alias = res.getStringArray(R.array.alias)
                val description = res.getStringArray(R.array.descriptions)
                val pouvoirs = res.getStringArray(R.array.pouvoirs)
                val images = res.obtainTypedArray(R.array.idimages)

                // recopier les données dans la base Realm
                for (i in 0..noms.size - 1) {
                    // constructeur avec tous les paramètres
                    XMen.create(
                        realm = it,
                        nom = noms[i],
                        alias = alias[i],
                        idImage = images.getResourceId(i, R.drawable.undef),
                        description = description[i],
                        pouvoirs = pouvoirs[i]
                    )
                }
                // libérer les images (obligation Android)
                images.recycle()
            }

        }
    }
}