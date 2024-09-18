package model

import play.api.libs.json.{Json, OFormat}

case class RateReport(
    bestTotalRateProvider: Provider,
    bestProviderPerCountry: Map[Country, Provider],
    ratesPerProvider: Seq[RatePerProvider]
)

object RateReport {
  implicit val format: OFormat[RateReport] = Json.format[RateReport]
}
