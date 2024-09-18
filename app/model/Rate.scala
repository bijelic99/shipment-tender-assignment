package model

import play.api.libs.json.{Json, OFormat}

case class Rate(
    provider: Provider,
    weightFrom: Long,
    weightTo: Long,
    price: Double
)

object Rate {
  implicit val ordering: Ordering[Rate] = Ordering.by[Rate, Long](_.weightFrom)

  def apply(
      weightFrom: Long,
      weightTo: Long,
      price: Double
  )(implicit provider: Provider): Rate =
    Rate(provider, weightFrom, weightTo, price)

  implicit val format: OFormat[Rate] = Json.format[Rate]
}
