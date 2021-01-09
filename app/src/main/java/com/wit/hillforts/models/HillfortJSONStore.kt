package com.wit.hillforts.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import com.wit.hillforts.helpers.*
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<User>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}
/*
class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()
    var users = mutableListOf<User>()

    constructor (context: Context) {
        this.context = context

        if (exists(context, JSON_FILE)) {
            deserialize()
        }

    }

    override fun findAll(user: User): MutableList<User> {
        return users
    }

    override fun create(hillfort: HillfortModel, user: User) {
        deserialize()
        hillfort.id = generateRandomId()
        var currentUser = findUserByEmail(user.email)
        if (currentUser != null) currentUser.hillforts.add(hillfort)
         serialize()
    }

    override fun createUser(user: User) {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
        user.id = generateRandomId()
        user.hillforts = seed(user)
        users.add(user)
        serialize()
    }

    override fun updateUser(user: User) {
        var currentUser = findUserByEmail(user.email)
        if (currentUser != null) {
            currentUser.name = user.name
            currentUser.year = user.year
            currentUser.email = user.email
            currentUser.password = user.password
        }
        serialize()
    }

    override fun clear() {

    }


    override fun update(hillfort: HillfortModel, user: User) {
        var currentUser = findUserByEmail(user.email)
        if (currentUser != null) {
            var foundHillfort: HillfortModel? = currentUser.hillforts.find { h -> h.id == hillfort.id }
            if (foundHillfort != null) {
                foundHillfort.name = hillfort.name
                foundHillfort.description = hillfort.description
                foundHillfort.image1 = hillfort.image1
                foundHillfort.image2 = hillfort.image2
                foundHillfort.image3 = hillfort.image3
                foundHillfort.image4 = hillfort.image4
                foundHillfort.lat = hillfort.lat
                foundHillfort.lng = hillfort.lng
                foundHillfort.zoom = hillfort.zoom
                foundHillfort.notes = hillfort.notes
                foundHillfort.visited = hillfort.visited
                foundHillfort.dateVisitedYear = hillfort.dateVisitedYear
                foundHillfort.dateVisitedMonth = hillfort.dateVisitedMonth
                foundHillfort.dateVisitedDay = hillfort.dateVisitedDay
                serialize()
            }
        }
    }

    override fun findUserByEmail(email: String): User? {
        for (each in users) {

            if (email == each.email) return each
        }
            return null
        }



    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }

    override fun delete(hillfort: HillfortModel, user: User) {
        var currentUser = findUserByEmail(user.email)
        if (currentUser != null) currentUser.hillforts.remove(hillfort)
        serialize()
       }


    private fun seed(user: User): MutableList<HillfortModel> {
        user.hillforts.add(HillfortModel(
            name = "Downeen"
            ,description = "This coastal stack is situated at Carrigagappul Cove, c. 2km S of Rosscarbery on the SW coast of Co. Cork. Depicted on the first edition OS six-inch map as site of Donoure Castle (1842). The site can be described as a small rocky island projecting SE into Castle Bay at an altitude of 9m OD with Downeen Castle (CO143-069002) at its N edge. There is no surface trace of earlier defences. Westropp (1914, 112) considered island 'only reached by a plank even when used as a dun in its fort-days'."
            ,lat = 51.559961
            ,lng = -9.024633
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "downeen"
            ,image2 = "downeenmap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Dundeady"
            ,description = "This headland is situated at Galley Head, c. 4km SW of Ardfield village on the SW coast of Co. Cork. Depicted as 'Dundeady Head' on the first edition OS six-inch map (1842). It can be described as an irregular promontory projecting S into the Atlantic at an altitude of 30m OD. There are no obvious remains of defences earlier than the medieval fortifications which cross neck of Dundeady (or Galley) Head for c. 95m. That said, Westropp (1914, 144) suggests these might mask earlier ones. For example, a Dip in front (to N) of the curtain wall has ditch-like appearance, though it is probably a natural feature (Power et al. 1992, 130). A possible standing stone is recorded near the centre of the headland at its highest point. No other potential early features such as hut-sites are recorded. A modern lighthouse is situated at the seaward end of the headland. Site is currently under pasture."
            ,lat = 51.531901
            ,lng = -8.953403
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "dundeady"
            ,image2 = "dundeadymap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Donoure"
            ,description = "This headland is situated at Bealcoon Cove, c. 4km E of Owenahinchy (Rosscarbery) on the SW coast of Co. Cork. The site is known as 'Dunoure' which Windele gives as 'Dun uair' (Westropp 1914, 105). It can be described as a narrow finger, the upper surface extending to no more than 0.1ha, projecting SW into Bealacoon Bay at an altitude of 4m OD. It is joined to mainland by narrow neck with V-shaped profile and is depicted on the first edition OS six-inch map as site of Donoure Castle (1842). Traces of Late Medieval defences are recorded on seaward side of the neck. There are no visible traces of earlier defences."
            ,lat = 51.543887
            ,lng = -8.956465
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "donoure"
            ,image2 = "donouremap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Reenogrena"
            ,description = "This promontory is situated at Siege Cove, c.3km SE of Glandore in West Cork. The headland can be described as a small rectangular area (L 70m; Wth 22m) projecting SE over Siege Cove at an altitude of 25m OD. Known locally as \\\"The Caisleanin\\\", it is not depicted as a defended headland on any edition of the OS six-inch map. The narrow neck of the promontory on the W is defended by a rock-cut ditch measuring 4m in width and 2m in depth. A field fence is recorded on the landward side. A stone built wall is recorded (H 1m) on N cliff-edge. There are no formal entrances into the the promontory. The E and W ends of the interior are divided by a natural fault lying N and S. A grass covered depression on the seaward side is interpreted as a possible infilled well (Power et al. 1992, 129) while a further a circular depression (diam. 6m; D 0.8m) and a sod covered arc of bank (diam. 2.6; H 0.3m) may be the remains of a possible hut-site. The site is currently much overgrown."
            ,lat = 51.548237
            ,lng = -9.074029
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "reenogrena"
            ,image2 = "reenogrenamap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Carrigillihy"
            ,description = "The site is located on the western shore of Glandore Harbour, West Cork. It can be described as a rectangular area of approximately 0.4ha projecting E into the harbour at an altitude of 10m OD. The enclosing elements are composed of a single bank and ditch on the W side with evidence of a small outer counterscarp bank. There is a single break that is probably of later date. Westropp describes that the bank was overlain with a modern fence in the early twentieth century (1914, 95). There are no recorded internal features such as hut-sites and excavation in 1952 did not find any trace of occupation in an 'archaeologically sterile' interior (O'Kelly 1952). Sections through the banks of the promontory revealed dry-stone faced bank with a core of stone rubble quarried from a steep-sided ditch. The bank also had evidence of an internal terrace. The original entrance was not identified with the excavator suggesting it may have been eroded away by coastal erosion (ibid). The site is currently under pasture."
            ,lat = 51.540364
            ,lng = -9.1114
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "carrigillihy"
            ,image2 = "carrigillihymap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Moyross"
            ,description = "The headland is located c. 2.1km SE of Castletownshend Village in West Cork. It is situated in rough pasture, on a promontory projecting E at an altitude of 12m OD, with high cliffs on three sides, and with views S to High Island and Low Island. Near its W end, the promontory is defended by the remains of an earthen bank (L 6m N-S; Wth 2.1m; H 0.55m) both ends of which are being eroded by the sea. There is an external ditch (L 6m N-S; Wth 1.8m; D 0.2m) as well as traces of a possible internal example (L 6m N-S; Wth 1m; D 0.1m). The interior narrows and slopes gently to the E for a distance of c. 25m. There are no recorded internal features within the enclosed area of c.0.02ha."
            ,lat = 51.522688
            ,lng = -9.143409
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "moyross"
            ,image2 = "moyrossmap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Reen"
            ,description = "This promontory is situated at Reen Point, immediately W of Skiddy Island and 1km S of Castletownsend on the W coast of Co. Cork. The headland can be defined as a small rectangular area projecting SW into Castlehaven Bay. Westropp (1914, 99) noted at the neck, 'two very shallow straight fosses with an intervening wall and an inner one revetting a mound'. He describes the outer ditch as a 'shallow hollow' and the next wall outside as probably recent. Defences remain as in Westropp's time. Both inner (H 1.35m) and outer (H 1.2m) banks are earthen while the inner ditch is completely infilled. The outer example is extant measuring 0.9m in depth and is crossed by a causeway associated with a central gap through both banks. Interior is grass, heather and gorse over rock outcropping. A battery-operated beacon is located on seaward edge."
            ,lat = 51.516471
            ,lng = -9.174795
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "reen"
            ,image2 = "reenmap"
            ,image3 = ""
            ,image4 = ""))
        user.hillforts.add(HillfortModel(
            name = "Portadoona"
            ,description = "The site is located c. 5km SE of Skibereen town, West Cork. It is composed of an irregular, shallow promontory of approximately 0.11ha that projects E into the Atlantic at an altitude of 24m OD. Situated on elevated land-mass of which Toehead marks the S point. The only approach to the site is from the landward side on the W, where the bank and ditch are well preserved, composed of a single curvilinear rampart with a terraced inner face. There was no evidence of an outer stone facing. The ditch was rock-cut and flat bottomed. There is a single break that is probably of later date. Westropp describes the site as a 'small headland defended by a curved fosse and mound about 120 feet long, on grassy cliffs in Scobaun townland' (1914, 95). No evidence of any internal features on the surface. Excavation of the interior did reveal two hearth sites and a single post-hole (O'Kelly 1952, 26-9). Promontory now covered by rough grazing; both seaward edges of defences suffering from erosion."
            ,lat = 51.500926
            ,lng = -9.212005
            ,visited = false
            ,notes = ""
            ,id = generateRandomId()
            ,image1 = "portadoona"
            ,image2 = "portadoonamap"
            ,image3 = ""
            ,image4 = ""))

        return user.hillforts
    }



}

*/