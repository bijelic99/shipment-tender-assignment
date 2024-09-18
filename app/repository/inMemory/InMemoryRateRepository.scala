package repository.inMemory

import com.google.inject.{Inject, Singleton}
import model.{Country, Provider, Rate}
import repository.RateRepository

import scala.collection.immutable.SortedSet

@Singleton
class InMemoryRateRepository @Inject() extends RateRepository {

  private val providerCountryRateMap
      : Map[Provider, Map[Country, SortedSet[Rate]]] = Map(
    {
      implicit val providerA: Provider = Provider("provider-a")
      providerA -> Map[Country, SortedSet[Rate]](
        Country.Netherlands -> SortedSet(
          Rate(0, 100, 50),
          Rate(101, 250, 110),
          Rate(251, 500, 170),
          Rate(501, 750, 290),
          Rate(751, 1000, 650)
        ),
        Country.Belgium -> SortedSet(
          Rate(0, 100, 75),
          Rate(101, 250, 160),
          Rate(251, 500, 240),
          Rate(501, 750, 480),
          Rate(751, 1000, 700)
        ),
        Country.Germany -> SortedSet(
          Rate(0, 100, 50),
          Rate(101, 250, 150),
          Rate(251, 500, 440),
          Rate(501, 750, 780),
          Rate(751, 1000, 900)
        )
      )
    }, {
      implicit val providerB: Provider = Provider("provider-b")
      providerB -> Map(
        Country.Netherlands -> SortedSet(
          Rate(0, 250, 100),
          Rate(251, 500, 190),
          Rate(501, 750, 270),
          Rate(751, 1000, 350)
        ),
        Country.Belgium -> SortedSet(
          Rate(0, 250, 150),
          Rate(251, 500, 250),
          Rate(501, 750, 490),
          Rate(751, 1000, 650)
        ),
        Country.Germany -> SortedSet(
          Rate(0, 250, 100),
          Rate(251, 500, 390),
          Rate(501, 750, 680),
          Rate(751, 1000, 790)
        )
      )
    }, {
      implicit val providerC: Provider = Provider("provider-c")
      providerC -> Map(
        Country.Netherlands -> SortedSet(
          Rate(0, 100, 50),
          Rate(101, 200, 80),
          Rate(201, 300, 120),
          Rate(301, 400, 200),
          Rate(401, 500, 300),
          Rate(501, 600, 500),
          Rate(601, 700, 600),
          Rate(701, 800, 800),
          Rate(801, 900, 950),
          Rate(901, 1000, 950)
        ),
        Country.Belgium -> SortedSet(
          Rate(0, 100, 100),
          Rate(101, 200, 120),
          Rate(201, 300, 300),
          Rate(301, 400, 330),
          Rate(401, 500, 360),
          Rate(501, 600, 390),
          Rate(601, 700, 500),
          Rate(701, 800, 550),
          Rate(801, 900, 600),
          Rate(901, 1000, 800)
        ),
        Country.Germany -> SortedSet(
          Rate(0, 100, 40),
          Rate(101, 200, 80),
          Rate(201, 300, 130),
          Rate(301, 400, 400),
          Rate(401, 500, 450),
          Rate(501, 600, 500),
          Rate(601, 700, 550),
          Rate(701, 800, 600),
          Rate(801, 900, 700),
          Rate(901, 1000, 850)
        )
      )
    }
  )

  override def getApplicableRatesPerProvider(
      countryWeightMap: Map[Country, Set[Double]]
  ): Map[Provider, Map[Country, SortedSet[Rate]]] = {
    val lookupCountrySet = countryWeightMap.keySet
    providerCountryRateMap.collect {
      case (provider, countryRates)
          if lookupCountrySet.diff(countryRates.keySet).isEmpty =>
        provider -> countryRates.collect {
          case (country, rates) if lookupCountrySet.contains(country) =>
            country -> rates.filter { case Rate(_, weightFrom, weightTo, _) =>
              countryWeightMap(country).exists(w =>
                weightFrom <= w && w <= weightTo
              )
            }
        }
    }
  }
}
