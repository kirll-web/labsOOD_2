import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class Test {

    @Test
    fun testToInt() {
        assertThrows<Exception> {
            "astasd".toInt()
            "2.3".toInt()
        }
        assertDoesNotThrow  {
            "2".toInt()
        }
    }

}