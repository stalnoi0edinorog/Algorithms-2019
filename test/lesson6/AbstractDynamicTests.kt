package lesson6

import kotlin.test.assertEquals

abstract class AbstractDynamicTests {
    fun longestCommonSubSequence(longestCommonSubSequence: (String, String) -> String) {
        assertEquals("", longestCommonSubSequence("мой мир", "я"))
        assertEquals("рата сферу", longestCommonSubSequence("Стратосфера, Носферату", "Траты на сферу"))
        assertEquals("__-___", longestCommonSubSequence("-__-_--___-", "1_1_1-___1"))
        assertEquals("олиария", longestCommonSubSequence("Полиамория", "полиандрия"))
        assertEquals("13", longestCommonSubSequence("123", "13"))
        assertEquals("здс", longestCommonSubSequence("здравствуй мир", "мы здесь"))
        assertEquals("emt ole", longestCommonSubSequence("nematode knowledge", "empty bottle"))
        assertEquals("", longestCommonSubSequence("Экс-граф?_Плюш_изъят.", "Бьём чуждый цен хвощ!"))
        val expectedLength = "e kerwelkkd r".length
        assertEquals(
            expectedLength, longestCommonSubSequence(
                "oiweijgw kejrhwejelkrw kjhdkfjs hrk",
                "perhkhk lerkerorwetp lkjklvvd durltr"
            ).length, "Answer must have length of $expectedLength, e.g. 'e kerwelkkd r' or 'erhlkrw kjk r'"
        )
        val expectedLength2 = """ дд саы чтых,
евшнео ваа се сви дн.
        """.trimIndent().length
        assertEquals(
            expectedLength2, longestCommonSubSequence(
                """
Мой дядя самых честных правил,
Когда не в шутку занемог,
Он уважать себя заставил
И лучше выдумать не мог.
                """.trimIndent(),
                """
Так думал молодой повеса,
Летя в пыли на почтовых,
Всевышней волею Зевеса
Наследник всех своих родных.
                """.trimIndent()
            ).length, "Answer must have length of $expectedLength2"
        )
    }

    fun longestIncreasingSubSequence(longestIncreasingSubSequence: (List<Int>) -> List<Int>) {
        assertEquals(listOf(), longestIncreasingSubSequence(listOf()))
        assertEquals(listOf(1), longestIncreasingSubSequence(listOf(1)))
        assertEquals(listOf(1, 2), longestIncreasingSubSequence(listOf(1, 2)))
        assertEquals(listOf(2), longestIncreasingSubSequence(listOf(2, 1)))
        assertEquals(
            listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
            longestIncreasingSubSequence(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        )
        assertEquals(listOf(2, 8, 9, 12), longestIncreasingSubSequence(listOf(2, 8, 5, 9, 12, 6)))
        assertEquals(listOf(2, 8, 8, 9, 12), longestIncreasingSubSequence(listOf(2, 8, 5, 8, 9, 12, 6)))
        assertEquals(listOf(2, 5, 8, 8, 9, 12), longestIncreasingSubSequence(listOf(2, 5, 8, 8, 9, 12, 6)))
        assertEquals(listOf(10), longestIncreasingSubSequence(listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)))
        assertEquals(listOf(2, 8, 9, 12), longestIncreasingSubSequence(listOf(2, 8, 5, 9, 12)))
        assertEquals(listOf(5, 10, 12, 24), longestIncreasingSubSequence(listOf(5, 10, 6, 12, 3, 24, 7, 8)))
        assertEquals(
            listOf(23, 34, 56, 87, 91, 98, 140, 349), longestIncreasingSubSequence(
                listOf(
                    23, 76, 34, 93, 123, 21, 56, 87, 91, 12, 45, 98, 140, 12, 5, 38, 349, 65, 94,
                    45, 76, 15, 99, 100, 88, 84, 35, 88
                )
            )
        )
        assertEquals(
            listOf(11, 22, 27, 33, 444, 555), longestIncreasingSubSequence(
                listOf(11, 5, 22, 6, 27, 7, 33, 8, 444, 555)
            )
        )
    }

    fun shortestPathOnField(shortestPathOnField: (String) -> Int) {
        assertEquals(1, shortestPathOnField("input/field_in2.txt"))
        assertEquals(12, shortestPathOnField("input/field_in1.txt"))
        assertEquals(43, shortestPathOnField("input/field_in3.txt"))
        assertEquals(28, shortestPathOnField("input/field_in4.txt"))
        assertEquals(222, shortestPathOnField("input/field_in5.txt"))
        assertEquals(15, shortestPathOnField("input/field_in6.txt"))
    }

}