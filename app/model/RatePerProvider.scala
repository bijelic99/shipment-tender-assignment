package model

import play.api.libs.json.{Json, OFormat}

case class RatePerProvider(
    provider: Provider,
    total: Double,
    perCountry: Map[Country, Double]
)

object RatePerProvider {
  implicit val format: OFormat[RatePerProvider] = Json.format[RatePerProvider]
}
