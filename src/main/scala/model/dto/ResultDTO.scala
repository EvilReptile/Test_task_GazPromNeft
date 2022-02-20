package model.dto

import play.api.libs.json.{Format, Json}

case class ResultDTO(createTimestamp: Long,
                     timestamp: Long,
                     title: String,
                     language: String,
                     wiki: String,
                     categories: Seq[String],
                     auxilaryTexts: Seq[String])

object ResultDTO {
    implicit val format: Format[ResultDTO] = Json.format[ResultDTO]
}
