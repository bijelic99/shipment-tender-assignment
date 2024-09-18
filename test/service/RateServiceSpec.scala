package service

import model.Country.{Belgium, Germany, Netherlands}
import model.{Provider, RatePerProvider, Shipment}
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import repository.inMemory.InMemoryRateRepository

class RateServiceSpec extends AsyncWordSpec with Matchers {
  "RateService" when {
    val service = new RateService(new InMemoryRateRepository())
    "getRatePerProvider is called" should {
      "return empty array if empty array of shipments is provided" in {
        service.getRateReport(Nil) mustEqual None
      }

      "return correct results for provided shipments" in {
        val shipments = Seq(
          Shipment(1, Netherlands, 10),
          Shipment(2, Netherlands, 20),
          Shipment(3, Netherlands, 500),
          Shipment(4, Netherlands, 1000),
          Shipment(5, Belgium, 10),
          Shipment(6, Germany, 1000)
        )
        val report = service
          .getRateReport(shipments)
          .get

        report.bestTotalRateProvider mustEqual Provider("provider-b")

        report.bestProviderPerCountry mustEqual Map(
          Netherlands -> Provider("provider-b"),
          Belgium -> Provider("provider-a"),
          Germany -> Provider("provider-b")
        )

        report.ratesPerProvider
          .sortBy(_.provider.name) mustEqual Seq(
          RatePerProvider(
            Provider("provider-a"),
            1895.0,
            Map(Belgium -> 75.0, Netherlands -> 920.0, Germany -> 900.0)
          ),
          RatePerProvider(
            Provider("provider-b"),
            1680.0,
            Map(Belgium -> 150.0, Netherlands -> 740.0, Germany -> 790.0)
          ),
          RatePerProvider(
            Provider("provider-c"),
            2300.0,
            Map(Belgium -> 100.0, Netherlands -> 1350.0, Germany -> 850.0)
          )
        ).sortBy(_.provider.name)
      }
    }
  }
}
