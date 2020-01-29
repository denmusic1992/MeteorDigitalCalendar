package sample

import helpers.HashEncoder
import kotlin.test.Test
import kotlin.test.assertTrue

class SampleTestsAndroid {
    @Test
    fun testHello() {
        assertTrue("Android" in hello())
    }

    @Test
    fun testMD5() {
        val paramStr1 = "AuthDeviceID=26f4027e-a5c1-43a3-bb56-99b67bff520e&Client=androidnull"
        val paramStr2 =
            "AuthDeviceID=26f4027e-a5c1-43a3-bb56-99b67bff520e&DateFrom=2019-12-01&DateTo=2020-01-31&Favourite=0&Price=28619ac7d2aafedf5bfaeb7e1031a557"

        //val paramStrThis1 = "AuthDeviceID=a6c969ec-3a33-4968-891d-db7406142b8d39cc8331a6006afca33d5bfa040fd403"
        //val paramStrThis2 = "CategoryID[]=[1, 2, 3, 4]&DateTo=2020-01-31&AuthDeviceID=a6c969ec-3a33-4968-891d-db7406142b8d&DateFrom=2020-01-01&Favourite=0&Price=&CityType[]=[russia, moscow, spb]39cc8331a6006afca33d5bfa040fd403"

        val result1 = HashEncoder.getMD5Hash(paramStr1)
        val result2 = HashEncoder.getMD5Hash(paramStr2)

        val resultFromTest1 = "e6bd39b48b4e2dea8441a148b57ded6f"
        val resultFromTest2 = "15fea4b05bcf08739f44cbd0804b999e"

        println("result 1 = $result1")
        println("result 2 = $result2")
        println("Первые результаты ${if (result1 == resultFromTest1) "одинаковые" else "неодинаковые"}")
        println("Вторые результаты ${if (result2 == resultFromTest2) "одинаковые" else "неодинаковые"}")
    }
}