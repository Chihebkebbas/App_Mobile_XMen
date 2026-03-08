package fr.ceri.xmen.model

import androidx.annotation.DrawableRes
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class XMen (
    @PrimaryKey var id: ObjectId? = null,
    var nom: String? = null,
    var alias: String? = null,
    var description: String? = null,
    var pouvoirs: String? = null,
    @DrawableRes var idImage: Int? = null
): RealmObject() {
    companion object {
        val ID = "id"
        val NOM = "nom"
        val ALIAS = "alias"
        val DESCRIPTION = "description"
        val POUVOIRS = "pouvoirs"

        fun create (realm: Realm, nom: String, alias: String, description: String, pouvoirs: String, @DrawableRes idImage: Int, id: ObjectId = ObjectId()): ObjectId {
            val xmen = realm.createObject(XMen::class.java, id)
            xmen.nom = nom
            xmen.alias = alias
            xmen.description = description
            xmen.pouvoirs = pouvoirs
            xmen.idImage = idImage
            return id
        }

    }
}