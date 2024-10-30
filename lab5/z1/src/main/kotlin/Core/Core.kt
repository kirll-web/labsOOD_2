package Core

typealias FPCommand = (input: String) -> Unit

class Core {

    companion object {
        const val MAX_LENGTH_TEMP_NAME = 12;

        fun generateRandomFileName(fileNameLength: Int) : String{
            val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')

            return (1 ..fileNameLength).map { allowedChars.random() }.joinToString("")
        }
    }

}