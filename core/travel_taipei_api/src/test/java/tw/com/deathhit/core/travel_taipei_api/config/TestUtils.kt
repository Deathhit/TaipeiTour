package tw.com.deathhit.core.travel_taipei_api.config

import tw.com.deathhit.core.travel_taipei_api.protocol.response.GetAttractionsResponse
import java.util.UUID
import kotlin.random.Random

internal fun generateGetAttractionsResponse() =
    GetAttractionsResponse(
        data = mutableListOf<GetAttractionsResponse.Attraction>().apply {
            for (i in 0..getRandomInt()) {
                add(
                    GetAttractionsResponse.Attraction(
                        address = getRandomStr(),
                        id = getRandomStr(),
                        images = mutableListOf<GetAttractionsResponse.Attraction.Image>().apply {
                            for (j in 0..getRandomInt()) {
                                add(
                                    GetAttractionsResponse.Attraction.Image(src = getRandomStr())
                                )
                            }
                        }.toList(),
                        introduction = getRandomStr(),
                        modified = getRandomStr(),
                        name = getRandomStr(),
                        url = getRandomStr()
                    )
                )
            }
        }.toList()
    )


private fun getRandomInt() = Random.nextInt(3, 10)
private fun getRandomStr() = UUID.randomUUID().toString()