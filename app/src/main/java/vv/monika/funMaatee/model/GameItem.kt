package vv.monika.funMaatee.model


data class GameItem(
    val id: Int,
    val title: String,
    val iconRes: Int ,
    val url: String,//Drawer resource id

)
//data class ek trah k template bannata hai,
// jisse hame baar baar Toy(name=Teddy, color=Brown, size=Medium) es trah se  n likhna pade
// it also provides other functions as well

//2. auto equals checker-> agar  same toy ka dusra version banana ho then val teddy = teddy.copy( size = "Big)
//checker
//val teddy2 = Toy("Teddy", "Brown", "Medium")
//println(teddy == teddy2) // true

