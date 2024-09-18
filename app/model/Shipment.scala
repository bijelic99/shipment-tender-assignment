package model

import play.api.libs.json.{Json, OFormat}

case class Shipment(
    number: Long,
    country: Country,
    weight: Double
)

object Shipment {
  def apply(array: Array[String]): Shipment = {
    val Array(numberStr, countryCode, weightStr) = array
    Shipment(
      numberStr.toLong,
      Json.parse(s"\"$countryCode\"").as[Country],
      weightStr.toDouble
    )
  }
  implicit val format: OFormat[Shipment] = Json.format[Shipment]
}
