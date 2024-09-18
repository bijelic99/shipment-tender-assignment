package model

import play.api.libs.json.{Json, OFormat}

case class Provider(
    name: String
)

object Provider {
  implicit val format: OFormat[Provider] = Json.format[Provider]
}
