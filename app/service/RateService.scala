package service

import com.google.inject.Inject
import model.{RatePerProvider, RateReport, Shipment}
import repository.RateRepository
import util.CsvParser

import java.nio.file.Path

class RateService @Inject() (
    rateRepository: RateRepository
) {

  def getRateReport(shipments: Seq[Shipment]): Option[RateReport] = if (
    shipments.nonEmpty
  ) {
    val shipmentsPerCountry = shipments.groupBy(_.country)
    val ratesPerProvider = rateRepository
      .getApplicableRatesPerProvider(
        shipmentsPerCountry.view.mapValues(_.map(_.weight).toSet).toMap
      )
      .map { case (provider, perCountryRates) =>
        val summedPerCountryRates = shipmentsPerCountry.map {
          case (country, shipments) =>
            val rates = perCountryRates(country)
            val summed = shipments
              .map(s =>
                rates
                  .find(r => r.weightFrom <= s.weight && s.weight <= r.weightTo)
                  .get
                  .price
              )
              .sum
            country -> summed
        }
        val total = summedPerCountryRates.values.sum
        RatePerProvider(provider, total, summedPerCountryRates)
      }
      .toSeq
    val bestTotalRateProvider = ratesPerProvider.minBy(_.total).provider
    val bestPerCountryProvider = ratesPerProvider
      .flatMap { case RatePerProvider(provider, _, perCountry) =>
        perCountry.map { case (country, total) =>
          country -> (total, provider)
        }
      }
      .groupMap(_._1)(_._2)
      .view
      .mapValues(_.minBy(_._1)._2)
      .toMap
    Some(
      RateReport(
        bestTotalRateProvider,
        bestPerCountryProvider,
        ratesPerProvider
      )
    )
  } else {
    None
  }

  def getRatePerProvider(shipmentsCsv: Path): Option[RateReport] = {
    val shipments = CsvParser.parseFile[Shipment](shipmentsCsv)(Shipment.apply)
    getRateReport(shipments)
  }
}
