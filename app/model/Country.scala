package model

import play.api.libs.json.{
  Format,
  JsError,
  JsString,
  JsSuccess,
  Json,
  Reads,
  Writes
}

sealed class Country(val code: String)

object Country {
  case object Netherlands extends Country("NL")
  case object Belgium extends Country("BE")
  case object Germany extends Country("DE")

  private object StringCountry {
    def unapply(string: String): Option[Country] =
      string.toUpperCase match {
        case "NL" => Some(Netherlands)
        case "BE" => Some(Belgium)
        case "DE" => Some(Germany)
        case _    => None
      }
  }

  implicit val format: Format[Country] = Format(
    Reads[Country] {
      case JsString(StringCountry(country)) => JsSuccess(country)
      case v => JsError(s"Couldn't parse country, invalid value: '$v'")
    },
    Writes[Country](c => JsString(c.code))
  )
}
