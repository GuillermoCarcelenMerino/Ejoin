package app.Ejoin.DataClasses

data class Chat(
    var evento : Boolean = false,
    var id : String = "",
    var photo : String = "",
    var users : List<String> = emptyList()
)


